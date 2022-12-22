package com.example.testSecurity.service.impl;

import com.example.testSecurity.entity.Member;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.repository.ProfileJpaRepository;
import com.example.testSecurity.service.MemberService;
import com.example.testSecurity.service.ProfileService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ProfileJpaRepository profileJpaRepository;

    @Override
    public Optional<Member> findById(Long valueOf) {
        return Optional.empty();
    }

    @Override
    public boolean checkIsMyProfile(Long profileNo, Long loginMemberNo) {

        Optional<Profile> profileById = profileJpaRepository.findById(profileNo);

        if(profileById.isPresent()){
            if(profileById.get().getMember().getNo().equals(loginMemberNo))
                return true;
        }
        return false;
    }
}
