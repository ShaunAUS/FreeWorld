package com.example.testSecurity.server.controller;

import com.example.testSecurity.domain.enums.CategoryType;
import com.example.testSecurity.domain.dto.article.ArticleCreateDto;
import com.example.testSecurity.domain.dto.article.ArticleInfoDto;
import com.example.testSecurity.domain.dto.article.ArticleSearchConditionDto;
import com.example.testSecurity.domain.dto.article.ArticleUpdateDto;
import com.example.testSecurity.domain.entity.Member;
import com.example.testSecurity.service.aop.LoginMember;
import com.example.testSecurity.service.exception.ServiceProcessException;
import com.example.testSecurity.service.exception.enums.ServiceMessage;
import com.example.testSecurity.service.service.ArticleService;
import com.example.testSecurity.service.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @ApiOperation(value = "등록", notes = "게시글 등록")
    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public ArticleInfoDto createArticle(
        @ApiParam(value = "ArticleCreateDto") @RequestBody ArticleCreateDto articleCreateDto,
        @LoginMember Member logimMember
    ) {
        return articleService.createArticle(articleCreateDto, logimMember.getNo());
    }

    @ApiOperation(value = "조회", notes = "게시글 조회")
    @GetMapping("/{articleNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public String getArticle(
        @PathVariable Long articleNo,
        Model model
    ) {
        ArticleInfoDto articleInfo = articleService.getArticle(articleNo);
        model.addAttribute("articleInfo", articleInfo);
        return "articleDetail";
    }

    //index
    @ApiOperation(value = "조회", notes = "게시글 전체조회")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public String getAllArticle(
        @PageableDefault(sort = {
            "no"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable, Model model
    ) {
        Page<ArticleInfoDto> articles = articleService.getArticles(pageable);
        model.addAttribute("allArticles", articles);
        return "articleAll";
    }


    @ApiOperation(value = "수정", notes = "게시글 수정")
    @PatchMapping("/{articleNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public ArticleInfoDto modifyArticle(
        @ApiParam(value = "ArticleCreateDto") @RequestBody ArticleUpdateDto articleUpdateDto,
        @PathVariable Long articleNo,
        @LoginMember Member logimMember

    ) {
        if (checkIsMemberArticle(articleNo, logimMember.getNo())) {
            return articleService.updateArticle(articleUpdateDto, articleNo);
        }
        throw new ServiceProcessException(ServiceMessage.NOT_AUTHORIZED);

    }


    @ApiOperation(value = "삭제", notes = "게시글 삭제")
    @DeleteMapping("/{articleNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void deleteProfile(
        @PathVariable Long articleNo,
        @LoginMember Member logimMember
    ) {
        if (checkIsMemberArticle(articleNo, logimMember.getNo())) {
            articleService.deleteArticle(articleNo);
        }
        throw new ServiceProcessException(ServiceMessage.NOT_AUTHORIZED);
    }

    @ApiOperation(value = "북마크", notes = "게시글 북마크")
    @PostMapping("/{articleNo}/bookmark")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void bookmarkArticle(
        @PathVariable Long articleNo,
        @LoginMember Member logimMember
    ) {
        articleService.bookmarkArticle(articleNo, logimMember);
    }

    @ApiOperation(value = "좋아요", notes = "게시글 좋아요")
    @PatchMapping("/{articleNo}/like")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public ArticleInfoDto likeArticle(
        @PathVariable Long articleNo
    ) {
        return articleService.likeArticle(articleNo);
    }


    //제목+내용 , 카테고리로 검색
    @ApiOperation(value = "검색", notes = "게시글 검색")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public String searchArticle(
        @ApiParam(value = "키워드") @RequestParam String keyword,
        @ApiParam(value = "카테고리") @RequestParam CategoryType categoryType,
        @PageableDefault(sort = {
            "article_no"}, direction = Sort.Direction.DESC, size = 10) Pageable pageable,
        Model model

    ) {
        ArticleSearchConditionDto searchCondition = ArticleSearchConditionDto.builder()
            .keyword(keyword)
            .category(categoryType)
            .build();
        Page<ArticleInfoDto> search = articleService.search(searchCondition, pageable);
        model.addAttribute("searchResult", search);

        return "articleSearch";
    }


    private Boolean checkIsMemberArticle(Long articleNo, Long loginMemberNo) {
        return articleService.checkIsMemberArticle(articleNo, loginMemberNo);
    }
}
