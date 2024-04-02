package com.seowonn.mymap.service.Impl;

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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final MailService mailService;

  public Member createMember(MemberFormDto memberFormDto, Role role){

    // redis code & 인증 번호 비교

    // 인증 번호가 다른 사용자에 대한 에러 처리

    Member member = Member.buildFromDto(memberFormDto, role);
    return memberRepository.save(member);
  }

  @Override
  public SimpleMailMessage sendVerificationCode(EmailDto emailDto) {
    
    // 이미 등록된 아이디(이메일)인지 확인
    Optional<Member> optionalMember =
        memberRepository.findByUserId(emailDto.getEmailAddress());

    if(optionalMember.isPresent()){
      throw new MyMapSystemException(USERID_EXISTS);
    }
    
    // 인증 번호 생성
    String verificationNum = createNumber();

    // redis에 인증 번호 저장


    String title = "이메일 인증 번호 : 인증 번호를 확인해주세요";

    return mailService.sendAuthEmail(
        emailDto.getEmailAddress(), title, verificationNum);
  }

  private String createNumber() {

    SecureRandom random = new SecureRandom();
    int code = random.nextInt(900000) + 100000;

    return String.valueOf(code);
  }

}
