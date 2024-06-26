package com.seowonn.mymap.service;

import static com.seowonn.mymap.type.ErrorCode.ACCESS_DENIED;
import static com.seowonn.mymap.type.ErrorCode.REGION_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.MY_MAP_NOT_FOUND;
import static com.seowonn.mymap.type.ErrorCode.USER_NOT_FOUND;

import com.seowonn.mymap.dto.myMap.MyMapResponse;
import com.seowonn.mymap.dto.myMap.NewMyMapDto;
import com.seowonn.mymap.dto.myMap.UpdateMyMapDto;
import com.seowonn.mymap.entity.Member;
import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.entity.SiDo;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.MemberRepository;
import com.seowonn.mymap.repository.MyMapRepository;
import com.seowonn.mymap.repository.SiDoRepository;
import com.seowonn.mymap.type.Access;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyMapService {

  private final SiDoRepository siDoRepository;
  private final MyMapRepository myMapRepository;
  private final MemberRepository memberRepository;

  private final CheckService checkService;


  public MyMapResponse registerNewMap(NewMyMapDto newMyMapDto) {

    // 해당 아이디로 로그인한 사용자인지 확인
    checkService.checkIsLoginUser(newMyMapDto.getUserId());

    MyMap myMap = MyMap.from(newMyMapDto);

    // 선택 지역(광역시도) 정보 조회
    SiDo siDo = siDoRepository.findBySiDoCode(newMyMapDto.getSiDoCode())
        .orElseThrow(() -> new MyMapSystemException(REGION_NOT_FOUND));

    // 매핑을 위한 회원 정보 가져오기
    Member member = memberRepository.findByUserId(newMyMapDto.getUserId())
        .orElseThrow(() -> new MyMapSystemException(USER_NOT_FOUND));

    myMap.setMember(member);
    myMap.setSiDo(siDo);

    myMapRepository.save(myMap);
    return MyMapResponse.from(myMap);
  }

  public Page<MyMapResponse> getAllMyMaps(String userId, Pageable pageable) {

    // 해당 아이디로 로그인한 사용자인지 확인
    checkService.checkIsLoginUser(userId);

    // 해당 아이디로 작성된 모든 마이맵 조회
    Page<MyMap> myMapPages =
        myMapRepository.findAllByMemberUserIdOrderByCreatedAt(userId, pageable);

    return MyMapResponse.fromPage(myMapPages);
  }

  @Transactional
  public MyMapResponse updateMyMap(UpdateMyMapDto updateMyMapDto, Long myMapId) {

    // 해당 아이디로 로그인한 사용자인지 확인
    checkService.checkIsLoginUser(updateMyMapDto.getUserId());

    // 해당 아이디의 마이맵 조회
    MyMap myMap = myMapRepository.findById(myMapId)
        .orElseThrow(() -> new MyMapSystemException(MY_MAP_NOT_FOUND));

    // 조회된 마이맵의 작성자 본인인지 확인
    if (!myMap.getMember().getUserId().equals(updateMyMapDto.getUserId())) {
      throw new MyMapSystemException(ACCESS_DENIED);
    }

    myMap.setMyMapTitle(updateMyMapDto.getMyMapTitle());
    myMap.setAccess(
        Access.valueOf(updateMyMapDto.getAccess().toUpperCase()));

    return MyMapResponse.from(myMap);
  }

  public void deleteMyMap(Long myMapId) {

    MyMap myMap = checkMyMapUser(myMapId);
    myMapRepository.delete(myMap);

  }

  public MyMap checkMyMapUser(Long myMapId){

    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    String currentUserId = authentication.getName();

    MyMap myMap = myMapRepository.findById(myMapId)
        .orElseThrow(() -> new MyMapSystemException(MY_MAP_NOT_FOUND));

    // 작성자 본인인지 확인
    if (!myMap.getMember().getUserId().equals(currentUserId)) {
      throw new MyMapSystemException(ACCESS_DENIED);
    }

    return myMap;
  }
}
