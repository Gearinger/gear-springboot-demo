package com.gear.gis.tool;

import com.gear.gis.tool.enums.DbType;
import lombok.Data;

/**
 * 数据连接
 *
 * @author GuoYingdong
 * @date 2022/03/08
 */
@Data
public class GearConnection {
    DbType dbType;
    String shpFile;
    String host;
    String port;
    String dataBase;
    String schema;
    String username;
    String passwd;
    String charset;

    /**
     * 构建连接信息
     *
     * @param dbType  db类型
     * @param shpFile shp文件
     */
    public GearConnection(DbType dbType, String shpFile) {
        this(dbType, shpFile, "UTF-8");
    }

    /**
     * 构建连接信息
     *
     * @param dbType  db类型
     * @param shpFile shp文件
     * @param charset 字符集
     */
    public GearConnection(DbType dbType, String shpFile, String charset) {
        this.dbType = dbType;
        this.shpFile = shpFile;
        this.charset = charset;
    }

    /**
     * 构建连接信息
     *
     * @param dbType   db型
     * @param host     主机ip
     * @param port     端口
     * @param dataBase 数据库
     * @param username 用户名
     * @param passwd   passwd
     */
    public GearConnection(DbType dbType, String host, String port, String dataBase, String username, String passwd) {
        this.dbType = dbType;
        this.host = host;
        this.port = port;
        this.dataBase = dataBase;
        this.username = username;
        this.passwd = passwd;
    }

    /**
     * 构建连接信息
     *
     * @param dbType   db型
     * @param host     主机ip
     * @param port     端口
     * @param dataBase 数据库
     * @param schema   模式
     * @param username 用户名
     * @param passwd   passwd
     */
    public GearConnection(DbType dbType, String host, String port, String dataBase, String schema, String username, String passwd) {
        this.dbType = dbType;
        this.host = host;
        this.port = port;
        this.dataBase = dataBase;
        this.schema = schema;
        this.username = username;
        this.passwd = passwd;
    }
}
