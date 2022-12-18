package com.example.testSecurity.controller;

import com.example.testSecurity.Enum.RoleType;
import com.example.testSecurity.dto.CompanyDto;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.jwt.AuthenticationUser;
import com.example.testSecurity.service.CompanyService;
import com.example.testSecurity.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final MemberService memberService;
    private final CompanyService companyService;

    @ApiOperation(value = "등록", notes = "회사 등록")
    @PostMapping("")
    @PreAuthorize("hasAnyRole('COMPANY_MEMBER')")
    public void createProfile(
            @ApiParam(value = "CompanyCreateDto") @RequestBody CompanyDto.Create companyCreateDto,
            @ApiIgnore Authentication authentication
    ) throws ParseException {
        isAuthorizedMember(authentication);
        companyService.createCompany(companyCreateDto);
    }


    @ApiOperation(value = "조회", notes = "회사 조회")
    @GetMapping("/{companyNo}")
    @PreAuthorize("hasAnyRole('COMPANY_MEMBER','ADMIN')")
    public void getProfile(
            @PathVariable Long companyNo,
            @ApiIgnore Authentication authentication
    ) {
        isAuthorizedMember(authentication);
        companyService.getCompany(companyNo);
    }


    @ApiOperation(value = "수정", notes = "회사 수정")
    @PatchMapping("/{no}")
    @PreAuthorize("hasAnyRole('COMPANY_MEMBER','ADMIN')")
    public void modifyProfile(
            @ApiParam(value = "CompanyCreateDto") @RequestBody CompanyDto.Create companyCreateDto,
            @PathVariable Long companyNo,
            @ApiIgnore Authentication authentication
    ) {
        isAuthorizedMember(authentication);
        companyService.updateCompany(companyCreateDto, companyNo);
    }


    @ApiOperation(value = "삭제", notes = "회사 삭제")
    @DeleteMapping("/{companyNo}")
    @PreAuthorize("hasAnyRole('COMPANY_MEMBER','ADMIN')")
    public void deleteProfile(
            @PathVariable Long companyNo,
            @ApiIgnore Authentication authentication
    ) {
        isAuthorizedMember(authentication);
        companyService.deleteCompany(companyNo);
    }


    private Integer getLoginMemberNo(Authentication authentication) {
        return AuthenticationUser.extractMemberNo(authentication);
    }
    private Member getLoginMember(Integer memberNo){
        return memberService.findById(Long.valueOf(memberNo))
                .orElseThrow(() -> new ServiceProcessException(ServiceMessage.USER_NOT_FOUND));
    }

    private void isAuthorizedMember(Authentication authentication) {
        switch (RoleType.valueOf(getLoginMember(getLoginMemberNo(authentication)).getRoleType())) {
            case ADMIN:
            case COMPANY_MEMBER:
                break;
            default:
                throw new ServiceProcessException(ServiceMessage.NOT_AUTHORIZED);
        }
    }
}
