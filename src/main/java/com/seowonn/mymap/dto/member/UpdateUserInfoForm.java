package com.seowonn.mymap.dto.member;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserInfoForm {

  private String currentId;
  private String newId;

  private String currentPassword;
  private String newPassword;

  private String verificationNum;

}
