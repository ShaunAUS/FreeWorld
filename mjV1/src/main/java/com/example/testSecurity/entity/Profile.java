package com.example.testSecurity.entity;

import com.example.testSecurity.dto.career.CareerInfoDto;
import com.example.testSecurity.dto.profile.ProfileInfoDto;
import com.example.testSecurity.dto.profile.ProfileUpdateDto;
import com.example.testSecurity.utils.MapperUtils;
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


    public void update(ProfileUpdateDto profileUpdateDto) {
        this.name = profileUpdateDto.getName();
        this.introduce = profileUpdateDto.getIntroduce();
        this.email = profileUpdateDto.getEmail();
        this.contactNumber = profileUpdateDto.getContactNumber();

    }

    public ProfileInfoDto toInfoDto() {
        return MapperUtils.getMapper()
            .typeMap(Profile.class, ProfileInfoDto.class)
            .addMappings(mapper -> {
                mapper.using(CAREER_LIST_TO_INFO_LIST)
                    .map(Profile::getCareers, ProfileInfoDto::setInfoCareers);
            })
            .map(this);
    }

    public static final Converter<List<Career>, List<CareerInfoDto>> CAREER_LIST_TO_INFO_LIST =
        context -> context.getSource() == null ? null : context.getSource().stream()
            .map(career -> career.toInfoDto())
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
