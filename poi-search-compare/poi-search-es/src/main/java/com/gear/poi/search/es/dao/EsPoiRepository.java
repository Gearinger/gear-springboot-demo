package com.gear.poi.search.es.dao;

import cn.hutool.core.lang.Snowflake;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.GeoDistanceType;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.gear.poi.search.es.dto.BaseDTO;
import com.gear.poi.search.es.dto.PoiDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EsPoiRepository {

    private final ElasticsearchClient esClient;

    /**
     * 雪花ID
     */
    private final Snowflake snowflake = new Snowflake(1, 1);


    /**
     * 创建索引
     *
     * @param index 指数
     * @return {@link Boolean}
     */
    public Boolean createIndexWithPointField(String index, String pointFieldName) {
        try {
            CreateIndexResponse response = esClient.indices().create(c -> c
                    .index(index)
                    .mappings(m -> m.properties(pointFieldName, p -> p.geoPoint(g -> g.ignoreZValue(true))))
            );
        } catch (Exception e) {
            log.warn("创建索引失败", e);
            return false;
        }
        return true;
    }

    /**
     * 删除索引
     *
     * @param index 指数
     * @return {@link Boolean}
     */
    public Boolean deleteIndex(String index) {
        try {
            esClient.indices().delete(d -> d
                    .index(index)
            );
        } catch (Exception e) {
            log.warn("删除索引失败", e);
            return false;
        }
        return true;
    }

    /**
     * 存在索引
     *
     * @param index 指数
     * @return {@link Boolean}
     */
    public Boolean existsIndex(String index) {
        try {
            BooleanResponse exists = esClient.indices().exists(d -> d
                    .index(index)
            );
            return exists.value();
        } catch (Exception e) {
            log.warn("查询索引失败", e);
            return false;
        }
    }

    /**
     * 插入文档
     *
     * @param t t
     * @return long
     */
    public <T extends BaseDTO> long insertDoc(String index, T t) {
        IndexResponse response = null;
        if (t.getId() == null) {
            t.setId(snowflake.nextIdStr());
        }
        try {
            response = esClient.index(i -> i
                    .index(index)
                    .id(t.getId())
                    .document(t)
            );
        } catch (Exception e) {
            log.warn("插入文档失败", e);
        }
        return response.version();
    }

    /**
     * 获取文档
     *
     * @param index  指数
     * @param id     身份证件
     * @param tClass t类
     * @return {@link T}
     */
    public <T extends BaseDTO> T getDoc(String index, String id, Class<T> tClass) {
        try {
            GetResponse<T> response = esClient.get(g -> g
                            .index(index)
                            .id(id),
                    tClass
            );
            if (response.found()) {
                return response.source();
            } else {
                log.warn("未查询到文档");
            }
        } catch (Exception e) {
            log.warn("查询文档失败", e);
        }
        return null;
    }

    /**
     * 搜索文档
     *
     * @param index      指数
     * @param fieldName  字段名称
     * @param searchText 搜索文本
     * @param tClass     t类
     * @return {@link List}<{@link T}>
     */
    public <T> List<T> searchDocs(String index, String fieldName, String searchText, Class<T> tClass) {
        SearchResponse<T> response = null;
        try {
            response = esClient.search(s -> s
                            .index(index)
                            .query(q -> q
                                    .match(t -> t
                                            .field(fieldName)
                                            .query(searchText)
                                    )
                            ),
                    tClass
            );
            return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("查询文档失败", e);
        }
        return new ArrayList<>();
    }

    /**
     * 更新文档
     *
     * @param index  指数
     * @param t      t
     * @param tClass t类
     * @return boolean
     */
    public <T extends BaseDTO> boolean updateDoc(String index, T t, Class<T> tClass) {
        try {
            esClient.update(u -> u
                            .index(index)
                            .id(t.getId())
                            .upsert(t),
                    tClass
            );
            return true;
        } catch (Exception e) {
            log.warn("更新文档失败", e);
            return false;
        }
    }

    /**
     * 删除单据
     *
     * @param index 指数
     * @param id    身份证件
     * @return boolean
     */
    public <T extends BaseDTO> boolean deleteDoc(String index, String id) {
        try {
            esClient.delete(d -> d
                    .index(index)
                    .id(id)
            );
            return true;
        } catch (Exception e) {
            log.warn("删除文档失败", e);
            return false;
        }
    }

    public <T> List<T> searchByDistance(String index, Point pos, Double distance, Class<T> tClass) {
        try {
            SearchResponse<T> response = esClient.search(s -> s
                            .index(index)
                            .query(q -> q
                                    .bool(b -> b
                                            .must(m -> m.matchAll(m2 -> m2))
                                            .filter(f -> f.geoDistance(
                                                    g -> g.distance(String.valueOf(distance))
                                                            .distanceType(GeoDistanceType.Arc)
                                                            .field("location")
                                                            .location(l -> l
                                                                    .latlon(l2 -> l2
                                                                            .lat(pos.getY())
                                                                            .lon(pos.getX())
                                                                    )
                                                            )
                                            ))
                                    )
                            ),
                    tClass
            );
            return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("查询文档失败", e);
        }
        return new ArrayList<>();
    }

    public boolean saveAllAndFlush(String index, List<PoiDTO> poiEntityList) {
        if (CollectionUtils.isEmpty(poiEntityList)) {
            return false;
        }
        List<BulkOperation> bulkOperations = new ArrayList<>();
        for (PoiDTO poiDTO : poiEntityList) {
            bulkOperations.add(BulkOperation.of(b -> b
                            .index(i -> i
                                    .index(index)
                                    .document(poiDTO)
                                    .id(poiDTO.getId())
                            )
                    )
            );
        }
        try {
            esClient.bulk(b -> b.operations(bulkOperations));
            return true;
        } catch (IOException e) {
            log.warn("批量插入文档失败", e);
            return false;
        }
    }
}
