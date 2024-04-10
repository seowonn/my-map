package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.NewVisitLogDto;
import com.seowonn.mymap.entity.VisitLog;

public interface VisitLogService {

  VisitLog createVisitLog(Long myMapId, NewVisitLogDto newVisitLogDto);
}
