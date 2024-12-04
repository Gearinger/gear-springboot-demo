package com.gear.db;

import com.gear.model.LicenseParam;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class LicenseServerInfo extends LicenseParam {

    private String activeCode;

    private Timestamp createTime;

    public LicenseServerInfo(LicenseParam licenseParam, String activeCode) {
        this.setLicenseName(licenseParam.getLicenseName());
        this.activeCode = activeCode;
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.setCpuSerial(licenseParam.getCpuSerial());
        this.setExpireDate(licenseParam.getExpireDate());
        this.setIp(licenseParam.getIp());
        this.setMac(licenseParam.getMac());
        this.setMainBoard(licenseParam.getMainBoard());
    }
}
