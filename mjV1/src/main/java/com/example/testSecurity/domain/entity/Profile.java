package com.example.testSecurity.domain.entity;

import static com.example.testSecurity.domain.dto.profile.ProfileCreateDto.CAREER_LIST_CONVERTER;

import com.example.testSecurity.domain.dto.career.CareerInfoDto;
import com.example.testSecurity.domain.dto.profile.ProfileCreateDto;
import com.example.testSecurity.domain.dto.profile.ProfileInfoDto;
import com.example.testSecurity.domain.dto.profile.ProfileUpdateDto;
import com.example.testSecurity.domain.utils.MapperUtils;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.Converter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = "careers") //to prevent stack overflow
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_no")
    private Long no;
    @ApiModelProperty(value = "이름")
    @Column(nullable = false)
    private String name;
    @ApiModelProperty(value = "소개")
    @Column(nullable = false)
    private String introduce;
    @ApiModelProperty(value = "년차")
    private Integer experienceYear;
    @ApiModelProperty(value = "이메일")
    @Column(nullable = false)
    private String email;
    @ApiModelProperty(value = "연락처")
    @Column(nullable = false)
    private String contactNumber;
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Career> careers;
    @OneToMany(mappedBy = "profile")
    @Column(nullable = false)
    private List<ProfileImage> profileImages;
    @OneToOne
    @JoinColumn(name = "member_no")
    private Member member;

    public static Profile of(ProfileCreateDto profileCreateDto) {
        return MapperUtils.getMapper()
            .typeMap(ProfileCreateDto.class, Profile.class)
            .addMappings(mapper -> {
                mapper.using(CAREER_LIST_CONVERTER)   //List<CareerDto.Create> -> List<Career>
                    .map(ProfileCreateDto::getCareers, Profile::setCareers);
            })
            .map(profileCreateDto);
    }


    public void update(ProfileUpdateDto profileUpdateDto) {
        this.name = profileUpdateDto.getName();
        this.introduce = profileUpdateDto.getIntroduce();
        this.email = profileUpdateDto.getEmail();
        this.contactNumber = profileUpdateDto.getContactNumber();

    }


    public static final Converter<List<Career>, List<CareerInfoDto>> CAREER_LIST_TO_INFO_LIST =
        context -> context.getSource() == null ? null : context.getSource().stream()
            .map(CareerInfoDto::of)
            .collect(Collectors.toList());

    public void changeMember(Member loginMember) {
        this.member = loginMember;
    }

    @PrePersist
    public void prePersist() {
        this.experienceYear = this.experienceYear == null ? 0 : this.experienceYear;
        this.careers = this.careers == null ? Collections.emptyList() : this.careers;
    }
}
