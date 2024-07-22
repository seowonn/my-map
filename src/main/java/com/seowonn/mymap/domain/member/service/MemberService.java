package com.seowonn.mymap.domain.member.service;

import com.seowonn.mymap.domain.member.dto.*;
import com.seowonn.mymap.domain.member.entity.Member;
import com.seowonn.mymap.domain.member.repository.MemberRepository;
import com.seowonn.mymap.domain.member.type.Role;
import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import com.seowonn.mymap.domain.email.dto.EmailDto;
import com.seowonn.mymap.domain.email.service.EmailService;
import com.seowonn.mymap.infra.redis.service.RedisService;
import com.seowonn.mymap.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static com.seowonn.mymap.global.type.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final EmailService emailService;
  private final RedisService redisService;
  private final JwtTokenProvider jwtTokenProvider;

  private final static int PASSWORD_LENGTH = 10;
  private static final char[] CHAR_SET =
      new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
          'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
          'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
          'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
          'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&'};

  public LoginDto.LoginResponse signInMember(LoginDto.LoginRequest loginRequest) {

    // 아이디 조회
    Member member = memberRepository.findByUserId(loginRequest.getUserId())
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    // 비밀번호 조회 & 입력 pw 암호화 후 비교
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    if (!passwordEncoder.matches(loginRequest.getPassword(),
        member.getPassword())) {
      throw new MyMapSystemException(INCORRECT_PASSWORD);
    }

    // jwt 토큰 생성
    List<String> roleList = new ArrayList<>();
    roleList.add(member.getRole().name());

    String accessToken =
        jwtTokenProvider.createAccessToken(loginRequest.getUserId(), roleList);

    return LoginDto.LoginResponse.builder()
        .accessToken(accessToken)
        .build();
  }

  public SignUpDto.SignUpResponse createMember(SignUpDto.SignUpRequest signUpRequest, Role role) {

    // 이미 등록된 아이디(이메일)인지 확인
    if (memberRepository.existsByUserId(signUpRequest.getUserId())) {
      throw new MyMapSystemException(USERID_EXISTS);
    }

    // redis code & 인증 번호 검증
    checkVerificationCode(signUpRequest.getUserId(),
            signUpRequest.getVerificationNum());

    Member member = Member.ofMemberFormAndRole(signUpRequest, role);
    memberRepository.save(member);
    return SignUpDto.SignUpResponse.from(member);
  }

  public void checkVerificationCode(String email, String verificationCode) {
    String redisCode = redisService.getVerificationData(email);

    // 다른 아이디(이메일) 값 입력 / 인증 번호 유효 시간 만료
    if (redisCode == null) {
      throw new MyMapSystemException(INCORRECT_REDIS_CODE);
    }

    // 인증 번호가 다른 사용자에 대한 에러 처리
    if (!redisCode.equals(verificationCode)) {
      throw new MyMapSystemException(INCORRECT_REDIS_CODE);
    }

  }

  public SimpleMailMessage sendNewPassword(EmailDto emailDto) {

    // 회원 검사
    Member member = memberRepository.findByUserId(emailDto.getEmailAddress())
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    // 임시 비밀번호 생성 및 이메일 전송
    String randomPassword = createRandomPassword();
    SimpleMailMessage message = emailService.sendAuthEmail(
        emailDto.getEmailAddress(), randomPassword);

    // DB 저장
    String encPassword = BCrypt.hashpw(randomPassword, BCrypt.gensalt());
    member.setPassword(encPassword);
    memberRepository.save(member);

    return message;
  }

  private String createRandomPassword() {

    StringBuilder sb = new StringBuilder();

    SecureRandom random = new SecureRandom();
    int idx = 0;
    int len = CHAR_SET.length;
    for (int i = 0; i < MemberService.PASSWORD_LENGTH; i++) {
      idx = random.nextInt(len);
      sb.append(CHAR_SET[idx]);
    }
    log.info("[createRandomPassword] : 임시 번호 생성 완료");

    return sb.toString();
  }

}
