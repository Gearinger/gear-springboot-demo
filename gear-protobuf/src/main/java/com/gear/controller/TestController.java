package com.gear.controller;

import com.gear.proto.PersonProto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/test")
@RestController
public class TestController {


    @GetMapping(value = "/protobuf", produces = "application/x-protobuf")
    public PersonProto.Person protobuf() {
        PersonProto.Person gear = PersonProto.Person.newBuilder().setName("gear").build();
        return gear;
    }

}
