package com.seowonn.mymap.config.security;

import static com.seowonn.mymap.type.ErrorCode.USER_NOT_FOUND;

import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.MemberRepository;
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
