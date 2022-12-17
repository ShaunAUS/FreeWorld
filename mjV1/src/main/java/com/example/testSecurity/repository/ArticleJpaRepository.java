package com.example.testSecurity.repository;

import com.example.testSecurity.entity.Article;
import com.example.testSecurity.entity.MemberArticleBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleJpaRepository extends JpaRepository<Article,Long> {

    //artilce bookmark @Query사용해야하는가?
    @Query(value = "insert into MemberArticleBookmark  ('member','article') values (:loginMemberNo,:articleNo)",nativeQuery = true)
    MemberArticleBookmark addArticleBookMark(Long loginMemberNo,Long articleNo);
}
