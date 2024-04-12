package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.INCORRECT_PASSWORD;
import static com.seowonn.mymap.type.ErrorCode.USER_NOT_FOUND;

import com.seowonn.mymap.dto.member.MemberInfoResponse;
import com.seowonn.mymap.dto.member.UpdateUserInfoForm;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.MemberRepository;
import com.seowonn.mymap.service.UserService;
import com.seowonn.mymap.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final MemberRepository memberRepository;
  private final MemberServiceImpl memberService;
  private final CheckService checkService;


  public MemberInfoResponse getUserProfile(String userId) {

    checkService.checkIsLoginUser(userId);

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    return MemberInfoResponse.entityToDto(member);
  }

  public MemberInfoResponse updateUser(UpdateUserInfoForm userInfoForm) {

    checkService.checkIsLoginUser(userInfoForm.getCurrentId());

    // 이메일 인증 확인
    memberService.checkVerificationCode(userInfoForm.getNewId(),
        userInfoForm.getVerificationNum());

    // 기존 아이디로 검색
    Member member = memberRepository.findByUserId(userInfoForm.getCurrentId())
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));
    member.setUserId(userInfoForm.getNewId());

    // 기존 비밀번호 조회 & 입력 pw 암호화 후 비교
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    if (!passwordEncoder.matches(userInfoForm.getCurrentPassword(),
        member.getPassword())) {
      throw new MyMapSystemException(INCORRECT_PASSWORD);
    }
    member.setPassword(userInfoForm.getNewPassword());

    Member saved = memberRepository.save(member);
    return MemberInfoResponse.entityToDto(saved);
  }

  /**
   * TODO 추후 마이맵, 방문 일지 삭제와도 연관성 고려 필요
   */
  public void signOutUser(String userId) {

    checkService.checkIsLoginUser(userId);

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    memberRepository.delete(member);
  }
}
