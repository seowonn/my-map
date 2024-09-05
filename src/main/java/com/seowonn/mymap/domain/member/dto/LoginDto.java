package com.seowonn.mymap.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class LoginDto {

    @AllArgsConstructor
    @Getter
    public static class LoginRequest {

        @NotBlank(message = "사용자 아이디를 입력해주세요.")
        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,6}$",
            message = "이메일 주소 양식을 확인해주세요")
        private String userId;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class LoginResponse {
        private String accessToken;
    }
}
