package com.example.testSecurity.querydlsRepository;

import com.example.testSecurity.dto.ArticleDto;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleCustomRepository {

    Page<ArticleDto.Info> search(ArticleDto.Search articleSearchConditionDto, Pageable pageable);

    void likeArticle(Long articleNo);



}
