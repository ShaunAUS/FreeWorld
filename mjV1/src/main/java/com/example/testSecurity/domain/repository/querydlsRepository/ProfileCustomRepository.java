package com.example.testSecurity.domain.repository.querydlsRepository;

import com.example.testSecurity.domain.dto.profile.ProfileCreateDto;
import com.example.testSecurity.domain.dto.profile.ProfileSearchConditionDto;
import com.example.testSecurity.domain.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileCustomRepository {

    Page<Profile> search(ProfileSearchConditionDto profileSearchConditionDto, Pageable pageable);

    ProfileCreateDto findProfileByIdWithCareer(Long profileNo);
}
