package com.seowonn.mymap.service;

import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.entity.VisitLog;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

  void upload(List<MultipartFile> multipartFiles, MyMap myMap, VisitLog visitLog);

}
