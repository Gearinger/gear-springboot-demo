package com.gear.poi.search.postgis.config;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.geotools.data.geojson.GeoJSONReader;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

/**
 * json配置
 *
 * @author guoyingdong
 * @date 2024/04/03
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
        this.registerGeometrySerializer(objectMapper);
        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    /**
     * 注册 Geometry 类型的序列化器
     *
     * @param objectMapper
     */
    private void registerGeometrySerializer(ObjectMapper objectMapper) {
        SimpleModule simpleModule = new SimpleModule();

        JsonSerializer<Point> serializer = new JsonSerializer<Point>() {
            @Override
            public void serialize(Point point, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                ObjectMapper lMapper = new ObjectMapper();
                lMapper.registerModule(new JtsModule(11));
                String geoJSON = lMapper.writeValueAsString(point);
                jsonGenerator.writeString(geoJSON);
            }

            @Override
            public Class<Point> handledType() {
                return Point.class;
            }
        };
        simpleModule.addSerializer(serializer);


        JsonDeserializer<Point> deserializer = new JsonDeserializer<>() {
            @Override
            public Point deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return (Point)GeoJSONReader.parseGeometry(jsonParser.getText());
            }
        };

        simpleModule.addDeserializer(Point.class, deserializer);
        objectMapper.registerModule(simpleModule);
    }

}