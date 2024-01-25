package com.gear.pt.repository;

import com.gear.pt.entity.PoiEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PoiRepository  extends CrudRepository<PoiEntity, Long> {

    //4326(wgs84)这个坐标系以度为单位.要想转成米为单位的话得做一下转换
    //GEOCS代表的是地理坐标系，也就是以经纬度表示的坐标系统,例如4326
    //PROJCS代表的投影坐标系，它是通过一种算法把球面坐标系转成平面坐标系，以便计算，一般是以米为单位表示, 例如26986
    @Query(value = "SELECT d.* FROM tb_poi AS d WHERE ST_DWithin(ST_Transform(:point,3857)," +
            "ST_Transform(d.location,3857), :distance)", nativeQuery = true)
    List<PoiEntity> findWithin(@Param("point") Point point, @Param("distance") Integer distance);

}
