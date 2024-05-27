package com.seowonn.mymap.service;

import static com.seowonn.mymap.global.type.ErrorCode.INCORRECT_PASSWORD;
import static com.seowonn.mymap.global.type.ErrorCode.USER_NOT_FOUND;

import com.seowonn.mymap.domain.member.dto.member.MemberInfoResponse;
import com.seowonn.mymap.domain.member.dto.member.UpdateUserInfoForm;
import com.seowonn.mymap.domain.member.entity.Member;
import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import com.seowonn.mymap.domain.member.repository.MemberRepository;
import com.seowonn.mymap.service.Impl.MemberServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final MemberRepository memberRepository;
  private final MemberServiceImpl memberService;
  private final CheckService checkService;


  public MemberInfoResponse getUserProfile(String userId) {

    checkService.checkIsLoginUser(userId);

    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    return MemberInfoResponse.from(member);
  }

  public MemberInfoResponse updateUser(String userId, UpdateUserInfoForm userInfoForm) {

    checkService.checkIsLoginUser(userId);

    // 이메일 인증 확인
    memberService.checkVerificationCode(userInfoForm.getNewId(),
        userInfoForm.getVerificationNum());

    // 기존 아이디로 검색
    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));
    member.setUserId(userInfoForm.getNewId());

    // 기존 비밀번호 조회 & 입력 pw 암호화 후 비교
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    if (!passwordEncoder.matches(userInfoForm.getCurrentPassword(),
        member.getPassword())) {
      throw new MyMapSystemException(INCORRECT_PASSWORD);
    }
    member.setPassword(BCrypt.hashpw(userInfoForm.getNewPassword(), BCrypt.gensalt()));

    Member saved = memberRepository.save(member);
    return MemberInfoResponse.from(saved);
  }

  /**
   * TODO 추후 마이맵, 방문 일지 삭제와도 연관성 고려 필요
   */
  @Transactional
  public void signOutUser() {

    Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();
    String currentUserId = authentication.getName();

    Member member = memberRepository.findByUserId(currentUserId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    memberRepository.delete(member);
  }
}
