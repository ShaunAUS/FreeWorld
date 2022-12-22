package com.example.testSecurity.entity;

import com.example.testSecurity.dto.ProfileDto;
import com.example.testSecurity.utils.MapperUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_no")
    private Long no;

    @ApiModelProperty(value = "이름")
    private String name;
    @ApiModelProperty(value = "소개")
    private String introducing;
/*    @ApiModelProperty(value = "사진URL")
    private MultipartFile image;*/
    @ApiModelProperty(value = "이메일")
    private String email;
    @ApiModelProperty(value = "연락처")
    private String contactNumber;

    //멤버당 프로필 하나가능
    @OneToOne
    @JoinColumn(name = "member_no")
    private Member member;

    public static void update(ProfileDto.Create profileCreateDTO, Profile profile) {
        MapperUtils.getMapper()
                .typeMap(ProfileDto.Create.class,Profile.class)
                .map(profileCreateDTO,profile);
    }
}
