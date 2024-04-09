package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.MY_MAP_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.USER_NOT_FOUND;

import com.seowonn.mymap.dto.MyMap.NewMyMapDto;
import com.seowonn.mymap.dto.MyMap.UpdateMyMapDto;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.entity.SiDo;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.MemberRepository;
import com.seowonn.mymap.repository.MyMapRepository;
import com.seowonn.mymap.repository.SiDoRepository;
import com.seowonn.mymap.service.CheckService;
import com.seowonn.mymap.service.MyMapService;
import com.seowonn.mymap.type.IsPublic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyMapServiceImpl implements MyMapService {

  private final SiDoRepository siDoRepository;
  private final MyMapRepository myMapRepository;
  private final MemberRepository memberRepository;

  private final CheckService checkService;

  @Override
  public Page<SiDo> getSiDoCites(Pageable pageable) {
    return siDoRepository.findAll(pageable);
  }

  @Override
  public MyMap registerNewMap(NewMyMapDto newMyMapDto) {

    Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    // 매핑을 위한 회원 정보 가져오기
    Member member = memberRepository.findByUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    MyMap myMap = MyMap.buildFromDto(newMyMapDto, member);
    return myMapRepository.save(myMap);
  }

  @Override
  public MyMap updateMyMap(UpdateMyMapDto updateMyMapDto, String userId) {

    // 작성자 본인인지 확인
    checkService.checkIsLoginUser(userId);

    MyMap myMap = myMapRepository.findByMemberUserId(userId)
        .orElseThrow(() -> new MyMapSystemException(MY_MAP_NOT_FOUND));

    myMap.setMyMapTitle(updateMyMapDto.getMyMapTitle());
    myMap.setIsPublic(IsPublic.valueOf(updateMyMapDto.getIsPublic().toUpperCase()));

    return myMapRepository.save(myMap);
  }
}
