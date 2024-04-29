package com.gear.poi.search.es.controller;

import cn.hutool.core.util.ZipUtil;
import com.gear.poi.search.es.dao.EsPoiRepository;
import com.gear.poi.search.es.dto.PoiDTO;
import com.gear.poi.search.es.service.EsPoiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.WKTWriter2;
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

/**
 * es然后控制器
 *
 * @author guoyingdong
 * @date 2024/04/03
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class EsPoiController {

    private final EsPoiRepository esPoiRepository;

    private final EsPoiService esPoiService;

    @GetMapping("/queryByKeyWord")
    public List<PoiDTO> queryByKeyWord(@RequestParam String word) {
        return esPoiService.searchDocs(word);
    }

    @GetMapping("/findWithin")
    public List<PoiDTO> findWithin(@RequestParam String pointWkt, @RequestParam Double distance) throws ParseException {
        Point point = (Point) new WKTReader().read(pointWkt);
        return esPoiService.searchByDistance(point, distance);
    }

    @GetMapping("/deleteIndex")
    public Boolean deleteIndex(@RequestParam String index) throws ParseException {
        return esPoiRepository.deleteIndex(index);
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
        List<PoiDTO> poiEntityList = new ArrayList<>();
        if (!esPoiRepository.existsIndex("poi")) {
            esPoiRepository.createIndexWithPointField("poi", "location");
        }
        int importCount = 0;
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 0, java.util.concurrent.TimeUnit.SECONDS, new java.util.concurrent.ArrayBlockingQueue<Runnable>(1000));
        while (features.hasNext()) {
            SimpleFeature next = features.next();
            PoiDTO poiEntity = new PoiDTO();
            poiEntity.setName(String.valueOf(next.getAttribute("name")));
            Point point = (Point) next.getAttribute("the_geom");
            String pointWkt = WKTWriter2.toPoint(point.getCoordinate());
            poiEntity.setLocation(pointWkt);
            poiEntity.setType(String.valueOf(next.getAttribute("type")));
            poiEntity.setAddress(String.valueOf(next.getAttribute("addr")));
            poiEntityList.add(poiEntity);
            importCount++;
            if (poiEntityList.size() >= 3000) {
                log.info("导入poi数据 {} 条", importCount);
                ArrayList<PoiDTO> poiEntities = new ArrayList<>(poiEntityList);
                executor.execute(() -> {
                    esPoiService.saveAllAndFlush(poiEntities);
                    log.info("{},导入poi数据 {} 条", Thread.currentThread().getName(), poiEntities.size());
                });
                poiEntityList.clear();
            }
        }
        features.close();
        dataStore.dispose();
        esPoiService.saveAllAndFlush(poiEntityList);
        log.info("导入poi数据完成，共 {} 条, 耗时{}", importCount, (System.currentTimeMillis() - startTime) / 1000.0);
        return "导入poi数据完成，共 " + importCount + " 条";
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
