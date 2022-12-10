package com.example.testSecurity.Enum;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;

//큰 범주 분류
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CategoryType {

    PROGRAMMING("PROGRAMMING",0),
    ANNOUNCE("ANNOUNCE",1),
    CHEF("CHEF",2);


    private String name;
    private Integer number;

    public static final Converter<RoleType, Integer> CATEGORY_TYPE_INTEGER_CONVERTER =
        context -> context.getSource() == null ? null : context.getSource().getNumber();

    public static final Converter<Integer, RoleType> INTEGER_CATEGORY_TYPE_CONVERTER =
        context -> context.getSource() == null ? null : RoleType.valueOf(context.getSource());
}
