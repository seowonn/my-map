package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.EXPIRED_VERIFICATION;
import static com.seowonn.mymap.type.ErrorCode.INCORRECT_CODE;
import static com.seowonn.mymap.type.ErrorCode.INCORRECT_EMAIL;
import static com.seowonn.mymap.type.ErrorCode.USERID_EXISTS;

import com.seowonn.mymap.dto.EmailDto;
import com.seowonn.mymap.dto.MemberFormDto;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.MemberRepository;
import com.seowonn.mymap.service.MailService;
import com.seowonn.mymap.service.MemberService;
import com.seowonn.mymap.type.Role;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final MailService mailService;
  private final RedisServiceImpl redisServiceImpl;

  private final static long VERIFICATION_EXPIRE_TIME = 600 * 5;

  @Override
  public SimpleMailMessage sendVerificationCode(EmailDto emailDto) {
    
    // 인증 번호 생성
    String verificationNum = createNumber();

    // redis에 인증 번호 저장
    redisServiceImpl.setDataExpire(
        emailDto.getEmailAddress(), verificationNum, VERIFICATION_EXPIRE_TIME);

    return mailService.sendAuthEmail(emailDto.getEmailAddress(), verificationNum);
  }

  private String createNumber() {

    SecureRandom random = new SecureRandom();
    int code = random.nextInt(900000) + 100000;
    log.info("[createNumber] : 인증 번호 생성 완료");

    return String.valueOf(code);
  }

  public Member createMember(MemberFormDto memberFormDto, Role role){

    // 이미 등록된 아이디(이메일)인지 확인
    if(memberRepository.existsByUserId(memberFormDto.getUserId())){
      throw new MyMapSystemException(USERID_EXISTS);
    }

    // redis code & 인증 번호 검증
    checkVerificationCode(memberFormDto.getUserId(), memberFormDto.getVerificationNum());

    Member member = Member.buildFromDto(memberFormDto, role);
    return memberRepository.save(member);
  }

  private void checkVerificationCode(String email, String verificationCode) {
    String redisCode = redisServiceImpl.getData(email);

    // 다른 아이디(이메일) 값을 입력하여 redis code가 null일 경우 에러 처리
    if(redisCode == null) {
      throw new MyMapSystemException(INCORRECT_EMAIL);
    }

    // 만료된 인증 번호에 대한 에러 처리
    long remainingTime = redisServiceImpl.getRemainingExpireTime(email);
    if(remainingTime <= 0){
      throw new MyMapSystemException(EXPIRED_VERIFICATION);
    }

    // 인증 번호가 다른 사용자에 대한 에러 처리
    if(!redisCode.equals(verificationCode)) {
      throw new MyMapSystemException(INCORRECT_CODE);
    }

    redisServiceImpl.deleteData(email);
  }

}
