package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.visitLog.NewVisitLogDto;
import com.seowonn.mymap.dto.visitLog.UpdateVisitLogDto;
import com.seowonn.mymap.dto.visitLog.VisitLogResponse;
import com.seowonn.mymap.entity.VisitLog;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitLogService {

  VisitLogResponse createVisitLog(Long myMapId, NewVisitLogDto newVisitLogDto);

  Page<VisitLogResponse> getUsersVisitLogs(Long myMapId, Pageable pageable);

  List<String> deleteVisitLogGetDeleteUrls(Long myMapId, Long visitLogId);

  VisitLogResponse updateLog(Long myMapId, Long visitLogId, UpdateVisitLogDto updateVisitLogDto);

}
