package com.gear.poi.search.pgvector.repository;

import com.gear.poi.search.pgvector.entity.PoiEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 然后存储库
 *
 * @author guoyingdong
 * @date 2024/04/03
 */
public interface PoiRepository extends JpaRepository<PoiEntity, Long> {

//    @Query(value = "SELECT d.* FROM poi AS d WHERE d.name like concat('%',:word, '%') limit 10", nativeQuery = true)
//    List<PoiEntity> queryByKeyWord(@Param("word") String word);

    @Query(value = "SELECT d.* FROM poi AS d ORDER BY words_vec <-> cast(:wordVec as vector) limit 100", nativeQuery = true)
    List<PoiEntity> queryByKeyWord(@Param("wordVec") String wordVec);

    @Query(value = "SELECT d.* FROM poi AS d WHERE ST_DWithin(geography(:point), d.location_geography, :distance) limit 100", nativeQuery = true)
    List<PoiEntity> findWithin(@Param("point") Point point, @Param("distance") Double distance);

    @Modifying
    @Query(value = "INSERT INTO poi(name, address, type, info, words_vec) " +
            "VALUES (:#{#poiEntity.name}, :#{#poiEntity.address}, :#{#poiEntity.type}, :#{#poiEntity.info}, " +
            "cast(:#{#poiEntity.avgVectorFromWords} as vector))", nativeQuery = true)
    void savePoi(@Param("poiEntity") PoiEntity poiEntity);
}
