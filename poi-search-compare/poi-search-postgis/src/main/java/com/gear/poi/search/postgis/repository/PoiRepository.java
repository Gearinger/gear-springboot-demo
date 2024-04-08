package com.gear.poi.search.postgis.repository;

import com.gear.poi.search.postgis.entity.PoiEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

    @Query(value = "SELECT d.* FROM poi AS d WHERE d.key_words @@ to_tsquery(:word) limit 100", nativeQuery = true)
    List<PoiEntity> queryByKeyWord(@Param("word") String word);

    @Query(value = "SELECT d.* FROM poi AS d WHERE ST_DWithin(geography(:point), d.location_geography, :distance) limit 100", nativeQuery = true)
    List<PoiEntity> findWithin(@Param("point") Point point, @Param("distance") Double distance);

    @Modifying
    @Query(value = "INSERT INTO poi(name, address, type, info, location_geography, key_words) " +
            "VALUES (:#{#poiEntity.name}, :#{#poiEntity.address}, :#{#poiEntity.type}, :#{#poiEntity.info}, " +
            "geography(:#{#poiEntity.location}), " +
            "to_tsvector(:#{#poiEntity.keyWords}))", nativeQuery = true)
    void savePoi(@Param("poiEntity") PoiEntity poiEntity);
}
