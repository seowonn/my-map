package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.ErrorCode.LOADING_FILE_ERROR;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.seowonn.mymap.entity.Image;
import com.seowonn.mymap.entity.MyMap;
import com.seowonn.mymap.entity.VisitLog;
import com.seowonn.mymap.exception.AWSS3Exception;
import com.seowonn.mymap.repository.ImageRepository;
import com.seowonn.mymap.service.S3Service;
import java.io.IOException;
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
public class S3ServiceImpl implements S3Service {

  private final AmazonS3Client amazonS3Client;
  private final ImageRepository imageRepository;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Override
  public void upload(List<MultipartFile> multipartFiles,
      MyMap myMap, VisitLog visitLog) {

    for(MultipartFile multipartFile : multipartFiles) {

      // 파일 업로드 하지 않은 경우 처리
      if(!Objects.equals(multipartFile.getOriginalFilename(), "")) {

        String fileName =
            myMap.getMember().getUserId() + "/" + myMap.getId() + "/"
                + multipartFile.getOriginalFilename();

        String uploadedImageUrl = putS3(multipartFile, fileName);

        Image image = Image.of(uploadedImageUrl, visitLog);
        imageRepository.save(image);
        log.info("[upload] : 이미지 url 저장 완료");
      }
    }
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
