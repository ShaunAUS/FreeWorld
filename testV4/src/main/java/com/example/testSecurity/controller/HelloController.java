package com.example.testSecurity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("swagger 테스트")
@RestController
@RequestMapping("/v1")
public class HelloController  {

    @ApiOperation(value = "스웨거 테스트", notes = "test swagger")
    @PostMapping("/register")
    public void insertAdmin(

        @ApiParam(value = "testParameter", required = false)  String testParameter
    ) {

    }


}
