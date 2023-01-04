package com.example.testSecurity.controller;

import com.example.testSecurity.Enum.RoleType;
import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.entity.Article;
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
    @ApiOperation(value = "등록", notes = "게시글 등록")
    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public ArticleDto.Info createArticle(
        @ApiParam(value = "ArticleCreateDto") @RequestBody ArticleDto.Create articleCreateDto,
        @ApiIgnore Authentication authentication
    ) {
        isAuthorizedMember(authentication);
        Integer loginMemberNo = getLoginMemberNo(authentication);
        return articleService.createArticle(articleCreateDto, loginMemberNo);
    }


    @ApiOperation(value = "조회", notes = "게시글 조회")
    @GetMapping("/{articleNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN','COMPANY_MEMBER')")
    public ArticleDto.Info getArticle(
        @PathVariable Long articleNo,
        @ApiIgnore Authentication authentication
    ) {
        isAuthorizedMember(authentication);
        return articleService.getArticle(articleNo);
    }


    @ApiOperation(value = "수정", notes = "게시글 수정")
    @PatchMapping("/{articleNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public ArticleDto.Info modifyArticle(
        @ApiParam(value = "ArticleCreateDto") @RequestBody ArticleDto.Create articleCreateDto,
        @PathVariable Long articleNo,
        @ApiIgnore Authentication authentication
    ) {
        if (checkIsMemberArticle(articleNo, getLoginMemberNo(authentication))) {
            return articleService.updateArticle(articleCreateDto, articleNo);
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
        articleService.bookmarkArticle(articleNo, getLoginMember(getLoginMemberNo(authentication)));
    }

    @ApiOperation(value = "좋아요", notes = "게시글 좋아요")
    @PatchMapping("/{articleNo}/like")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN','COMPANY_MEMBER')")
    public ArticleDto.Info likeArticle(
        @PathVariable Long articleNo,
        @ApiIgnore Authentication authentication
    ) {
        isAuthorizedMember(authentication);
        return articleService.likeArticle(articleNo);
    }

    @ApiOperation(value = "검색", notes = "게시글 검색")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN','COMPANY_MEMBER')")
    public Page<ArticleDto.Info> searchArticle(
        @RequestBody ArticleDto.Search searchCondition,
        @PageableDefault(sort = {
            "article_no"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable,
        @ApiIgnore Authentication authentication
    ) {
        return articleService.search(searchCondition, pageable);
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
