package com.gear.service;

import com.gear.common.RSAUtils;
import com.gear.config.LicenseServerConfig;
import com.gear.db.LicenseServerInfo;
import com.gear.model.LicenseParam;
import com.gear.model.RsaKeyPair;
import com.gear.util.LicenseCreateUtil;
import com.gear.util.LicenseVerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.gear.constant.Constant.PUBLIC_KEY;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final LicenseServerConfig licenseServerConfig;

    private static List<LicenseServerInfo> list = new ArrayList<>();

    public String createLicense(LicenseParam licenseParam) {
        if (StringUtils.isEmpty(licenseParam.getLicenseName())){
            return "授权名称不能为空";
        }
        Timestamp expireDate = licenseParam.getExpireDate();
        try {
            if (expireDate.getTime() < System.currentTimeMillis()) {
                return "过期时间不能小于当前时间";
            }
        } catch (Exception e) {
            return "过期时间格式异常";
        }
        licenseParam.setIp(StringUtils.hasLength(licenseParam.getIp()) ? licenseParam.getIp() : "*");
        licenseParam.setMac(StringUtils.hasLength(licenseParam.getMac()) ? licenseParam.getMac() : "*");
        licenseParam.setCpuSerial(StringUtils.hasLength(licenseParam.getCpuSerial()) ? licenseParam.getCpuSerial() : "*");
        licenseParam.setMainBoard(StringUtils.hasLength(licenseParam.getMainBoard()) ? licenseParam.getMainBoard() : "*");

        String activeCode = LicenseCreateUtil.createActiveCode(licenseParam, licenseServerConfig.getLicensePrivateKey());
        LicenseServerInfo licenseServerInfo = new LicenseServerInfo(licenseParam, activeCode);
        list.add(licenseServerInfo);
        return activeCode;
    }

    public List<LicenseServerInfo> listLicense() {
        return list;
    }

    public RsaKeyPair generateKeyPair() {
        return RSAUtils.generateKeyPair();
    }

    public boolean verifyActiveCode(String activeCode) {
        return LicenseVerifyUtil.verify(activeCode, PUBLIC_KEY);
    }
}
