package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.BookMarkDto;
import com.seowonn.mymap.dto.visitLog.VisitLogDto;
import com.seowonn.mymap.dto.visitLog.VisitLogUserInputForm;
import com.seowonn.mymap.entity.VisitLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitorService {

  VisitLog getVisitLogDetails(Long visitLogId);

  Page<VisitLogDto> getAllVisitLogsFromMyMap(Long mayMapId, Pageable pageable);

  VisitLogDto applyUserInput(Long myMapId, Long visitLogId, VisitLogUserInputForm form);

  Page<BookMarkDto> getMarkedLogs(Pageable pageable);
}
