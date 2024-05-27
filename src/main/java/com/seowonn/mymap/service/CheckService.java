package com.seowonn.mymap.service;

import static com.seowonn.mymap.global.type.ErrorCode.ACCESS_DENIED;

import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CheckService {

  public void checkIsLoginUser(String userId) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserId = authentication.getName();

    // 요청한 userId와 현재 로그인한 사용자 ID가 같지 않을 경우 에러 처리
    if(!userId.equals(currentUserId)){
      throw new MyMapSystemException(ACCESS_DENIED);
    }
  }

}
