package com.gear.controller;

import com.gear.proto.PersonProto;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/test")
@RestController
public class TestController {


    @GetMapping(value = "/protobuf", produces = "application/x-protobuf")
    public PersonProto.Person protobuf() {
        PersonProto.Person gear = PersonProto.Person.newBuilder().setName("gear").setAddress("湖北省武汉市江夏区").setEmail("test@gmail.com").build();
        return gear;
    }

    @GetMapping(value = "/string")
    public Map<String, String> string() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "gear");
        map.put("address", "湖北省武汉市江夏区");
        map.put("email", "test@gmail.com");
        return map;
    }

}
