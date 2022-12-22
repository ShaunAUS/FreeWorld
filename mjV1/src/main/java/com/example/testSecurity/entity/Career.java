package com.example.testSecurity.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_no")
    private Long no;
    @ApiModelProperty(value = "회사이름")
    private String companyName;
    @ApiModelProperty(value = "담당업무")
    private String assignedTask;
    @ApiModelProperty(value = "설명")
    private String description;
    @ApiModelProperty(value = "년차")
    private Integer year;
    @ApiModelProperty(value = "카테고리")
    private Integer category;
    @ApiModelProperty(value = "카테고리 상세")
    private Integer categoryDetail;


    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "profile_no")
    private Profile profile;

}
