package com.example.testSecurity.domain.repository;

import com.example.testSecurity.domain.entity.MemberArticleBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberArticleBookmarkJpaRepository extends
    JpaRepository<MemberArticleBookmark, Long> {


}

