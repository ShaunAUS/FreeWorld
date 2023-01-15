package com.example.testSecurity.querydlsRepository;

import com.example.testSecurity.dto.ArticleDto.Search;
import com.example.testSecurity.entity.Article;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ArticleCustomRepository {

    Page<Article> search(Search articleSearchConditionDto, Pageable pageable);

    List<Article> checkIsMemberArticle(Long articleNo, Long loginMemberNo);

}
