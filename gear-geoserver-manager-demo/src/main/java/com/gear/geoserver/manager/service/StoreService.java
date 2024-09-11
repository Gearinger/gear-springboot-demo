package com.gear.geoserver.manager.service;

import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 存储服务
 *
 * @author guoyingdong
 * @date 2024/07/03
 */
@Service
@RequiredArgsConstructor
public class StoreService {

    private final GeoServerRESTStoreManager geoServerRestStoreManager;

    /**
     * 创建post-gis存储
     *
     * @param workspaceName 工作区名称
     * @param storeName     商店名称
     * @return {@link Boolean}
     */
    public Boolean createPostGisStore(String workspaceName, String storeName) {
        GSPostGISDatastoreEncoder encoder = new GSPostGISDatastoreEncoder(storeName);
        return geoServerRestStoreManager.create(workspaceName, encoder);
    }

    public Boolean removePostGisStore(String workspaceName, String storeName, boolean recurse) {
        GSPostGISDatastoreEncoder encoder = new GSPostGISDatastoreEncoder(storeName);
        return geoServerRestStoreManager.remove(workspaceName, encoder, recurse);
    }

    public Boolean updatePostGisStore(String workspaceName, String storeName) {
        GSPostGISDatastoreEncoder encoder = new GSPostGISDatastoreEncoder(storeName);
        return geoServerRestStoreManager.update(workspaceName, storeName, encoder);
    }

}
