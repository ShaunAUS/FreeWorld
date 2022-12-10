package com.example.testSecurity.service.impl;

import com.example.testSecurity.entity.Member;
import com.example.testSecurity.service.MemberService;

import java.util.Optional;

public class MemberServiceImpl implements MemberService {

    @Override
    public Optional<Member> findById(Long valueOf) {
        return Optional.empty();
    }

    @Override
    public boolean checkIsMyProfile(Long profileNo, Long valueOf) {
        return false;
    }
}
