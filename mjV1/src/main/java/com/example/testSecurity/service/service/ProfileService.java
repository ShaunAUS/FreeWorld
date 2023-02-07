package com.example.testSecurity.service.service;

import com.example.testSecurity.domain.dto.profile.ProfileCreateDto;
import com.example.testSecurity.domain.dto.profile.ProfileInfoDto;
import com.example.testSecurity.domain.dto.profile.ProfileSearchConditionDto;
import com.example.testSecurity.domain.dto.profile.ProfileUpdateDto;
import com.example.testSecurity.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService {

    ProfileInfoDto createProfile(ProfileCreateDto profileCreateDTO, Member loginMember);

    ProfileInfoDto getProfile(Long no);

    ProfileInfoDto updateProfile(ProfileUpdateDto profileUpdateDTO, Long no);

    void deleteProfile(Long no);

    Page<ProfileInfoDto> search(ProfileSearchConditionDto profileSearchConditionDto,
        Pageable pageable);

}
