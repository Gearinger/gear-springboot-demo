package com.gear.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTWriter;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static org.locationtech.jts.geom.Coordinate.NULL_ORDINATE;

/**
 * @author: wangminghui
 * @date: 2022/4/13 19:46
 * @description: geometry字段入库处理
 */
@Slf4j
@MappedJdbcTypes(value = JdbcType.JAVA_OBJECT, includeNullJdbcType = true)
@MappedTypes({Geometry.class})
public class GeometryTypeHandler extends BaseTypeHandler<Geometry> {
//    private static final WKBWriter wkbWriter = new WKBWriter(3, true);
//    private static final WKBWriter wkbWriter2D = new WKBWriter(2, true);

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Geometry geometry, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(i, toPGobject(geometry));
    }

    public static Geometry toGeometry(String hexString) {
        try {
            byte[] d = WKBReader.hexToBytes(hexString);
            return new WKBReader().read(d);
        } catch (ParseException e) {
            log.error("几何字段解析失败。{}", hexString, e);
            return null;
        }
    }

    private String toPGobject(Geometry geometry) {
        if (geometry.getSRID() == 0) {
            geometry.setSRID(4326);
        }
        int dimension = dimension(geometry);
        String wkt = new WKTWriter(dimension).write(geometry);
        return "SRID=" + geometry.getSRID() + ";" + wkt;
    }


    public static int dimension(Geometry geo) {
        if (geo.getCoordinates() != null && geo.getCoordinates().length > 0) {
            return Objects.equals(NULL_ORDINATE, geo.getCoordinates()[0].getZ()) ? 2 : 3;
        } else {
            // TODO: 2023/2/3
            log.error("无法判断geometry是否包含Z值,将保存为3维");
            return 3;
        }
    }

    @Override
    public Geometry getNullableResult(ResultSet resultSet, String s) throws SQLException {
        if (resultSet.getObject(s) == null) {
            return null;
        }
        String string = ((PGobject) resultSet.getObject(s)).getValue();
        return toGeometry(string);
    }


    @Override
    public Geometry getNullableResult(ResultSet resultSet, int i) throws SQLException {
        if (resultSet.getObject(i) == null) {
            return null;
        }
        String string = ((PGobject) resultSet.getObject(i)).getValue();
        return toGeometry(string);
    }

    @Override
    public Geometry getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        if (callableStatement.getObject(i) == null) {
            return null;
        }
        String string = ((PGobject) callableStatement.getObject(i)).getValue();
        return toGeometry(string);
    }
}

