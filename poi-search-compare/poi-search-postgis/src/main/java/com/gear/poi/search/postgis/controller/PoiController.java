package com.gear.poi.search.postgis.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ZipUtil;
import com.gear.poi.search.postgis.entity.PoiEntity;
import com.gear.poi.search.postgis.repository.PoiRepository;
import com.gear.poi.search.postgis.service.PoiService;
import com.huaban.analysis.jieba.JiebaSegmenter;
import idea.verlif.mock.data.MockDataCreator;
import idea.verlif.mock.data.creator.data.DoubleRandomCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@RestController
@RequestMapping("/poi")
@RequiredArgsConstructor
public class PoiController {

    private final PoiRepository poiRepository;

    private final PoiService poiService;
    private final static JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();

    @GetMapping("/queryByKeyWord")
    public List<PoiEntity> queryByKeyWord(@RequestParam String word) {
        List<String> strings = jiebaSegmenter.sentenceProcess(word);
        String keyWords = strings.stream().reduce((a, b) -> a + " | " + b).orElse("");
        return poiRepository.queryByKeyWord(keyWords);
    }

    @GetMapping("/findWithin")
    public List<PoiEntity> findWithin(@RequestParam String pointWkt, @RequestParam Double distance) throws ParseException {
        Point point = (Point) new WKTReader().read(pointWkt);
        return poiRepository.findWithin(point, distance);
    }

    @PostMapping("/importDataByShp")
    public String importDataByShp(@RequestPart MultipartFile file) throws IOException {
        long startTime = System.currentTimeMillis();
        verifyFile(file);
        File unzip = ZipUtil.unzip(file.getInputStream(), new File("./temp/", file.getOriginalFilename()), Charset.defaultCharset());
        Optional<File> first = Arrays.stream(unzip.listFiles(f -> f.getName().endsWith(".shp"))).findFirst();
        if (!first.isPresent()) {
            throw new IllegalStateException("未找到shp文件");
        }
        File shpFile = first.get();
        Map<String, Object> map = new HashMap();
        map.put("url", shpFile.toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        SimpleFeatureIterator features = featureCollection.features();
        List<PoiEntity> poiEntityList = new ArrayList<>();
        int importCount = 0;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 0, java.util.concurrent.TimeUnit.SECONDS, new java.util.concurrent.ArrayBlockingQueue<Runnable>(1000));
        while (features.hasNext()) {
            SimpleFeature next = features.next();
            PoiEntity poiEntity = new PoiEntity();
            poiEntity.setName(String.valueOf(next.getAttribute("name")));
            poiEntity.setLocation((Point) next.getAttribute("the_geom"));
            poiEntity.setType(String.valueOf(next.getAttribute("type")));
            poiEntity.setAddress(String.valueOf(next.getAttribute("addr")));
            List<String> strings = jiebaSegmenter.sentenceProcess(poiEntity.getName() + poiEntity.getAddress());
            poiEntity.setKeyWords(strings.toString());
            poiEntityList.add(poiEntity);
            importCount++;
            if (poiEntityList.size() >= 3000) {
                log.info("导入poi数据 {} 条", importCount);
                ArrayList<PoiEntity> poiEntities = new ArrayList<>(poiEntityList);
                executor.execute(() -> {
                    poiService.saveAllAndFlush(poiEntities);
                    log.info("{},导入poi数据 {} 条", Thread.currentThread().getName(), poiEntities.size());
                });
                poiEntityList.clear();
            }
        }
        features.close();
        dataStore.dispose();
        poiService.saveAllAndFlush(poiEntityList);
        log.info("导入poi数据完成，共 {} 条, 耗时{}", importCount, (System.currentTimeMillis() - startTime) / 1000.0);
        return "导入poi数据完成，共 " + importCount + " 条";
    }


    @PostMapping("/createMockData")
    public String createMockData(@RequestParam Integer count) throws IOException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 0, java.util.concurrent.TimeUnit.SECONDS, new java.util.concurrent.ArrayBlockingQueue<Runnable>(1000));
        MockDataCreator creator = new MockDataCreator();
        GeometryFactory geometryFactory = new GeometryFactory();
        creator.getConfig()
                // 开启级联构建并对默认值进行替换
                .autoCascade(true).forceNew(true)
                .fieldObject(Point.class, null)
                // 如果需要，也可以设定其他double类型的默认范围
                .fieldValue(double.class, new DoubleRandomCreator(0D, 1000D));
        List<PoiEntity> poiEntityList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PoiEntity poiEntity = creator.mock(PoiEntity.class);
            Coordinate coordinate = new Coordinate(RandomUtil.randomDouble(100, 140), RandomUtil.randomDouble(20, 40));
            poiEntity.setLocation(geometryFactory.createPoint(coordinate));
            poiEntityList.add(poiEntity);
            if (poiEntityList.size() >= 3000) {
                ArrayList<PoiEntity> poiEntities = new ArrayList<>(poiEntityList);
                executor.execute(() -> {
                    poiRepository.saveAllAndFlush(poiEntities);
                    log.info("{},导入poi数据 {} 条", Thread.currentThread().getName(), poiEntities.size());
                });

                poiEntityList.clear();
            }
        }
        poiRepository.saveAllAndFlush(poiEntityList);
        log.info("导入poi数据完成，共 {} 条", count);
        return "导入poi数据完成，共 " + count + " 条";
    }

    private static void verifyFile(MultipartFile file) {
        boolean empty = file.isEmpty();
        if (empty) {
            throw new IllegalStateException("文件为空");
        }
        boolean endsWith = file.getOriginalFilename().endsWith(".zip");
        if (!endsWith) {
            throw new IllegalStateException("文件格式错误");
        }
    }

}
