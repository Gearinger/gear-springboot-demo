package com.gear.poi.search.es.service;

import com.gear.poi.search.es.dao.EsPoiRepository;
import com.gear.poi.search.es.dto.PoiDTO;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EsPoiService {

    private final EsPoiRepository esPoiRepository;

    public boolean saveAllAndFlush(List<PoiDTO> poiEntityList) {
        return esPoiRepository.saveAllAndFlush("poi", poiEntityList);
    }

    public List<PoiDTO> searchDocs(String word) {
        return esPoiRepository.searchDocs("poi", "name", word, PoiDTO.class);
    }

    public List<PoiDTO> searchByDistance(Point point, Double distance) {
        return esPoiRepository.searchByDistance("poi", point, distance, PoiDTO.class);
    }
}
