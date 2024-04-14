package com.seowonn.mymap.service;

public interface OpenApiService {
  String makeUrl(String place);

  void fetchSiDo();

  void fetchSiGunGu();

}
