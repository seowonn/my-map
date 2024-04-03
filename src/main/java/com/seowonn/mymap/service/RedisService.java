package com.seowonn.mymap.service;

public interface RedisService {
  String getData(String key);

  void setDataExpire(String key, String value, long duration);

  void deleteData(String key);
}