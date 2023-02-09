package com.example.testSecurity.service.service.impl;


import com.example.testSecurity.domain.dto.career.CareerUpdateDto;
import com.example.testSecurity.domain.dto.profile.ProfileCreateDto;
import com.example.testSecurity.domain.dto.profile.ProfileInfoDto;
import com.example.testSecurity.domain.dto.profile.ProfileSearchConditionDto;
import com.example.testSecurity.domain.dto.profile.ProfileUpdateDto;
import com.example.testSecurity.domain.entity.Career;
import com.example.testSecurity.domain.entity.Member;
import com.example.testSecurity.domain.entity.Profile;
import com.example.testSecurity.service.exception.ServiceProcessException;
import com.example.testSecurity.service.exception.enums.ServiceMessage;
import com.example.testSecurity.domain.repository.querydlsRepository.ProfileCustomRepository;
import com.example.testSecurity.domain.repository.ProfileJpaRepository;
import com.example.testSecurity.service.service.ProfileService;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {


    private final ProfileJpaRepository profileJpaRepository;
    private final ProfileCustomRepository profileCustomRepository;

    @Override
    @Transactional
    public ProfileInfoDto createProfile(ProfileCreateDto profileCreateDTO, Member loginMember) {

        Profile createProfile = Profile.of(profileCreateDTO);
        createProfile.changeMember(loginMember);

        List<Career> careers = createProfile.getCareers();
        for (Career career : careers) {
            career.changeProfile(createProfile);
        }
        //Profile + Career save
        Profile savedProfile = profileJpaRepository.save(createProfile);

        log.info("============savedProfile============");
        log.info("savedProfile : {}", savedProfile);
        log.info("============savedProfile============");

        return ProfileInfoDto.of(savedProfile);

    }

    @Override
    public ProfileInfoDto getProfile(Long profileNo) {
        return ProfileInfoDto.of(profileByNo(profileNo));
    }


    @Override
    @Transactional
    public ProfileInfoDto updateProfile(ProfileUpdateDto profileUpdateDto, Long profileNo) {

        Profile profileById = profileByNo(profileNo);

        log.info("============before update profile============");
        log.info("profileById : {}", profileById);
        log.info("============before update profile============");

        //Only profile update
        profileById.update(profileUpdateDto);

        //Only Career update
        List<CareerUpdateDto> updateCareers = profileUpdateDto.getUpdateCareers();
        List<Career> originCareers = profileById.getCareers();
        for (int i = 0; i < updateCareers.size(); i++) {
            originCareers.get(i).updateCareer(updateCareers.get(i));

        }

        log.info("============after update profile============");
        log.info("profileById : {}", profileById);
        log.info("============after update profile============");

        return ProfileInfoDto.of(profileById);

    }

    @Override
    public void deleteProfile(Long profileNo) {
        profileJpaRepository.deleteById(profileNo);
    }

    //profile + career
    @Override
    public Page<ProfileInfoDto> search(ProfileSearchConditionDto profileSearchConditionDto,
        Pageable pageable) {
        return profileCustomRepository.search(profileSearchConditionDto, pageable)
            .map(ProfileInfoDto::of);
    }

    private Profile profileByNo(Long profileNo) {
        return profileJpaRepository.findById(profileNo)
            .orElseThrow(() -> new ServiceProcessException(ServiceMessage.NOT_FOUND_PROFILE));
    }

}

