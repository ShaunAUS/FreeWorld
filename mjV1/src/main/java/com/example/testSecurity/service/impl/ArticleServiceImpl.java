package com.example.testSecurity.service.impl;

import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.service.ArticleService;

public class ArticleServiceImpl implements ArticleService {
    @Override
    public void createArticle(ArticleDto.Create articleCreateDTO) {

    }

    @Override
    public void getArticle(Long no) {

    }

    @Override
    public void updateArticle(ArticleDto.Create articleCreateDTO, Long no) {

    }

    @Override
    public void deleteArticle(Long no) {

    }

    @Override
    public void search(ArticleDto.Search articleSearchConditionDto) {

    }

    @Override
    public void likeArticle(Long no) {

    }

    @Override
    public void bookmarkArticle(Long no, Integer loginMemberNo) {

    }

    @Override
    public Boolean checkIsMemberArticle(Long no, Integer loginMemberNo) {
        return null;
    }
}
