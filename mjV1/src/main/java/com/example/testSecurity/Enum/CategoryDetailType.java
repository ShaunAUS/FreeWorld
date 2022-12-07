package com.example.testSecurity.Enum;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CategoryDetailType {

    PROGRAMMING("LOW",0),
    ANNOUNCE("MIDDLE",1),
    CHEF("HIGH",2);


    private String name;
    private Integer number;


}
