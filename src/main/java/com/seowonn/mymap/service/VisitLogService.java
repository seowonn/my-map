package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.visitLog.NewVisitLogDto;
import com.seowonn.mymap.dto.visitLog.UpdateVisitLogDto;
import com.seowonn.mymap.entity.VisitLog;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitLogService {

  VisitLog createVisitLog(Long myMapId, NewVisitLogDto newVisitLogDto);

  Page<VisitLog> getUsersVisitLogs(Long myMapId, Pageable pageable);

  List<String> deleteVisitLogGetDeleteUrls(Long myMapId, Long visitLogId);

  VisitLog updateLog(Long myMapId, Long visitLogId, UpdateVisitLogDto updateVisitLogDto);

}
