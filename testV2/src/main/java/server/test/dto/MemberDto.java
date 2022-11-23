package server.test.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberDto {

    @Getter
    @Builder
    @ApiModel(value = "MemberDto.Info", description = "기본정보")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String name;
        private Integer age;
        private String createDate;
        private String updateDate;
    }


}
