package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.visitLog.VisitLogDto;
import com.seowonn.mymap.entity.VisitLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitorService {

  VisitLog addVisitLogLikes(Long myMapId, Long visitLogId);

  VisitLog deleteVisitLogLikes(Long myMapId, Long visitLogId);

  Page<VisitLogDto> getAllVisitLogsFromMyMap(Long mayMapId, Pageable pageable);
}
