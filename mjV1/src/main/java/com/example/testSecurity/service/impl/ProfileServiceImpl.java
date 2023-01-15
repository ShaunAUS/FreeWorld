package com.example.testSecurity.service.impl;


import com.example.testSecurity.dto.CareerDto;
import com.example.testSecurity.dto.CareerDto.Create;
import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.dto.ProfileDto.Info;
import com.example.testSecurity.dto.ProfileDto.Search;
import com.example.testSecurity.entity.Career;
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
    private final CareerJpaRepository careerJpaRepository;
    private final ProfileCustomRepository profileCustomRepository;
    private final CareerCustomRepository careerCustomRepository;

    @Override
    @Transactional
    public ProfileDto.Info createProfile(ProfileDto.Create profileCreateDTO) {

        Profile profile = profileCreateDTO.toEntity();

        List<Career> careers = profile.getCareers();
        for (Career career : careers) {
            career.setProfile(profile);
        }
        //Profile + Career save
        Profile savedProfile = profileJpaRepository.save(profile);

        log.info("============savedProfile============");
        log.info("savedProfile : {}", savedProfile);
        log.info("============savedProfile============");

        return savedProfile.toInfoDto();

    }

    @Override
    public ProfileDto.Info getProfile(Long profileNo) {

        Optional<Profile> profileById = profileJpaRepository.findById(profileNo);
        if (profileById.isPresent()) {
            return profileById.get().toInfoDto();
        } else {
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND_PROFILE);
        }
    }

    //FIXME should i update like this?
    @Override
    @Transactional
    public ProfileDto.Info updateProfile(ProfileDto.Create profileCreateDto, Long profileNo) {

        Profile profileById = profileJpaRepository.findById(profileNo)
            .orElseThrow(() -> new ServiceProcessException(ServiceMessage.NOT_FOUND_PROFILE));

        log.info("============before update profile============");
        log.info("profileById : {}", profileById);
        log.info("============before update profile============");

        //Only profile update
        profileById.update(profileCreateDto);

        //Only Career update
        List<Create> updateCareers = profileCreateDto.getCareers();
        List<Career> originCareers = profileById.getCareers();
        for (int i = 0; updateCareers.size() > i; i++) {
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

    //porifle + career
    @Override
    public Page<Info> search(Search profileSearchConditionDto, Pageable pageable) {
        Page<Info> profileBySearch = profileCustomRepository.search(profileSearchConditionDto,
                pageable)
            .map(Info::toInfoDto);
        return profileBySearch;
    }

}

