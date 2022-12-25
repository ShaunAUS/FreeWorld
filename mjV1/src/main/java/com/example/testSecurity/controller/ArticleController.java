package com.example.testSecurity.controller;

import com.example.testSecurity.Enum.RoleType;
import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.jwt.AuthenticationUser;
import com.example.testSecurity.querydlsRepository.ArticleCustomRepository;
import com.example.testSecurity.service.ArticleService;
import com.example.testSecurity.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;

    private final ArticleCustomRepository articleCustomRepository;


    //회사멤버는 글을 올리지 못하고 헤드헌팅만 가능하다.
    @ApiOperation(value = "등록", notes = "개시글 등록")
    @GetMapping("/hello")
    public String hello(
        @ApiIgnore Authentication authentication
    ) {
        return "hello";
    }

    //회사멤버는 글을 올리지 못하고 헤드헌팅만 가능하다.
    @ApiOperation(value = "등록", notes = "개시글 등록")
    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void createArticle(
        @ApiParam(value = "ArticleCreateDto") @RequestBody ArticleDto.Create articleCreateDto,
        @ApiIgnore Authentication authentication
    ) {
        isAuthorizedMember(authentication);
        articleService.createArticle(articleCreateDto);
    }


    @ApiOperation(value = "조회", notes = "개시글 조회")
    @GetMapping("/{no}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN','COMPANY_MEMBER')")
    public void getArticle(
        @PathVariable Long no,
        @ApiIgnore Authentication authentication
    ) {
        isAuthorizedMember(authentication);
        articleService.getArticle(no);
    }

    //TODO 수정
    @ApiOperation(value = "수정", notes = "개시글 수정")
    @PatchMapping("/{articleNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void modifyArticle(
        @ApiParam(value = "ArticleCreateDto") @RequestBody ArticleDto.Create articleCreateDto,
        @PathVariable Long articleNo,
        @ApiIgnore Authentication authentication
    ) {
        if (checkIsMemberArticle(articleNo, getLoginMemberNo(authentication))) {
            articleService.updateArticle(articleCreateDto, articleNo);
        }
        throw new ServiceProcessException(ServiceMessage.NOT_AUTHORIZED);
    }


    @ApiOperation(value = "삭제", notes = "게시글 삭제")
    @DeleteMapping("/{articleNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void deleteProfile(
        @PathVariable Long articleNo,
        @ApiIgnore Authentication authentication
    ) {
        if (checkIsMemberArticle(articleNo, getLoginMemberNo(authentication))) {
            articleService.deleteArticle(articleNo);
        }
        throw new ServiceProcessException(ServiceMessage.NOT_AUTHORIZED);
    }

    @ApiOperation(value = "북마크", notes = "게시글 북마크")
    @PostMapping("/{articleNo}/bookmark")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN','COMPANY_MEMBER')")
    public void bookmarkArticle(
        @PathVariable Long articleNo,
        @ApiIgnore Authentication authentication
    ) {
        isAuthorizedMember(authentication);
        articleService.bookmarkArticle(articleNo, getLoginMemberNo(authentication));
    }

    @ApiOperation(value = "좋아요", notes = "게시글 좋아요")
    @PostMapping("/{articleNo}/like")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN','COMPANY_MEMBER')")
    public void likeArticle(
        @PathVariable Long articleNo,
        @ApiIgnore Authentication authentication
    ) {
        isAuthorizedMember(authentication);
        articleCustomRepository.likeArticle(articleNo);
    }

    @ApiOperation(value = "검색", notes = "게시글 검색")
    @DeleteMapping("/search")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN','COMPANY_MEMBER')")
    public Page<ArticleDto.Info> searchArticle(
        @RequestBody ArticleDto.Search searchCondition,
        @PageableDefault(sort = {
            "artilce_no"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable,
        @ApiIgnore Authentication authentication
    ) {
        return articleCustomRepository.search(searchCondition, pageable);
    }


    private Boolean checkIsMemberArticle(Long articleNo, Integer loginMemberNo) {
        return articleService.checkIsMemberArticle(articleNo, loginMemberNo);
    }

    private void isAuthorizedMember(Authentication authentication) {
        switch (RoleType.valueOf(getLoginMember(getLoginMemberNo(authentication)).getRoleType())) {
            case ADMIN:
            case GENERAL_MEMBER:
                break;
            default:
                throw new ServiceProcessException(ServiceMessage.NOT_AUTHORIZED);
        }
    }

    private Integer getLoginMemberNo(Authentication authentication) {
        return AuthenticationUser.extractMemberNo(authentication);
    }

    private Member getLoginMember(Integer memberNo) {
        return memberService.findById(Long.valueOf(memberNo))
            .orElseThrow(() -> new ServiceProcessException(ServiceMessage.USER_NOT_FOUND));
    }
}
