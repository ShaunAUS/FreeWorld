package com.example.testSecurity.service;

import com.example.testSecurity.domain.enums.CategoryType;
import com.example.testSecurity.domain.enums.RoleType;
import com.example.testSecurity.domain.dto.career.CareerCreateDto;
import com.example.testSecurity.domain.dto.career.CareerUpdateDto;
import com.example.testSecurity.domain.dto.member.MemberCreateDto;
import com.example.testSecurity.domain.dto.profile.ProfileCreateDto;
import com.example.testSecurity.domain.dto.profile.ProfileInfoDto;
import com.example.testSecurity.domain.dto.profile.ProfileSearchConditionDto;
import com.example.testSecurity.domain.dto.profile.ProfileUpdateDto;
import com.example.testSecurity.domain.entity.Member;
import com.example.testSecurity.domain.entity.Profile;
import com.example.testSecurity.domain.repository.MemberJpaRepository;
import com.example.testSecurity.domain.repository.ProfileJpaRepository;
import com.example.testSecurity.service.service.ProfileService;
import com.example.testSecurity.service.service.auth.AuthService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static com.example.testSecurity.domain.enums.CategoryType.ANNOUNCE;
import static com.example.testSecurity.domain.enums.CategoryType.PROGRAMMING;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProfileServiceTest {

    @Autowired
    private ProfileJpaRepository profileJpaRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @BeforeEach
    private void addMember() {

        MemberCreateDto member = MemberCreateDto.builder()
            .userName("testUserName")
            .password("12345")
            .roleType(RoleType.ADMIN)
            .build();
        authService.createMember(member);

    }


    @BeforeEach
    private void addProfile() {

        List<CareerCreateDto> careerList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CareerCreateDto careerCreateDto = CareerCreateDto.builder()
                .companyName("company" + i)
                .assignedTask("assignedTask" + i)
                .description("description" + i)
                .startPeriod(20200613)
                .finishPeriod(20200615)
                .category(PROGRAMMING)
                //.categoryDetail()
                .build();
            careerList.add(careerCreateDto);
        }

        ProfileCreateDto create = ProfileCreateDto.builder()
            .name("test")
            .introduce("test introduce")
            .email("test email")
            .contactNumber("010-1234-5678")
            .experienceYear(3)
            .careers(careerList)
            .build();

        Member member = memberJpaRepository.findAll().get(0);
        profileService.createProfile(create, member);
    }

    @AfterEach
    private void deleteAll() {
        profileJpaRepository.deleteAll();
        memberJpaRepository.deleteAll();
    }

    @Test
    @Transactional()
    void createProfile() {

        //given
        //when
        Profile profile = profileJpaRepository.findAll().get(0);

        //then
        assertThat(profileJpaRepository.findAll().size()).isEqualTo(1);
        assertThat(profile.getName()).isEqualTo("test");
        assertThat(profile.getIntroduce()).isEqualTo("test introduce");
        assertThat(profile.getCareers().get(0).getCompanyName()).isEqualTo("company" + 0);
    }

    @Test
    @Transactional
    void getProfile() {

        //given
        //when
        Profile profile = profileJpaRepository.findAll().get(0);
        ProfileInfoDto profileInfo = profileService.getProfile(profile.getNo());

        //then
        assertThat(profileInfo.getName()).isEqualTo("test");
        assertThat(profileInfo.getEmail()).isEqualTo("test email");
        assertThat(profileInfo.getInfoCareers().get(0).getCompanyName()).isEqualTo("company" + 0);

    }

    @Test
    @Transactional
    void updateProfile() {

        //given
        List<CareerUpdateDto> updateCareerList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CareerUpdateDto careerCreateDto = CareerUpdateDto.builder()
                .companyName("update company" + i)
                .assignedTask("update assignedTask" + i)
                .description("update description" + i)
                .startPeriod(20200613)
                .finishPeriod(20200615)
                .category(CategoryType.ANNOUNCE)
                //.categoryDetail()
                .build();
            updateCareerList.add(careerCreateDto);
        }

        ProfileUpdateDto update = ProfileUpdateDto.builder()
            .name("update test")
            .introduce("update test introduce")
            .email("update test email")
            .experienceYear(3)
            .contactNumber("update number")
            .updateCareers(updateCareerList)
            .build();

        //given
        Profile profile = profileJpaRepository.findAll().get(0); // 저장된 Profile 호출
        ProfileInfoDto info = profileService.updateProfile(update, profile.getNo());

        //then
        assertThat(profile.getName()).isEqualTo("update test");
        assertThat(profile.getIntroduce()).isEqualTo("update test introduce");
        assertThat(profile.getCareers().get(0).getCompanyName()).isEqualTo("update company" + 0);
        assertThat(profile.getCareers().get(0).getAssignedTask()).isEqualTo(
            "update assignedTask" + 0);


    }

    @Test
    void deleteProfile() {
        Profile profile = profileJpaRepository.findAll().get(0);
        profileService.deleteProfile(profile.getNo());

        assertThat(profileJpaRepository.findAll().size()).isEqualTo(0);
        assertThat(profileJpaRepository.findById(profile.getNo())).isEqualTo(Optional.empty());

    }

    @Test
    @Transactional()
    void search() {
        //given
        List<CareerCreateDto> careerList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CareerCreateDto careerCreateDto = CareerCreateDto.builder()
                .companyName("different company" + i)
                .assignedTask("different assignedTask" + i)
                .description("different description" + i)
                .startPeriod(20200613)
                .finishPeriod(20200615)
                .category(ANNOUNCE)
                //.categoryDetail()
                .build();
            careerList.add(careerCreateDto);
        }

        ProfileCreateDto create = ProfileCreateDto.builder()
            .name("different test")
            .introduce("different test introduce")
            .email("different test email")
            .contactNumber("010-1234-5678")
            .experienceYear(4)
            .careers(careerList)
            .build();

        //다른 멤버
        MemberCreateDto member = MemberCreateDto.builder()
            .userName("different test userName")
            .password("12345")
            .roleType(RoleType.ADMIN)
            .build();
        authService.createMember(member);
        Member secondMember = memberJpaRepository.findAll().get(1);
        profileService.createProfile(create, secondMember);

        ProfileSearchConditionDto firstSearchCondition = new ProfileSearchConditionDto(null, null,
            3);
        ProfileSearchConditionDto secondSearchCondition = new ProfileSearchConditionDto(ANNOUNCE,
            null, null);

        Sort sort = Sort.by("profile_no").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);

        //when
        Page<ProfileInfoDto> firstSearchResult = profileService.search(firstSearchCondition,
            pageable);
        Page<ProfileInfoDto> secondSearchResult = profileService.search(secondSearchCondition,
            pageable);

        //then
        assertThat(firstSearchResult.getTotalElements()).isEqualTo(1);
        assertThat(firstSearchResult.getContent().get(0).getName()).isEqualTo("test");
        assertThat(secondSearchResult.getTotalElements()).isEqualTo(1);
        assertThat(secondSearchResult.getContent().get(0).getName()).isEqualTo("different test");
    }
}