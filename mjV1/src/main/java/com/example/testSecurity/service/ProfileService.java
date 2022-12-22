package com.example.testSecurity.service;

import com.example.testSecurity.dto.ProfileDto;

public interface ProfileService {

    ProfileDto.Info createProfile(ProfileDto.Create profileCreateDTO);

    ProfileDto.Info getProfile(Long no);

    ProfileDto.Info updateProfile(ProfileDto.Create profileCreateDTO, Long no);

    void deleteProfile(Long no);

}
