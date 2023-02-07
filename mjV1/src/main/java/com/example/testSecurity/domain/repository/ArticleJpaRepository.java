package com.example.testSecurity.domain.repository;

import com.example.testSecurity.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArticleJpaRepository extends JpaRepository<Article, Long> {

}
