package com.example.testSecurity.Enum;


import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;

import java.util.Arrays;

//큰 범주 분류
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CategoryType {

    PROGRAMMING("PROGRAMMING", 0),
    ANNOUNCE("ANNOUNCE", 1),
    CHEF("CHEF", 2);


    private String name;
    private Integer number;

    public static final Converter<CategoryType, Integer> CATEGORY_TYPE_INTEGER_CONVERTER =
        context -> context.getSource() == null ? null : context.getSource().getNumber();

    public static final Converter<Integer, CategoryType> INTEGER_CATEGORY_TYPE_CONVERTER =
        context -> context.getSource() == null ? null : CategoryType.valueOf(context.getSource());

    public static CategoryType valueOf(Integer i) {
        return Arrays.stream(CategoryType.values())
            .filter(v -> v.getNumber().intValue() == i.intValue())
            .findFirst()
            .orElseGet(null);
    }

    public static Integer toInteger(CategoryType categoryType) {
        return Arrays.stream(CategoryType.values())
            .filter(v -> Objects.equals(v.getName(), categoryType.getName()))
            .findFirst()
            .map(CategoryType::getNumber)
            .orElseGet(null);
    }
}
