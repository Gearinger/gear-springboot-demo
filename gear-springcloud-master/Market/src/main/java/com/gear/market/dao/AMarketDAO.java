package com.gear.market.dao;

import com.gear.market.dto.AMarketDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * amarket刀
 *
 * @author Gear
 * @date 2021/06/12
 */
@Component
public class AMarketDAO {
    /**
     * 查询amarket列表
     *
     * @return {@link List<AMarketDTO>}
     */
    public List<AMarketDTO> queryAMarketList(){
        List<AMarketDTO> aMarketDTOS = new ArrayList<>();
        aMarketDTOS.add(new AMarketDTO());
        return aMarketDTOS;
    }
}
