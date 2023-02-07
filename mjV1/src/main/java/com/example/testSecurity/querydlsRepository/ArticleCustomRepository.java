package com.example.testSecurity.querydlsRepository;

import com.example.testSecurity.dto.article.ArticleSearchConditionDto;
import com.example.testSecurity.entity.Article;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleCustomRepository {

    Page<Article> search(ArticleSearchConditionDto searchCondition, Pageable pageable);

    List<Article> checkIsMemberArticle(Long articleNo, Long loginMemberNo);

}
