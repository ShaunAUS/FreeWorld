package com.example.testSecurity.service;

import com.example.testSecurity.dto.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ArticleService {

    ArticleDto.Info createArticle(ArticleDto.Create articleCreateDTO);

    ArticleDto.Info getArticle(Long articleNo);

    ArticleDto.Info updateArticle(ArticleDto.Create articleCreateDTO, Long no);

    void deleteArticle(Long articleNo);

    void bookmarkArticle(Long articleNo, Integer loginMemberNo);


    Boolean checkIsMemberArticle(Long articleNo, Integer loginMemberNo);

}
