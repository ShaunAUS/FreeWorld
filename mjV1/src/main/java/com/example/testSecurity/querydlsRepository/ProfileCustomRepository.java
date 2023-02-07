package com.example.testSecurity.querydlsRepository;

import com.example.testSecurity.dto.profile.ProfileCreateDto;
import com.example.testSecurity.dto.profile.ProfileSearchConditionDto;
import com.example.testSecurity.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileCustomRepository {

    Page<Profile> search(ProfileSearchConditionDto profileSearchConditionDto, Pageable pageable);

    ProfileCreateDto findProfileByIdWithCareer(Long profileNo);
}
