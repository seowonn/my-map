package com.seowonn.mymap.infra.awsS3.service;

import static com.seowonn.mymap.global.type.ErrorCode.IMAGE_NOT_FOUND;
import static com.seowonn.mymap.global.type.ErrorCode.LOADING_FILE_ERROR;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.seowonn.mymap.domain.visitLog.entity.Image;
import com.seowonn.mymap.domain.myMap.entity.MyMap;
import com.seowonn.mymap.domain.visitLog.entity.VisitLog;
import com.seowonn.mymap.infra.awsS3.exception.AWSS3Exception;
import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import com.seowonn.mymap.domain.visitLog.repository.ImageRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3Client amazonS3Client;
  private final ImageRepository imageRepository;

  private final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(
      Regions.AP_NORTHEAST_2).build();

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.s3.baseUrl}")
  private String BASE_URL;

  public List<String> upload(List<MultipartFile> multipartFiles,
      MyMap myMap, VisitLog visitLog) {

    List<String> imageUrls = new ArrayList<>();
    for(MultipartFile multipartFile : multipartFiles) {

      // 파일 업로드 하지 않은 경우 처리
      if(!Objects.equals(multipartFile.getOriginalFilename(), "")) {

        String fileName =
            myMap.getMember().getUserId() + "/" + myMap.getId() + "/"
                + visitLog.getId() + "/" + multipartFile.getOriginalFilename();

        String uploadedImageUrl = putS3(multipartFile, fileName);
        imageUrls.add(uploadedImageUrl);

        Image image = Image.of(uploadedImageUrl, visitLog);
        imageRepository.save(image);
        log.info("[upload] : 이미지 url 저장 완료");
      }
    }

    return imageUrls;
  }

  public void deleteVisitLogFile(String fileName, VisitLog visitLog) {

    Image image = imageRepository.findFirstByImageUrlAndVisitLog(fileName, visitLog)
        .orElseThrow(() -> new MyMapSystemException(IMAGE_NOT_FOUND));

    deleteS3File(image.getImageUrl());

    imageRepository.delete(image);
    log.info("[deleteFile] : 삭제 요청 이미지 삭제 완료");
  }

  public void deleteS3File(String imageUrl) {

    String key = imageUrl.replace(BASE_URL, "").replace("%40", "@");

    try {
      s3.deleteObject(bucket, key);
    } catch (AmazonServiceException e){
      throw new AWSS3Exception(e.getErrorMessage());
    }
    log.info("[deleteS3File] : s3에 저장된 이미지 삭제 완료");

  }

  private String putS3(MultipartFile multipartFile, String fileName) {

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(multipartFile.getSize());
    metadata.setContentType(multipartFile.getContentType());

    log.info("[putS3] : 파일 aws S3 업로드 시작");

    try {
      amazonS3Client.putObject(
          new PutObjectRequest(
              bucket, fileName, multipartFile.getInputStream(), metadata
          ));
    } catch (IOException e) {
      throw new AWSS3Exception(LOADING_FILE_ERROR);
    }

    log.info("[putS3] : 파일 aws S3 업로드 완료");

    return amazonS3Client.getUrl(bucket, fileName).toString();
  }

}
