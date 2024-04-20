package com.seowonn.mymap.controller;

import static com.seowonn.mymap.type.SuccessMessage.DATA_LOAD_COMPLETE;
import static com.seowonn.mymap.type.SuccessMessage.DELETE_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.PROFILE_UPDATE_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.SIGN_OUT_SUCCESS;
import static com.seowonn.mymap.type.SuccessMessage.USER_PROFILE_VIEWED;

import com.seowonn.mymap.dto.ApiResponse;
import com.seowonn.mymap.dto.category.CategoryForm;
import com.seowonn.mymap.dto.category.CategoryResponse;
import com.seowonn.mymap.dto.member.MemberInfoResponse;
import com.seowonn.mymap.dto.member.UpdateUserInfoForm;
import com.seowonn.mymap.service.AdminService;
import com.seowonn.mymap.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final AdminService adminService;

  @GetMapping("/profile/{userId}")
  public ApiResponse<?> getUserProfile(@PathVariable String userId) {
    MemberInfoResponse memberInfoResponse = userService.getUserProfile(userId);
    return ApiResponse.createSuccessMessage(memberInfoResponse, USER_PROFILE_VIEWED);
  }

  @PatchMapping("/profile/{userId}")
  public ApiResponse<?> updateUserId(
      @PathVariable String userId,
      @Valid @RequestBody UpdateUserInfoForm userIdForm) {
    MemberInfoResponse memberInfoResponse = userService.updateUser(userId, userIdForm);
    return ApiResponse.createSuccessMessage(memberInfoResponse, PROFILE_UPDATE_SUCCESS);
  }

  @DeleteMapping("/profile")
  public ApiResponse<?> updateUserId() {
    userService.signOutUser();
    return ApiResponse.createSuccessMessage(true, SIGN_OUT_SUCCESS);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/category")
  public ApiResponse<?> createCategory(@RequestBody CategoryForm categoryForm){
    List<CategoryResponse> categoryResponse = adminService.addCategory(categoryForm);
    return ApiResponse.createSuccessMessage(categoryResponse, DATA_LOAD_COMPLETE);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/category")
  public ApiResponse<?> deleteCategory(@RequestBody CategoryForm categoryForm){
    adminService.deleteCategory(categoryForm);
    return ApiResponse.createSuccessMessage(true, DELETE_SUCCESS);
  }

}
