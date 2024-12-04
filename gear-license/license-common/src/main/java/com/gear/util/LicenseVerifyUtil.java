package com.gear.util;

import com.gear.common.Base64Utils;
import com.gear.common.IpUtils;
import com.gear.common.LicenseException;
import com.gear.common.RSAUtils;
import com.gear.model.AServerInfos;
import com.gear.model.LicenseExtraParam;
import com.gear.model.LicenseParam;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static com.gear.constant.Constant.CURRENT_TIME;
import static com.gear.constant.Constant.SPLIT_STR;

/**
 * 许可证验证util
 *
 * @author guoyingdong
 * @date 2024/09/11
 */
public class LicenseVerifyUtil {

    public static boolean verify(String encryptedData, String publicKey) {
        try {
            LicenseParam licenseParam = parseLicense(encryptedData, publicKey);
            LicenseExtraParam serverInfos = AServerInfos.getServer("").getServerInfos();
            validateTime(licenseParam.getExpireDate());
            validateParams(serverInfos.getMacAddress(), licenseParam.getMac(), "License verify failed: Invalid macAddress.");
            validateParams(Collections.singletonList(serverInfos.getMainBoardSerial()), licenseParam.getMainBoard(), "License verify failed: Invalid mainBoardSerial.");
            validateIps(serverInfos.getIpAddress(), licenseParam.getIp());
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    private static boolean validateTime(Timestamp timeStr) {
        long time = timeStr.getTime();
        long currentTime = System.currentTimeMillis();
        if (time >= currentTime && time >= CURRENT_TIME && currentTime >= CURRENT_TIME) {
            return true;
        }
        throw new LicenseException("License verify failed: Invalid expireDate.");
    }

    public static LicenseParam parseLicense(String encryptedData, String publicKey) throws Exception {
        String result = getLicenseContent(encryptedData, publicKey);
        String[] split = result.split(SPLIT_STR);
        LicenseParam licenseParam = new LicenseParam();
        licenseParam.setExpireDate(Timestamp.valueOf(split[0]));
        licenseParam.setMac(split[1]);
        licenseParam.setCpuSerial(split[2]);
        licenseParam.setMainBoard(split[3]);
        licenseParam.setIp(split[4]);
        return licenseParam;
    }

    /**
     * 获取当前证书的内容
     *
     * @param encryptedData
     * @param publicKey
     * @return
     */
    public static String getLicenseContent(String encryptedData, String publicKey) throws Exception {
        byte[] decode = Base64Utils.decodeToBytes(encryptedData);
        byte[] decodedData = RSAUtils.decryptByPublicKey(decode, publicKey);
        return new String(Base64Utils.decode(new String(decodedData)));
    }

    private static void validateParams(List<String> serverInfos, String params, String message) {
        if (serverInfos == null || serverInfos.isEmpty() || !StringUtils.hasLength(params) || params.equals("*")) {
            return;
        }
        String[] split = params.split(",");
        for (String param : split) {
            for (String serverInfo : serverInfos) {
                if (serverInfo.contains(param)) {
                    return;
                }
            }
        }
        throw new LicenseException(message);
    }

    private static void validateIps(List<String> idAddresss, String ips) {
        if (idAddresss == null || idAddresss.isEmpty() || !StringUtils.hasLength(ips) || ips == "*") {
            return;
        }
        String[] checkIps = ips.split(",");
        boolean isIpInSubnet;
        for (String checkIp : checkIps) {
            for (String ipAddress : idAddresss) {
                isIpInSubnet = IpUtils.isIpInSubnet(ipAddress, checkIp);
                if (isIpInSubnet) {
                    return;
                }
            }
        }
        throw new LicenseException("License verify failed: Invalid ipAddress.");
    }

}
