package com.example.testSecurity.controller;

import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.dto.profile.ProfileCreateDto;
import com.example.testSecurity.dto.profile.ProfileInfoDto;
import com.example.testSecurity.dto.profile.ProfileSearchConditionDto;
import com.example.testSecurity.dto.profile.ProfileUpdateDto;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.service.MemberService;
import com.example.testSecurity.service.ProfileImageService;
import com.example.testSecurity.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Api("")
@Controller
@RequestMapping("/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final MemberService memberService;
    private final ProfileImageService profileImageService;


    //등록은 일반회원만 가능
    @ApiOperation(value = "등록", notes = "프로필 등록")
    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER')")
    public ProfileInfoDto createProfile(
        @ApiParam(value = "ProfileCreateDTO") @RequestBody ProfileCreateDto profileCreateDTO,
        @ApiIgnore Authentication authentication
    ) {
        return profileService.createProfile(profileCreateDTO, getLoginMember(authentication));
    }


    @ApiOperation(value = "조회", notes = "프로필 조회")
    @GetMapping("/{profileNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public String getProfile(
        @PathVariable Long profileNo,
        Model model
    ) {
        ProfileInfoDto profile = profileService.getProfile(profileNo);
        model.addAttribute("profileDetail", profile);

        return "profileDetail";
    }


    @ApiOperation(value = "수정", notes = "프로필 수정")
    @PatchMapping("/{profileNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public ProfileInfoDto modifyProfile(
        @ApiParam(value = "ProfileCreateDTO") @RequestBody ProfileUpdateDto profileUpdateDTO,
        @PathVariable Long profileNo,
        @ApiIgnore Authentication authentication
    ) {
        checkIsMyProfile(profileNo, getLoginMember(authentication).getNo());
        return profileService.updateProfile(profileUpdateDTO, profileNo);
    }


    @ApiOperation(value = "삭제", notes = "프로필 삭제")
    @DeleteMapping("/{profileNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void deleteProfile(
        @PathVariable Long profileNo,
        @ApiIgnore Authentication authentication
    ) {
        checkIsMyProfile(profileNo, getLoginMember(authentication).getNo());
        profileService.deleteProfile(profileNo);
    }

    //랜딩 페이지 검색창
    @ApiOperation(value = "검색", notes = "프로필 검색")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public String searchProfile(
        @ApiParam(value = "카테고리") @RequestParam CategoryType category, //from career table
        @ApiParam(value = "경력") @RequestParam Integer year, Model model,
        @PageableDefault(sort = {
            "profile_no"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        ProfileSearchConditionDto profileSearchConditionDto = ProfileSearchConditionDto.builder()
            .categoryType(category)
            .experienceYear(year)
            .build();
        Page<ProfileInfoDto> search = profileService.search(profileSearchConditionDto, pageable);

        model.addAttribute("searchResult", search);
        return "profile";
    }


    @ApiOperation(value = "등록", notes = "이미지 등록")
    @PostMapping("/{profileNo}/image")
    @PreAuthorize("hasAnyRole('ADMIN','GENERAL_MEMBER')")
    public void registerProfileImage(
        @RequestParam List<MultipartFile> imageList,
        @PathVariable Long profileNo
    ) {
        if (imageList == null || imageList.size() == 0) {
            throw new ServiceProcessException(ServiceMessage.IMAGE_LOAD_ERROR);
        } else {
            profileImageService.registerImage(imageList, profileNo);
        }
    }

    private void checkIsMyProfile(Long profileNo, Long loginMemberNo) {
        if (!memberService.checkIsMyProfile(profileNo, loginMemberNo)) {
            throw new ServiceProcessException(ServiceMessage.NOT_AUTHORIZED);
        }
    }

    private Member getLoginMember(Authentication authentication) {

        Optional<Member> findByUserName = memberService.findByUserName(getUserName(authentication));
        if (findByUserName.isPresent()) {
            return findByUserName.get();
        } else {
            throw new ServiceProcessException(ServiceMessage.USER_NOT_FOUND);
        }
    }

    private String getUserName(Authentication authentication) {
        final int i = authentication.getName().lastIndexOf(":");
        final String username =
            i > -1 ? authentication.getName().substring(0, i) : authentication.getName();
        return username;
    }
}
