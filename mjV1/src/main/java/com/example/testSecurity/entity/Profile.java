package com.example.testSecurity.entity;

import com.example.testSecurity.dto.CareerDto;
import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.dto.ProfileDto.Info;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModelProperty;
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
@ToString(exclude = "careers")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_no")
    private Long no;

    @ApiModelProperty(value = "이름")
    private String name;
    @ApiModelProperty(value = "소개")
    private String introduce;

    @ApiModelProperty(value = "이메일")
    private String email;
    @ApiModelProperty(value = "연락처")
    private String contactNumber;


    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Career> careers;

    @OneToMany(mappedBy = "profile")
    private List<ProfileImage> profileImages;

    @OneToOne
    @JoinColumn(name = "member_no")
    private Member member;


    public void update(ProfileDto.Create profileCreateDto) {
        this.name = profileCreateDto.getName();
        this.introduce = profileCreateDto.getIntroduce();
        this.email = profileCreateDto.getEmail();
        this.contactNumber = profileCreateDto.getContactNumber();

    }

    public Info toInfoDto() {
        return MapperUtils.getMapper()
            .typeMap(Profile.class, ProfileDto.Info.class)
            .addMappings(mapper -> {
                mapper.using(CAREER_LIST_TO_INFO_LIST)
                    .map(Profile::getCareers, ProfileDto.Info::setCareers);
            })
            .map(this);
    }

    public static final Converter<List<Career>, List<CareerDto.Info>> CAREER_LIST_TO_INFO_LIST =
        context -> context.getSource() == null ? null : context.getSource().stream()
            .map(career -> career.toInfoDto())
            .collect(Collectors.toList());

}
