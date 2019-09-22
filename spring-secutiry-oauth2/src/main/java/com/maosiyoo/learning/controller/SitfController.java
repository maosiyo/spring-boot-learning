package com.maosiyoo.learning.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("Swagger2 与 oauth2 整合的测试接口")
@RestController
@RequestMapping("/sitf")
public class SitfController {

    @ApiOperation("测试接口")
    @GetMapping("/{name}")
    public String hello(@PathVariable("name") String name) {
        return "Hello ".concat(name).concat("!");
    }

}
