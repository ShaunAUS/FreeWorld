package com.example.testSecurity.service.service;

import com.example.testSecurity.domain.entity.Member;

import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long valueOf);

    boolean checkIsMyProfile(Long profileNo, Long loginMemberNo);

    Optional<Member> findByUserName(String name);
}
