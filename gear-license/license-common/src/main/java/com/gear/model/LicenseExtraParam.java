package com.gear.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>自定义需要校验的License参数</p>
 *
 * @author appleyk
 * @version v1.0.0
 * @blob https://blog.csdn.net/appleyk
 * @date created on  10:42 下午 2020/8/21
 */
@Data
public class LicenseExtraParam implements Serializable {

    private static final long serialVersionUID = 8600137500316662317L;

    /**
     * 可被允许的IP地址
     */
    private List<String> ipAddress;

    /**
     * 可被允许的mac地址
     */
    private List<String> macAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;

}
