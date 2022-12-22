package com.example.testSecurity.querydlsRepository;

import com.example.testSecurity.dto.ProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileCustomRepository {

    Page<ProfileDto.Info> search(ProfileDto.Search profileSearchConditionDto, Pageable pageable);
}
