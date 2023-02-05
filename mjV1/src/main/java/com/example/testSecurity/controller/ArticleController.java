package com.example.testSecurity.controller;

import com.example.testSecurity.Enum.CategoryType;
import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.dto.ArticleDto.Info;
import com.example.testSecurity.dto.ArticleDto.Search;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.service.ArticleService;
import com.example.testSecurity.service.MemberService;
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
    private final MemberService memberService;


    @ApiOperation(value = "등록", notes = "게시글 등록")
    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public ArticleDto.Info createArticle(
        @ApiParam(value = "ArticleCreateDto") @RequestBody ArticleDto.Create articleCreateDto,
        @ApiIgnore Authentication authentication
    ) {
        return articleService.createArticle(articleCreateDto, getLoginMemberNo(authentication));
    }


    @ApiOperation(value = "조회", notes = "게시글 조회")
    @GetMapping("/{articleNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public String getArticle(
        @PathVariable Long articleNo,
        Model model
    ) {
        ArticleDto.Info articleInfo = articleService.getArticle(articleNo);
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
        Page<Info> articles = articleService.getArticles(pageable);
        model.addAttribute("allArticles", articles);
        return "articleAll";
    }


    @ApiOperation(value = "수정", notes = "게시글 수정")
    @PatchMapping("/{articleNo}")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public ArticleDto.Info modifyArticle(
        @ApiParam(value = "ArticleCreateDto") @RequestBody ArticleDto.Update articleUpdateDto,
        @PathVariable Long articleNo,
        @ApiIgnore Authentication authentication
    ) {
        if (checkIsMemberArticle(articleNo, getLoginMemberNo(authentication))) {
            return articleService.updateArticle(articleUpdateDto, articleNo);
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
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public void bookmarkArticle(
        @PathVariable Long articleNo,
        @ApiIgnore Authentication authentication
    ) {
        articleService.bookmarkArticle(articleNo, getLoginMember(getLoginMemberNo(authentication)));
    }

    @ApiOperation(value = "좋아요", notes = "게시글 좋아요")
    @PatchMapping("/{articleNo}/like")
    @PreAuthorize("hasAnyRole('GENERAL_MEMBER','ADMIN')")
    public ArticleDto.Info likeArticle(
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
        ArticleDto.Search searchCondition = Search.builder()
            .keyword(keyword)
            .category(categoryType)
            .build();
        Page<Info> search = articleService.search(searchCondition, pageable);
        model.addAttribute("searchResult", search);

        return "articleSearch";
    }


    private Boolean checkIsMemberArticle(Long articleNo, Long loginMemberNo) {
        return articleService.checkIsMemberArticle(articleNo, loginMemberNo);
    }


    private Member getLoginMember(Long memberNo) {
        return memberService.findById(memberNo)
            .orElseThrow(() -> new ServiceProcessException(ServiceMessage.USER_NOT_FOUND));
    }

    private Long getLoginMemberNo(Authentication authentication) {

        Optional<Member> findByUserName = memberService.findByUserName(getUserName(authentication));
        if (findByUserName.isPresent()) {
            return findByUserName.get().getNo();
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
