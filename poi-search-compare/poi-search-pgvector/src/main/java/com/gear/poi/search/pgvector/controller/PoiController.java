package com.gear.poi.search.pgvector.controller;

import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import com.gear.poi.search.pgvector.entity.PoiEntity;
import com.gear.poi.search.pgvector.repository.PoiRepository;
import com.gear.poi.search.pgvector.service.PoiService;
import com.gear.poi.search.pgvector.word2vec.Word2Vec;
import com.huaban.analysis.jieba.JiebaSegmenter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
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

    private final Word2Vec word2Vec;

    @GetMapping("/queryByKeyWord")
    public List<PoiEntity> queryByKeyWord(@RequestParam String word) {
        List<String> strings = jiebaSegmenter.sentenceProcess(word);
        float[] floats = this.caculateAvgVectorFromWords(strings);
        String words_vec = JSONUtil.toJsonStr(floats);
        return poiRepository.queryByKeyWord(words_vec);
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
            poiEntity.setInfo(String.valueOf(next.getAttribute("business_h")));
            poiEntity.setAddress(String.valueOf(next.getAttribute("addr")));
            List<String> strings = jiebaSegmenter.sentenceProcess(poiEntity.getName() + poiEntity.getAddress());
            poiEntity.setKeyWords(strings.toString());

            float[] avgVectorFromWords = this.caculateAvgVectorFromWords(strings);
            if(avgVectorFromWords.length!=300){
                continue;
            }
            poiEntity.setAvgVectorFromWords(JSONUtil.toJsonStr(avgVectorFromWords));

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

    private float[] caculateAvgVectorFromWords(List<String> strings) {
        float[] floats = strings.stream()
                .map(word2Vec::getWordVector)
                .filter(Objects::nonNull)
                .reduce((a, b) -> {
                    for (int i = 0; i < a.length; i++) {
                        a[i] += b[i];
                    }
                    return a;
                }).orElse(new float[0]);
        for (int i = 0; i < floats.length; i++) {
            floats[i] = floats[i] / strings.size();
        }
        return floats;
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
