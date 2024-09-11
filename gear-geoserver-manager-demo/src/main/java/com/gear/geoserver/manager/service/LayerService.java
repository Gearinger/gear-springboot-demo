package com.gear.geoserver.manager.service;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 图层服务
 *
 * @author guoyingdong
 * @date 2024/07/03
 */
@Service
@RequiredArgsConstructor
public class LayerService {

    private final GeoServerRESTPublisher geoServerRESTPublisher;

    public Boolean publishLayer(String workspaceName, String storeName, String layerName) {
        GSFeatureTypeEncoder gSFeatureTypeEncoder = new GSFeatureTypeEncoder();
        gSFeatureTypeEncoder.setName(layerName);
        gSFeatureTypeEncoder.setTitle(layerName);
        gSFeatureTypeEncoder.setSRS("EPSG:4326");
        gSFeatureTypeEncoder.set("onlineresource", "");
        GSLayerEncoder gSLayerEncoder = new GSLayerEncoder();

        return geoServerRESTPublisher.publishDBLayer(workspaceName, storeName, gSFeatureTypeEncoder, gSLayerEncoder);
    }

}
