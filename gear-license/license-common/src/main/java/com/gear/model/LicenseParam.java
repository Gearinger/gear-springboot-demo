package com.gear.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 许可证参数
 *
 * @author guoyingdong
 * @date 2024/09/10
 */
@Data
public class LicenseParam {

    /**
     * 许可证名称
     */
    private String licenseName;
    /**
     * 到期日期（*，或者时间戳）
     */
    private Timestamp expireDate;

    /**
     * 物理地址（*，或者字符串）
     */
    private String mac;

    /**
     * ip（*，或者字符串）
     */
    private String ip;

    /**
     * cpu序列号（*，或者字符串）
     */
    private String cpuSerial;

    /**
     * 主电（*，或者字符串）
     */
    private String mainBoard;

}
