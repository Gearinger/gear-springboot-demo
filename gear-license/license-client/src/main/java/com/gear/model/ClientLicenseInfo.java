package com.gear.model;

import com.gear.model.LicenseParam;
import lombok.Data;

/**
 * 许可证信息
 *
 * @author guoyingdong
 * @date 2024/09/11
 */
@Data
public class ClientLicenseInfo extends LicenseParam {

    private Boolean isActive;

    public ClientLicenseInfo(LicenseParam licenseParam) {
        this.setCpuSerial(licenseParam.getCpuSerial());
        this.setMainBoard(licenseParam.getMainBoard());
        this.setIp(licenseParam.getIp());
        this.setExpireDate(licenseParam.getExpireDate());
        this.setMac(licenseParam.getMac());
    }
}
