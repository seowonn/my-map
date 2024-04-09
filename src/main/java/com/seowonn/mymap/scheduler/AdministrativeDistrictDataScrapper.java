package com.seowonn.mymap.scheduler;

import com.seowonn.mymap.service.Impl.OpenApiServiceImpl;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
public class AdministrativeDistrictDataScrapper {

  private final OpenApiServiceImpl openApiService;

  @Scheduled(fixedDelay = 60 * 1000L * 60 * 24 * 30)
  public void loadAdministrativeDistrictData() throws ParseException {

    log.info("[loadAdministrativeDistrictData] : {}, 행정구역 데이터 로딩 시작",
        LocalDateTime.now());

    openApiService.fetchSiDo();
    openApiService.fetchSiGunGu();

    log.info("[loadAdministrativeDistrictData] : {}, 행정구역 데이터 업데이트 완료",
        LocalDateTime.now());

  }
}
