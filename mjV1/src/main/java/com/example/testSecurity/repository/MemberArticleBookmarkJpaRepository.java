package com.example.testSecurity.repository;

import com.example.testSecurity.entity.MemberArticleBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberArticleBookmarkJpaRepository extends
    JpaRepository<MemberArticleBookmark, Long> {


}

