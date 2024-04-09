package com.seowonn.mymap.service;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

public interface OpenApiService {
  String makeUrl(String place);

  void fetchSiDo() throws ParseException;

  JSONArray parseJsonString(String jsonString) throws ParseException;

  void fetchSiGunGu() throws ParseException;

}
