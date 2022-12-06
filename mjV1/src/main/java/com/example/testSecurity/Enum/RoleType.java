package com.example.testSecurity.Enum;


import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.Converter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RoleType {

    LOW("LOW",0),
    MIDDLE("MIDDLE",1),
    HIGH("HIGH",2);

    private String name;
    private Integer number;

    public static final Converter<RoleType, Integer> ROLE_TYPE_INTEGER_CONVERTER =
        context -> context.getSource() == null ? null : context.getSource().getNumber();

    public static final Converter<Integer, RoleType> INTEGER_ROLE_TYPE_CONVERTER =
        context -> context.getSource() == null ? null : RoleType.valueOf(context.getSource());

    //Enum 일치하는값
    public static RoleType valueOf(Integer i) {
        return Arrays.stream(RoleType.values())
            .filter(v -> v.getNumber().intValue() == i.intValue())
            .findFirst()
            .orElseGet(null);
    }
}
