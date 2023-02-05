package com.example.testSecurity.service.impl;


import com.example.testSecurity.dto.CareerDto;
import com.example.testSecurity.dto.CareerDto.Create;
import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.dto.ProfileDto.Info;
import com.example.testSecurity.dto.ProfileDto.Search;
import com.example.testSecurity.entity.Career;
import com.example.testSecurity.entity.Member;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.querydlsRepository.CareerCustomRepository;
import com.example.testSecurity.querydlsRepository.ProfileCustomRepository;
import com.example.testSecurity.repository.CareerJpaRepository;
import com.example.testSecurity.repository.ProfileJpaRepository;
import com.example.testSecurity.service.ProfileService;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
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
    public ProfileDto.Info createProfile(ProfileDto.Create profileCreateDTO, Member loginMember) {

        Profile createProfile = profileCreateDTO.toEntity();
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

        return savedProfile.toInfoDto();

    }

    @Override
    public ProfileDto.Info getProfile(Long profileNo) {
        return profileByNo(profileNo).toInfoDto();
    }


    @Override
    @Transactional
    public ProfileDto.Info updateProfile(ProfileDto.Update profileUpdateDto, Long profileNo) {

        Profile profileById = profileByNo(profileNo);

        log.info("============before update profile============");
        log.info("profileById : {}", profileById);
        log.info("============before update profile============");

        //Only profile update
        profileById.update(profileUpdateDto);

        //Only Career update
        List<CareerDto.Update> updateCareers = profileUpdateDto.getCareer();
        List<Career> originCareers = profileById.getCareers();
        for (int i = 0; i < updateCareers.size(); i++) {
            originCareers.get(i).updateCareer(updateCareers.get(i));

        }

        log.info("============after update profile============");
        log.info("profileById : {}", profileById);
        log.info("============after update profile============");

        return profileById.toInfoDto();

    }

    @Override
    public void deleteProfile(Long profileNo) {
        profileJpaRepository.deleteById(profileNo);
    }

    //profile + career
    @Override
    public Page<Info> search(Search profileSearchConditionDto, Pageable pageable) {
        return profileCustomRepository.search(profileSearchConditionDto, pageable)
            .map(Info::toInfoDto);
    }

    private Profile profileByNo(Long profileNo) {
        return profileJpaRepository.findById(profileNo)
            .orElseThrow(() -> new ServiceProcessException(ServiceMessage.NOT_FOUND_PROFILE));
    }

}

