package com.example.testSecurity.service.impl;

import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.entity.Profile;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.repository.ProfileJpaRepository;
import com.example.testSecurity.service.ProfileService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {


    private final ProfileJpaRepository profileJpaRepository;

    @Override
    public ProfileDto.Info createProfile(ProfileDto.Create profileCreateDTO) {
        Profile saveedProfile = profileJpaRepository.save(
            ProfileDto.Create.toEntity(profileCreateDTO));
        return ProfileDto.Info.toDto(saveedProfile);
    }

    @Override
    public ProfileDto.Info getProfile(Long profileNo) {

        Optional<Profile> getProfileById = profileJpaRepository.findById(profileNo);

        if (getProfileById.isPresent()) {
            return ProfileDto.Info.toDto(getProfileById.get());
        } else {
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND_PROFILE);
        }
    }

    @Override
    public ProfileDto.Info updateProfile(ProfileDto.Create profileCreateDTO, Long profileNo) {

        Optional<Profile> profileById = profileJpaRepository.findById(profileNo);

        if (profileById.isPresent()) {
            //TODO dirtychecking
            Profile profile = profileById.get();
            Profile.update(profileCreateDTO, profile);
            return ProfileDto.Info.toDto(profile);
        } else {
            throw new ServiceProcessException(ServiceMessage.NOT_FOUND_PROFILE);
        }

    }

    @Override
    public void deleteProfile(Long profileNo) {
        //TODO member 연관관계 테이블 같이 삭제? check / fk profile에 있어서 상관 없을듯
        profileJpaRepository.deleteById(profileNo);
    }

}

