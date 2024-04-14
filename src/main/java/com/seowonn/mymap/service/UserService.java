package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.member.MemberInfoResponse;
import com.seowonn.mymap.dto.member.UpdateUserInfoForm;

public interface UserService {

  MemberInfoResponse getUserProfile(String userId);

  MemberInfoResponse updateUser(String userId, UpdateUserInfoForm userInfoForm);

  void signOutUser(String userId);

}
