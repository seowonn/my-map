package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.member.MemberInfoDto;
import com.seowonn.mymap.dto.member.UpdateUserInfoForm;

public interface UserService {

  MemberInfoDto getUserProfile(String userId);

  MemberInfoDto updateUser(UpdateUserInfoForm userId);

  void signOutUser(String userId);

}
