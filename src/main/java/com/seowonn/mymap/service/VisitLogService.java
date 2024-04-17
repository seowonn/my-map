package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.visitLog.NewVisitLogDto;
import com.seowonn.mymap.dto.visitLog.UpdateVisitLogDto;
import com.seowonn.mymap.entity.VisitLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitLogService {

  VisitLog createVisitLog(Long myMapId, NewVisitLogDto newVisitLogDto);

  Page<VisitLog> getUsersVisitLogs(Long myMapId, Pageable pageable);

  void deleteVisitLog(Long myMapId, Long visitLogId);

  VisitLog updateLog(Long myMapId, Long visitLogId, UpdateVisitLogDto updateVisitLogDto);

}
