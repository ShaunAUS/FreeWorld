package com.example.testSecurity.repository;

import com.example.testSecurity.entity.Article;
import com.example.testSecurity.entity.MemberArticleBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


public interface ArticleJpaRepository extends JpaRepository<Article, Long> {

}
