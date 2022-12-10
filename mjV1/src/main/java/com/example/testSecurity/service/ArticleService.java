package com.example.testSecurity.service;

import com.example.testSecurity.dto.ArticleDto;

public interface ArticleService {
    void createArticle(ArticleDto.Create articleCreateDTO);

    void getArticle(Long no);

    void updateArticle(ArticleDto.Create articleCreateDTO, Long no);

    void deleteArticle(Long no);

    void search(ArticleDto.Search articleSearchConditionDto);

    void likeArticle(Long no);

    void bookmarkArticle(Long no, Integer loginMemberNo);

    Boolean checkIsMemberArticle(Long no, Integer loginMemberNo);

}
