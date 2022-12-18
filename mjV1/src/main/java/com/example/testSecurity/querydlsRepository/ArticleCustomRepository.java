package com.example.testSecurity.querydlsRepository;

import com.example.testSecurity.dto.ArticleDto;
import com.example.testSecurity.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ArticleCustomRepository {

    Page<ArticleDto.Info> search(ArticleDto.Search articleSearchConditionDto, Pageable pageable);

    void likeArticle(Long articleNo);

    Optional<Article> checkIsMemberArticle(Long articleNo, Integer loginMemberNo);

}
