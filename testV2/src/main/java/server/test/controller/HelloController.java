package server.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api("swagger 테스트")
@RestController
public class HelloController  {

    @ApiOperation(value = "스웨거 테스트", notes = "test swagger")
    @PostMapping("/register")
    public void insertAdmin(

        @ApiParam(value = "testParameter", required = false)  String testParameter
    ) {

    }


}
