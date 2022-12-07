package com.example.testSecurity.controller;


import com.example.testSecurity.jwt.AuthenticationUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api("")
@RestController
@RequestMapping("/v1")
public class ProfileController {

    //TODO 등록
    @ApiOperation(value = "등록", notes = "프로필 등록")
    @PostMapping("/profile")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER')")
    public void createProfile(
        @ApiParam(value = "ProfileCreateDTO") @RequestBody Profile.Create profileCreateDTO,
        @ApiIgnore Authentication authentication
    ) {

    }

    //TODO 디테일
    @ApiOperation(value = "조회", notes = "프로필 조회")
    @GetMapping("/profile/{no}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void getProfile(
        @ApiParam(value = "ProfileCreateDTO") @RequestBody Profile.Create profileCreateDTO,
        @ApiIgnore Authentication authentication
    ) {

    }

    //TODO 수정
    @ApiOperation(value = "수정", notes = "프로필 수정")
    @PatchMapping("/profile/{no}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void modifyProfile(
        @ApiParam(value = "ProfileCreateDTO") @RequestBody Profile.Create profileCreateDTO,
        @ApiIgnore Authentication authentication
    ) {

    }

    //TODO 삭제
    @ApiOperation(value = "삭제", notes = "프로필 삭제")
    @DeleteMapping("/profile/{no}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void deleteProfile(
        @ApiParam(value = "ProfileCreateDTO") @RequestBody Profile.Create profileCreateDTO,
        @ApiIgnore Authentication authentication
    ) {

    }



    //TODO 이미지 등록


    private Integer getLoginMemberNo(Authentication authentication) {
        return AuthenticationUser.extractMemberNo(authentication);
    }
}
