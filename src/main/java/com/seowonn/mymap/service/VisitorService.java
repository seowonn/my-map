package com.seowonn.mymap.service;

import com.seowonn.mymap.dto.BookMarkDto;
import com.seowonn.mymap.dto.visitLog.VisitLogResponse;
import com.seowonn.mymap.dto.visitLog.VisitLogUserInputForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitorService {

  VisitLogResponse getVisitLogDetails(Long visitLogId);

  Page<VisitLogResponse> getAllVisitLogsFromMyMap(Long mayMapId, Pageable pageable);

  VisitLogResponse applyUserInput(Long myMapId, Long visitLogId, VisitLogUserInputForm form);

  Page<BookMarkDto> getMarkedLogs(Pageable pageable);

  Page<BookMarkDto> getCategoryMarkedLogs(String categoryName, Pageable pageable);
}
