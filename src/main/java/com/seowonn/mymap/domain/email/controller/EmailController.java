package com.seowonn.mymap.domain.email.controller;

import com.seowonn.mymap.global.dto.ApiResponse;
import com.seowonn.mymap.domain.email.dto.EmailDto;
import com.seowonn.mymap.domain.email.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.seowonn.mymap.global.type.SuccessMessage.SEND_EMAIL;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "이메일 인증 번호 전송",
            description = "회원가입 시 사용자 인증을 위해 이메일로 인증번호를 전송합니다.")
    @PostMapping("/verification")
    public ApiResponse<?> sendEmail(@Valid @RequestBody EmailDto emailDto){
        SimpleMailMessage message = emailService.sendVerificationCode(emailDto);
        return ApiResponse.createSuccessMessage(message, SEND_EMAIL);
    }
}
