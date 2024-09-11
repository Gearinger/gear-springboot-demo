package com.gear.db;

import com.gear.model.LicenseParam;
import lombok.Data;

@Data
public class LicenseServerInfo extends LicenseParam {

    private String activeCode;

    public LicenseServerInfo(LicenseParam licenseParam, String activeCode) {
        this.activeCode = activeCode;
        this.setCpuSerial(licenseParam.getCpuSerial());
        this.setExpireDate(licenseParam.getExpireDate());
        this.setIp(licenseParam.getIp());
        this.setMac(licenseParam.getMac());
        this.setMainBoard(licenseParam.getMainBoard());
    }
}
