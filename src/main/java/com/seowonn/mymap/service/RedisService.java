package com.seowonn.mymap.service;

import java.util.Set;

public interface RedisService {

  String getVerificationData(String key);

  Set<String> getViewsData(Long key);

  void makeViewCountExpire(long visitLogId, String userId);

  long getRemainingExpireTime(String key);

  void setEmailValidationExpire(String key, String value, long duration);

  void deleteVerificationData(String key);
}
