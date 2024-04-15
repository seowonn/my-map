package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.PROFILE_UPDATE_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.SIGN_OUT_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.USER_PROFILE_VIEWED;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.member.MemberInfoResponse;
import com.seowonn.mymap.dto.member.UpdateUserInfoForm;
import com.seowonn.mymap.service.Impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/profile")
public class UserController {

  private final UserServiceImpl userService;

  @GetMapping("/{userId}")
  public ApiResponse<?> getUserProfile(@PathVariable String userId) {
    MemberInfoResponse memberInfoResponse = userService.getUserProfile(userId);
    return ApiResponse.createSuccessMessage(memberInfoResponse, USER_PROFILE_VIEWED);
  }

  @PatchMapping("/{userId}")
  public ApiResponse<?> updateUserId(
      @PathVariable String userId,
      @Valid @RequestBody UpdateUserInfoForm userIdForm) {
    MemberInfoResponse memberInfoResponse = userService.updateUser(userId, userIdForm);
    return ApiResponse.createSuccessMessage(memberInfoResponse, PROFILE_UPDATE_SUCCESS);
  }

  @DeleteMapping("/{userId}")
  public ApiResponse<?> updateUserId(@PathVariable String userId) {
    userService.signOutUser(userId);
    return ApiResponse.createSuccessMessage(true, SIGN_OUT_SUCCESS);
  }

}
