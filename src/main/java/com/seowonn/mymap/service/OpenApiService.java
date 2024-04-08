package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.OpenApiSiDoDto;
import com.seowonn.mymap.dto.OpenApiSiggDto;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

public interface OpenApiService {
  String makeUrl(String place);

  List<OpenApiSiDoDto> fetchSiDo() throws ParseException;

  JSONArray parseJsonString(String jsonString) throws ParseException;

  List<OpenApiSiggDto> fetchSiGunGu() throws ParseException;

}
