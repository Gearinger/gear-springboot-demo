package com.gear.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.geotools.data.geojson.GeoJSONWriter;
import org.locationtech.jts.geom.Geometry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 全局序列化配置类
 *
 * @author GuoYingdong
 * @date 2023/02/02
 */
@Configuration
public class JsonConfig {

    /**
     * 创建Jackson对象映射器
     *
     * @param builder Jackson对象映射器构建器
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper getJacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        this.registerLongSerializer(objectMapper);
        this.registerGeometrySerializer(objectMapper);
        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //日期格式处理
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }

    /**
     * 注册 Long 类型的序列化器
     *
     * @param objectMapper
     */
    private void registerLongSerializer(ObjectMapper objectMapper) {
        //序列换成json时,将所有的long变成string.因为js中得数字类型不能包含所有的java long值，超过16位后会出现精度丢失
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, com.fasterxml.jackson.databind.ser.std.ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, com.fasterxml.jackson.databind.ser.std.ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
    }

    /**
     * 注册 Geometry 类型的序列化器
     *
     * @param objectMapper
     */
    private void registerGeometrySerializer(ObjectMapper objectMapper) {
        SimpleModule simpleModule = new SimpleModule();
        JsonSerializer<Geometry> serializer = new JsonSerializer<Geometry>() {
            @Override
            public void serialize(Geometry geometry, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                String geoJSON = GeoJSONWriter.toGeoJSON(geometry);
                jsonGenerator.writeString(geoJSON);
            }

            @Override
            public Class<Geometry> handledType() {
                return Geometry.class;
            }
        };
        simpleModule.addSerializer(serializer);
        objectMapper.registerModule(simpleModule);
    }
}
