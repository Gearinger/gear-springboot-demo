package com.gear.geoserver.manager.service;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 工作空间服务
 *
 * @author guoyingdong
 * @date 2024/07/03
 */
@Service
@RequiredArgsConstructor
public class WorkSpaceService {

    private final GeoServerRESTPublisher geoServerRESTPublisher;

    public Boolean createWorkSpace(String workSpaceName) {
        return geoServerRESTPublisher.createWorkspace(workSpaceName);
    }

    public Boolean removeWorkspace(String workSpaceName, boolean recurse) {
        return geoServerRESTPublisher.removeWorkspace(workSpaceName, recurse);
    }
}
