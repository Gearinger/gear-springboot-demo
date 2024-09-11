package com.gear.util;

import com.gear.common.Base64Utils;
import com.gear.common.RSAUtils;
import com.gear.model.LicenseParam;
import com.gear.model.RsaKeyPair;

import java.security.KeyPairGenerator;

import static com.gear.constant.Constant.SPLIT_STR;

public class LicenseCreateUtil {

    public static String createActiveCode(LicenseParam licenseParam, String privateKey) {
        String activeParamInfo = new StringBuilder()
                .append(licenseParam.getExpireDate())
                .append(SPLIT_STR)
                .append(licenseParam.getMac())
                .append(SPLIT_STR)
                .append(licenseParam.getCpuSerial())
                .append(SPLIT_STR)
                .append(licenseParam.getMainBoard())
                .append(SPLIT_STR)
                .append(licenseParam.getIp())
                .toString().trim();
        try {
            String activeInfo = Base64Utils.encode(activeParamInfo);
            byte[] bytes = RSAUtils.encryptByPrivateKey(activeInfo.getBytes(), privateKey);
            return Base64Utils.encodeFromBytes(bytes).replaceAll("(\\r\\n|\\r|\\n)", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
