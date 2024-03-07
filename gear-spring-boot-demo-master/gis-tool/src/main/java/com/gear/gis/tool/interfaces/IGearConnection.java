package com.gear.gis.tool.interfaces;

import com.gear.gis.tool.enums.DbType;

/**
 * 数据连接信息
 *
 * @author GuoYingdong
 * @date 2022/03/08
 */
public interface IGearConnection {
    /**
     * DB类型
     *
     * @return {@link DbType}
     */
    DbType getDbType();

    /**
     * shpfile
     *
     * @return {@link String}
     */
    String getShpFile();

    /**
     * host
     *
     * @return {@link String}
     */
    String getHost();

    /**
     * port
     *
     * @return {@link String}
     */
    String getPort();

    /**
     * 数据库
     *
     * @return {@link String}
     */
    String getDataBase();

    /**
     * 模式
     *
     * @return {@link String}
     */
    String getSchema();

    /**
     * 用户名
     *
     * @return {@link String}
     */
    String getUsername();

    /**
     * 密码
     *
     * @return {@link String}
     */
    String getPasswd();
}
