package com.seowonn.mymap.security;

import static com.seowonn.mymap.global.type.ErrorCode.USER_NOT_FOUND;

import com.seowonn.mymap.domain.member.entity.Member;
import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import com.seowonn.mymap.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {

    Member member = memberRepository.findByUserId(username)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    return new CustomUserDetails(member);
  }
}
