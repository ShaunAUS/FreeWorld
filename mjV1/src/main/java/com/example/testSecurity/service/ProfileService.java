package com.example.testSecurity.service;

import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.dto.ProfileDto.Info;
import com.example.testSecurity.dto.ProfileDto.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileService {

    ProfileDto.Info createProfile(ProfileDto.Create profileCreateDTO);

    ProfileDto.Info getProfile(Long no);

    ProfileDto.Info updateProfile(ProfileDto.Create profileCreateDTO, Long no);

    void deleteProfile(Long no);

    Page<Info> search(Search profileSearchConditionDto, Pageable pageable);
    
}
