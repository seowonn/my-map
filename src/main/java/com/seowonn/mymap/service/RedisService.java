package com.seowonn.mymap.service;

public interface RedisService {

  String getVerificationData(String key);

  String getViewsData(Long key, String userId);

  void makeViewCountExpire(long visitLogId, String userId);

  long getRemainingExpireTime(String key);

  void setEmailValidationExpire(String key, String value, long duration);

  void deleteVerificationData(String key);
}
