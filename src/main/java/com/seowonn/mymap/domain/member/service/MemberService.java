package com.seowonn.mymap.domain.member.service;

import static com.seowonn.mymap.global.type.ErrorCode.EXPIRED_VERIFICATION;
import static com.seowonn.mymap.global.type.ErrorCode.INCORRECT_CODE;
import static com.seowonn.mymap.global.type.ErrorCode.INCORRECT_EMAIL;
import static com.seowonn.mymap.global.type.ErrorCode.INCORRECT_PASSWORD;
import static com.seowonn.mymap.global.type.ErrorCode.USERID_EXISTS;
import static com.seowonn.mymap.global.type.ErrorCode.USER_NOT_FOUND;
import static com.seowonn.mymap.global.type.TimeSettings.VERIFICATION_EXPIRE_TIME;

import com.seowonn.mymap.security.jwt.JwtTokenProvider;
import com.seowonn.mymap.domain.member.dto.MemberFormDto;
import com.seowonn.mymap.domain.member.dto.MemberResponse;
import com.seowonn.mymap.domain.member.dto.SignInForm;
import com.seowonn.mymap.domain.member.dto.SignInResponse;
import com.seowonn.mymap.domain.member.entity.Member;
import com.seowonn.mymap.domain.member.repository.MemberRepository;
import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import com.seowonn.mymap.infra.email.dto.EmailDto;
import com.seowonn.mymap.infra.redis.service.RedisService;
import com.seowonn.mymap.infra.email.service.MailService;
import com.seowonn.mymap.domain.member.type.Role;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final MailService mailService;
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

  public SimpleMailMessage sendVerificationCode(EmailDto emailDto) {

    // 인증 번호 생성
    String verificationNum = createNumber();

    // redis에 인증 번호 저장
    redisService.setEmailValidationExpire(
        emailDto.getEmailAddress(), verificationNum, VERIFICATION_EXPIRE_TIME.getTime());

    return mailService.sendAuthEmail(emailDto.getEmailAddress(),
        verificationNum);
  }

  public SignInResponse signInMember(SignInForm signInForm) {

    // 아이디 조회
    Member member = memberRepository.findByUserId(signInForm.getUserId())
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    // 비밀번호 조회 & 입력 pw 암호화 후 비교
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    if (!passwordEncoder.matches(signInForm.getPassword(),
        member.getPassword())) {
      throw new MyMapSystemException(INCORRECT_PASSWORD);
    }

    // jwt 토큰 생성
    List<String> roleList = new ArrayList<>();
    roleList.add(member.getRole().name());

    String accessToken =
        jwtTokenProvider.createAccessToken(signInForm.getUserId(), roleList);

    return SignInResponse.builder()
        .accessToken(accessToken)
        .build();
  }

  private String createNumber() {

    SecureRandom random = new SecureRandom();
    int code = random.nextInt(900000) + 100000;
    log.info("[createNumber] : 인증 번호 생성 완료");

    return String.valueOf(code);
  }

  public MemberResponse createMember(MemberFormDto memberFormDto, Role role) {

    // 이미 등록된 아이디(이메일)인지 확인
    if (memberRepository.existsByUserId(memberFormDto.getUserId())) {
      throw new MyMapSystemException(USERID_EXISTS);
    }

    // redis code & 인증 번호 검증
    checkVerificationCode(memberFormDto.getUserId(),
        memberFormDto.getVerificationNum());

    Member member = Member.ofMemberFormAndRole(memberFormDto, role);
    memberRepository.save(member);
    return MemberResponse.from(member);
  }

  public void checkVerificationCode(String email, String verificationCode) {
    String redisCode = redisService.getVerificationData(email);

    // 다른 아이디(이메일) 값을 입력하여 redis code가 null일 경우 에러 처리
    if (redisCode == null) {
      throw new MyMapSystemException(INCORRECT_EMAIL);
    }

    // 만료된 인증 번호에 대한 에러 처리
    long remainingTime = redisService.getRemainingExpireTime(email);
    if (remainingTime <= 0) {
      throw new MyMapSystemException(EXPIRED_VERIFICATION);
    }

    // 인증 번호가 다른 사용자에 대한 에러 처리
    if (!redisCode.equals(verificationCode)) {
      throw new MyMapSystemException(INCORRECT_CODE);
    }

    redisService.deleteVerificationData(email);
  }

  public SimpleMailMessage sendNewPassword(EmailDto emailDto) {

    // 회원 검사
    Member member = memberRepository.findByUserId(emailDto.getEmailAddress())
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    // 임시 비밀번호 생성 및 이메일 전송
    String randomPassword = createRandomPassword();
    SimpleMailMessage message = mailService.sendAuthEmail(
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
