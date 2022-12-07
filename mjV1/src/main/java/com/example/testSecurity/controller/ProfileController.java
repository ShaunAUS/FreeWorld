package com.example.testSecurity.controller;


import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.jwt.AuthenticationUser;
import com.example.testSecurity.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api("")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    //TODO 등록
    @ApiOperation(value = "등록", notes = "프로필 등록")
    @PostMapping("/profile")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER')")
    public void createProfile(
        @ApiParam(value = "ProfileCreateDTO") @RequestBody ProfileDto.Create profileCreateDTO,
        @ApiIgnore Authentication authentication
    ) {
        profileService.createProfile(profileCreateDTO);
    }

    //TODO 디테일
    @ApiOperation(value = "조회", notes = "프로필 조회")
    @GetMapping("/profile/{no}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void getProfile(
        @PathVariable Long no,
        @ApiIgnore Authentication authentication
    ) {
        profileService.getProfile(no);
    }

    //TODO 수정
    @ApiOperation(value = "수정", notes = "프로필 수정")
    @PatchMapping("/profile/{no}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void modifyProfile(
        @ApiParam(value = "ProfileCreateDTO") @RequestBody ProfileDto.Create profileCreateDTO,
        @PathVariable Long no,
        @ApiIgnore Authentication authentication
    ) {
        profileService.updateProfile(profileCreateDTO, no);
    }

    //TODO 삭제
    @ApiOperation(value = "삭제", notes = "프로필 삭제")
    @DeleteMapping("/profile/{no}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void deleteProfile(
        @PathVariable Long no,
        @ApiIgnore Authentication authentication
    ) {
        profileService.deleteProfile(no);
    }
    //TODO 검색
    @ApiOperation(value = "삭제", notes = "프로필 삭제")
    @DeleteMapping("/profile/search")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void deleteProfile(
        @ApiParam(value = "ProfileSearchConditionDto") @RequestBody ProfileDto.SearchCondition profileSearchConditionDto,
            @ApiIgnore Authentication authentication
    ) {
        profileService.search(profileSearchConditionDto);
    }

    //TODO 이미지 등록


    private Integer getLoginMemberNo(Authentication authentication) {
        return AuthenticationUser.extractMemberNo(authentication);
    }
}
