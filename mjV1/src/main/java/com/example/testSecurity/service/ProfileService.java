package com.example.testSecurity.service;

import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.dto.ProfileDto.Info;
import com.example.testSecurity.dto.ProfileDto.Search;
import com.example.testSecurity.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService {

    ProfileDto.Info createProfile(ProfileDto.Create profileCreateDTO, Member loginMember);

    ProfileDto.Info getProfile(Long no);

    ProfileDto.Info updateProfile(ProfileDto.Update profileUpdateDTO, Long no);

    void deleteProfile(Long no);

    Page<Info> search(Search profileSearchConditionDto, Pageable pageable);

}
