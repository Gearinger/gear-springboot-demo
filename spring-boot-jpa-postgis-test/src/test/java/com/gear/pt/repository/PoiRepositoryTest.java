package com.gear.pt.repository;

import com.gear.pt.Application;
import com.gear.pt.entity.PoiEntity;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;


@SpringBootTest(classes = Application.class)
class PoiRepositoryTest {

    @Autowired
    PoiRepository  poiRepository  ;

    @Test
    void testAdd(){
        PoiEntity poi= new PoiEntity ();
        poi.setId(1L);
        poi.setName("test");
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(120.153576, 30.287459));
        point.setSRID(4326);
        poi.setLocation(point);
        poiRepository.save(poi);
    }

    @Test
    void testFindOne(){
        Optional<PoiEntity> byId =poiRepository.findById(1L);
        if(byId.isPresent()){
            PoiEntity poi = byId.get();
            Point location = poi.getLocation();
            if(location!=null){
                System.out.println(location.getX()+"--"+location.getY());
            }
        }
    }

    @Test
    void testFindWithIn(){
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(120.153576, 30.287459));
        point.setSRID(4326);
        //查找当前1000m范围内的poi
        List<PoiEntity> within = poiRepository.findWithin(point, 1000);
        System.out.println(within.size());
    }
}