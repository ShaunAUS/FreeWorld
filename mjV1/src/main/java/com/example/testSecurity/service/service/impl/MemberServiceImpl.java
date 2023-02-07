package com.example.testSecurity.service.service.impl;

import com.example.testSecurity.domain.entity.Member;
import com.example.testSecurity.domain.entity.Profile;
import com.example.testSecurity.domain.repository.MemberJpaRepository;
import com.example.testSecurity.domain.repository.ProfileJpaRepository;
import com.example.testSecurity.service.service.MemberService;
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
