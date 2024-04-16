package com.seowonn.mymap.service;

import com.seowonn.mymap.entity.VisitLog;

public interface VisitorService {

  VisitLog addVisitLogLikes(Long myMapId, Long visitLogId);

  VisitLog deleteVisitLogLikes(Long myMapId, Long visitLogId);

}
