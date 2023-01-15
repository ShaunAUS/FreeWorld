package com.example.testSecurity.service.impl;

import com.example.testSecurity.entity.Member;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.repository.MemberJpaRepository;
import com.example.testSecurity.repository.ProfileJpaRepository;
import com.example.testSecurity.service.MemberService;
import com.example.testSecurity.service.ProfileService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final ProfileJpaRepository profileJpaRepository;

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id);
    }

    @Override
    public boolean checkIsMyProfile(Long profileNo, Long loginMemberNo) {

        Optional<Profile> profileById = profileJpaRepository.findById(profileNo);

        if (profileById.isPresent()) {
            if (profileById.get().getMember().getNo().equals(loginMemberNo)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Member> findByUserName(String name) {
        return memberJpaRepository.findByUserName(name);
    }
}
