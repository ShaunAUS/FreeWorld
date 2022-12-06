package com.example.testSecurity.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class MapperUtils {

    /**
     * null 값 건너뜀 & 정확한 이름 매칭
     */
    public static ModelMapper getMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            .setSkipNullEnabled(true)
            .setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    /**
     * null 값 포함 & 정확한 이름 매칭
     */
    public static ModelMapper getMapperWithNull() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

}
