package com.gear.common;

import org.springframework.util.StringUtils;

/**
 * @author baofuen
 * @version v1.0
 * @create 2024-07-15 11:18
 * @project gis-basis
 * @description IpUtils-
 **/
public class IpUtils {


    /**
     * 判断IP是否在指定子网内
     * String realIp = "192.168.1.10";
     * String passIp = "192.168.*.*";
     *
     * @param realIp
     * @param passIp
     * @return
     */
    public static boolean isIpInSubnet(String realIp, String passIp) {
        try {
            // 将 passIp 转换为有效的网络地址
            // 假设未指定的部分使用默认的 /8, /16 或 /24 掩码
            String subnetInfo = passIp.replace("*", "0");
            int wildcardCount = passIp.length() - passIp.replace("*", "").length();
            int cidrMask = 32 - (wildcardCount * 8);
            SubnetUtils subnetUtils = new SubnetUtils(subnetInfo + "/" + cidrMask);
            SubnetUtils.SubnetInfo subnetInfoObj = subnetUtils.getInfo();
            // 判断IP是否在网络内
            return subnetInfoObj.isInRange(realIp);
        } catch (Exception e) {
            return false;
        }
    }
}
