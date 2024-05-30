package com.seowonn.mymap.domain.member.dto;

import com.seowonn.mymap.domain.member.entity.Member;
import com.seowonn.mymap.domain.member.type.Gender;
import com.seowonn.mymap.domain.member.type.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class SignUpDto {

    @Getter
    @AllArgsConstructor
    public static class SignUpRequest {

        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$",
                message = "이메일 주소 양식을 확인해주세요")
        @NotBlank(message = "반드시 내용을 입력해야 합니다.")
        private String userId;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @NotBlank(message = "전화번호는 필수입니다.")
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
                message = "휴대폰 번호 양식에 맞지 않습니다.")
        private String phone;

        @NotBlank(message = "활동명을 입력해야 합니다.")
        private String username;

        @NotBlank(message = "반드시 내용을 입력해야 합니다.")
        @Pattern(regexp = "male|female", message = "gender는 'male' 또는 'female' 이어야 합니다.")
        private String gender;

        @NotBlank(message = "등록할 아이디로 받은 인증번호를 입력해주세요.")
        private String verificationNum;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SignUpResponse {

        private Role role;
        private String userId;
        private String phone;
        private String userName;
        private Gender gender;

        public static SignUpResponse from(Member member){
            return SignUpResponse.builder()
                    .role(member.getRole())
                    .userId(member.getUserId())
                    .phone(member.getPhone())
                    .userName(member.getUserName())
                    .gender(member.getGender())
                    .build();
        }

    }
}
