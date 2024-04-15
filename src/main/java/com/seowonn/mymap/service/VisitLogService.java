package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.NewVisitLogDto;
import com.seowonn.mymap.dto.UpdateVisitLogDto;
import com.seowonn.mymap.entity.VisitLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitLogService {

  VisitLog createVisitLog(Long myMapId, NewVisitLogDto newVisitLogDto);

  Page<VisitLog> getVisitLogs(Long myMapId, Pageable pageable);

  void deleteVisitLog(Long myMapId, Long visitLogId);

  VisitLog updateLog(Long myMapId, Long visitLogId, UpdateVisitLogDto updateVisitLogDto);

  void updateViews(Long visitLogId);

  VisitLog visitLogDetails(Long visitLogId);
}
