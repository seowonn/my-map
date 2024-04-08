package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.DATA_SCRAPPING_ERROR;

import com.seowonn.mymap.dto.OpenApiSiDoDto;
import com.seowonn.mymap.dto.OpenApiSiggDto;
import com.seowonn.mymap.entity.SiDo;
import com.seowonn.mymap.entity.SiGunGu;
import com.seowonn.mymap.exception.LoadingDataException;
import com.seowonn.mymap.repository.SiDoRepository;
import com.seowonn.mymap.repository.SiGunGuRepository;
import com.seowonn.mymap.service.OpenApiService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenApiServiceImpl implements OpenApiService {

  @Value("${openApi.baseUrl}")
  private String BASE_URL;

  @Value("${openApi.sidoData}")
  private String SIDO;

  @Value("${openApi.siggData}")
  private String SIGG;

  @Value("${openApi.apiKey}")
  private String apiKey;

  @Value("${openApi.domain}")
  private String domain;

  private final static RestTemplate restTemplate = new RestTemplate();

  private final SiDoRepository siDoRepository;
  private final SiGunGuRepository siGunGuRepository;

  // 전체 데이터 가져오는 url 조건
  @Override
  public String makeUrl(String place) {
    return BASE_URL + "?key=" + apiKey + "&domain=" + domain
        + "&service=data&version=2.0&request=getfeature&format=json&size=1000"
        + "&page=1&geometry=false&attribute=true&crs=EPSG:3857"
        + "&geomfilter=BOX(13663271.680031825,3894007.9689600193,14817776.555251127,4688953.0631258525)"
        + "&data=" + place;
  }

  @Override
  public List<OpenApiSiDoDto> fetchSiDo() throws ParseException {

    String jsonString = restTemplate.getForObject(makeUrl(SIDO), String.class);

    JSONArray jsonFeatures = parseJsonString(jsonString);

    List<OpenApiSiDoDto> result = new ArrayList<>();

    for (Object o : jsonFeatures) {

      JSONObject feature = (JSONObject) o;
      JSONObject properties = (JSONObject) feature.get("properties");
      OpenApiSiDoDto openApiSidoDto = OpenApiSiDoDto.makeSiDoDto(properties);

      // DB에 저장 안된 것만 새로 저장
      if(!siDoRepository.existsBySiDoCode(openApiSidoDto.getDistrictCode())){
        siDoRepository.save(SiDo.buildFromDto(openApiSidoDto));
        result.add(openApiSidoDto);
      }
    }

    return result;
  }

  @Override
  public List<OpenApiSiggDto> fetchSiGunGu() throws ParseException {

    String jsonString = restTemplate.getForObject(makeUrl(SIGG), String.class);

    JSONArray jsonFeatures = parseJsonString(jsonString);

    List<OpenApiSiggDto> result = new ArrayList<>();

    for (Object o : jsonFeatures) {
      JSONObject feature = (JSONObject) o;
      JSONObject properties = (JSONObject) feature.get("properties");
      OpenApiSiggDto openApiSiggDto = OpenApiSiggDto.makeSiggDto(properties);

      // DB에 저장되지 않으면서
      if(!siGunGuRepository.existsBySiGunGuCode(openApiSiggDto.getDistrictCode())){
        // 시도 정보 추출
        String siDoName = openApiSiggDto.getCityFullName().split(" ")[0];

        // 광역시도 DB에서 시도가 조회되는 시군구일 경우만 시군구 DB에 저장
        SiDo siDo = siDoRepository.findBySiDoName(siDoName)
            .orElseThrow(() -> new LoadingDataException(DATA_SCRAPPING_ERROR));

        SiGunGu siGunGu = SiGunGu.buildFromDto(openApiSiggDto);
        siGunGu.setSiDo(siDo);

        siGunGuRepository.save(siGunGu);
        result.add(openApiSiggDto);
      }
    }

    return result;
  }

  @Override
  public JSONArray parseJsonString(String jsonString) throws ParseException {

    log.info("[parseJsonString] : 가져온 open api 데이터 파싱 시작");
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);

    JSONObject jsonResponse = (JSONObject) jsonObject.get("response");

    JSONObject jsonResult = (JSONObject) jsonResponse.get("result");

    JSONObject jsonFeature = (JSONObject) jsonResult.get("featureCollection");

    log.info("[parseJsonString] : 파싱 완료");
    return (JSONArray) jsonFeature.get("features");
  }

}
