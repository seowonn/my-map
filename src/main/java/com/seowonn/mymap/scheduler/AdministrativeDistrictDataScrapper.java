package com.seowonn.mymap.scheduler;

import com.seowonn.mymap.domain.openApi.service.OpenApiService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdministrativeDistrictDataScrapper {

  private final OpenApiService openApiService;

  @Scheduled(fixedDelay = 60 * 1000L * 60 * 24 * 30) // 30일 주기
  public void loadAdministrativeDistrictData() {
    log.info("[loadAdministrativeDistrictData] : {}, 행정구역 데이터 로딩 시작",
        LocalDateTime.now());

    openApiService.fetchSiDo();
    openApiService.fetchSiGunGu();

    log.info("[loadAdministrativeDistrictData] : {}, 행정구역 데이터 업데이트 완료",
        LocalDateTime.now());
  }
}
