package com.gear.poi.search.postgis.service;

import com.gear.poi.search.postgis.entity.PoiEntity;
import com.gear.poi.search.postgis.repository.PoiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PoiService {

    private final PoiRepository poiRepository;

    @Transactional
    public void saveAllAndFlush(List<PoiEntity> poiEntities) {
        for (PoiEntity poiEntity : poiEntities) {
            poiRepository.savePoi(poiEntity);
        }
        poiRepository.flush();
    }

}
