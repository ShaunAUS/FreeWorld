package com.example.testSecurity.querydlsRepository;

import com.example.testSecurity.dto.ProfileDto;

public interface ProfileCustomRepository {
    ProfileDto.Info search(ProfileDto.Search profileSearchConditionDto);
}
