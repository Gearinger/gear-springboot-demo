package com.gear.geoserver.manager.controller;

import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTLayerList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地理服务器管理器控制器
 *
 * @author guoyingdong
 * @date 2024/07/03
 */
@RestController
public class GeoserverManagerController {

    private final GeoServerRESTReader geoServerRestReader;

    public GeoserverManagerController(GeoServerRESTReader geoServerRestReader) {
        this.geoServerRestReader = geoServerRestReader;
    }

    @GetMapping("/list")
    public List<String> list() {
        RESTLayerList layers = geoServerRestReader.getLayers();

        List<String> layerNames = layers.getNames();
        return layerNames;
    }

}
