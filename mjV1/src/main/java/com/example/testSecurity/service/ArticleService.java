package com.example.testSecurity.service;

import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.dto.ArticleDto.Search;
import com.example.testSecurity.entity.Article;
import com.example.testSecurity.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ArticleService {

    ArticleDto.Info createArticle(ArticleDto.Create articleCreateDTO, Long loginMemberNo);

    ArticleDto.Info getArticle(Long articleNo);

    ArticleDto.Info updateArticle(ArticleDto.Create articleCreateDTO, Long no);

    void deleteArticle(Long articleNo);

    void bookmarkArticle(Long articleNo, Member loginMember);


    Boolean checkIsMemberArticle(Long articleNo, Long loginMemberNo);

    ArticleDto.Info likeArticle(Long articleNo);

    Page<ArticleDto.Info> search(Search searchCondition, Pageable pageable);
}
