package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.DATA_SCRAPPING_ERROR;

import com.seowonn.mymap.dto.SiGunGuDto;
import com.seowonn.mymap.dto.cityOpenApi.siDo.SiDoApiResponseDto;
import com.seowonn.mymap.dto.cityOpenApi.siDo.SiDoFeature;
import com.seowonn.mymap.dto.cityOpenApi.siGunGu.SiGunGuApiResponseDto;
import com.seowonn.mymap.dto.cityOpenApi.siGunGu.SiGunGuFeature;
import com.seowonn.mymap.entity.SiDo;
import com.seowonn.mymap.entity.SiGunGu;
import com.seowonn.mymap.exception.LoadingDataException;
import com.seowonn.mymap.exception.MyMapSystemException;
import com.seowonn.mymap.repository.SiDoRepository;
import com.seowonn.mymap.repository.SiGunGuRepository;
import com.seowonn.mymap.service.OpenApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public void fetchSiDo() {

    ResponseEntity<SiDoApiResponseDto> response =
        restTemplate.getForEntity(makeUrl(SIDO), SiDoApiResponseDto.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      SiDoApiResponseDto apiResponse = response.getBody();

      if(apiResponse == null){
        throw new MyMapSystemException(DATA_SCRAPPING_ERROR);
      }

      List<SiDoFeature> siDoFeatures =
          apiResponse.getResponse().getResult().
              getFeatureCollection().getFeatures();

      for (SiDoFeature siDoFeature : siDoFeatures) {

        if(!siDoRepository.existsBySiDoCode(siDoFeature.getProperties().getCtprvn_cd())){
          siDoRepository.save(SiDo.from(siDoFeature.getProperties()));
        }

      }

    } else {
      throw new MyMapSystemException(DATA_SCRAPPING_ERROR);
    }

  }

  @Override
  public void fetchSiGunGu() {

    ResponseEntity<SiGunGuApiResponseDto> response =
        restTemplate.getForEntity(makeUrl(SIGG), SiGunGuApiResponseDto.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      SiGunGuApiResponseDto apiResponse = response.getBody();

      if(apiResponse == null){
        throw new MyMapSystemException(DATA_SCRAPPING_ERROR);
      }

      List<SiGunGuFeature> siGunGuFeatures =
          apiResponse.getResponse().getResult().
              getFeatureCollection().getFeatures();

      for (SiGunGuFeature siGunGuFeature : siGunGuFeatures) {

        SiGunGuDto siGunGuDto = siGunGuFeature.getProperties();

        // DB에 저장되지 않으면서
        if(!siGunGuRepository.existsBySiGunGuCode(siGunGuDto.getSig_cd())){
          // 시도 정보 추출
          String siDoName = siGunGuDto.getFull_nm().split(" ")[0];

          // 광역시도 DB에서 시도가 조회되는 시군구일 경우만 시군구 DB에 저장
          SiDo siDo = siDoRepository.findBySiDoName(siDoName)
              .orElseThrow(() -> new LoadingDataException(DATA_SCRAPPING_ERROR));

          SiGunGu siGunGu = SiGunGu.from(siGunGuDto);
          siGunGu.setSiDo(siDo);

          siGunGuRepository.save(siGunGu);
        }

      }

    } else {
      throw new MyMapSystemException(DATA_SCRAPPING_ERROR);
    }
  }

}
