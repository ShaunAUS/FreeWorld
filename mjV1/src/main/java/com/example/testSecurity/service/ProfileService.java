package com.example.testSecurity.service;

import com.example.testSecurity.dto.ProfileDto;

public interface ProfileService {
    void createProfile(ProfileDto.Create profileCreateDTO);

    void getProfile(Long no);

    void updateProfile(ProfileDto.Create profileCreateDTO, Long no);

    void deleteProfile(Long no);

    void search(ProfileDto.SearchCondition profileSearchConditionDto);
}
