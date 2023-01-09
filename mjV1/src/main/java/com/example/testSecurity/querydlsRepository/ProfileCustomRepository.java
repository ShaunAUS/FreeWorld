package com.example.testSecurity.querydlsRepository;

import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.dto.ProfileDto.Search;
import com.example.testSecurity.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileCustomRepository {

    Page<Profile> search(Search profileSearchConditionDto, Pageable pageable);

    ProfileDto.Create findProfileByIdWithCareer(Long profileNo);
}
