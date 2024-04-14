package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.EXPIRED_VERIFICATION;
import static com.seowonn.mymap.type.ErrorCode.INCORRECT_CODE;
import static com.seowonn.mymap.type.ErrorCode.INCORRECT_EMAIL;
import static com.seowonn.mymap.type.ErrorCode.INCORRECT_PASSWORD;
import static com.seowonn.mymap.type.ErrorCode.USERID_EXISTS;
import static com.seowonn.mymap.type.ErrorCode.USER_NOT_FOUND;

import com.seowonn.mymap.config.security.jwt.JwtTokenProvider;
import com.seowonn.mymap.dto.EmailDto;
import com.seowonn.mymap.dto.member.MemberFormDto;
import com.seowonn.mymap.dto.member.SignInForm;
import com.seowonn.mymap.dto.member.SignInResponse;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.MemberRepository;
import com.seowonn.mymap.service.MailService;
import com.seowonn.mymap.service.MemberService;
import com.seowonn.mymap.type.Role;
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
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final MailService mailService;
  private final RedisServiceImpl redisServiceImpl;
  private final JwtTokenProvider jwtTokenProvider;

  private final static long VERIFICATION_EXPIRE_TIME = 600 * 5;
  private final static int PASSWORD_LENGTH = 10;
  private static final char[] CHAR_SET =
      new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
          'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
          'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
          'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
          'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&' };

  @Override
  public SimpleMailMessage sendVerificationCode(EmailDto emailDto) {

    // 인증 번호 생성
    String verificationNum = createNumber();

    // redis에 인증 번호 저장
    redisServiceImpl.setDataExpire(
        emailDto.getEmailAddress(), verificationNum, VERIFICATION_EXPIRE_TIME);

    return mailService.sendAuthEmail(emailDto.getEmailAddress(),
        verificationNum);
  }

  @Override
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

  public Member createMember(MemberFormDto memberFormDto, Role role) {

    // 이미 등록된 아이디(이메일)인지 확인
    if (memberRepository.existsByUserId(memberFormDto.getUserId())) {
      throw new MyMapSystemException(USERID_EXISTS);
    }

    // redis code & 인증 번호 검증
    checkVerificationCode(memberFormDto.getUserId(),
        memberFormDto.getVerificationNum());

    Member member = Member.ofMemberFormAndRole(memberFormDto, role);
    return memberRepository.save(member);
  }

  public void checkVerificationCode(String email, String verificationCode) {
    String redisCode = redisServiceImpl.getData(email);

    // 다른 아이디(이메일) 값을 입력하여 redis code가 null일 경우 에러 처리
    if (redisCode == null) {
      throw new MyMapSystemException(INCORRECT_EMAIL);
    }

    // 만료된 인증 번호에 대한 에러 처리
    long remainingTime = redisServiceImpl.getRemainingExpireTime(email);
    if (remainingTime <= 0) {
      throw new MyMapSystemException(EXPIRED_VERIFICATION);
    }

    // 인증 번호가 다른 사용자에 대한 에러 처리
    if (!redisCode.equals(verificationCode)) {
      throw new MyMapSystemException(INCORRECT_CODE);
    }

    redisServiceImpl.deleteData(email);
  }

  @Override
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
    for(int i = 0; i < MemberServiceImpl.PASSWORD_LENGTH; i++){
      idx = random.nextInt(len);
      sb.append(CHAR_SET[idx]);
    }
    log.info("[createRandomPassword] : 임시 번호 생성 완료");

    return sb.toString();
  }

}
