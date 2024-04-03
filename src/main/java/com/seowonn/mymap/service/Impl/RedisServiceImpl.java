package com.seowonn.mymap.service.Impl;

import com.seowonn.mymap.service.RedisService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

  private final RedisTemplate<String, String> redisTemplate;

  public String getData(String key) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void setDataExpire(String key, String value, long duration) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    Duration expireDuration = Duration.ofSeconds(duration);
    valueOperations.set(key, value, expireDuration);
    log.info("[setDataExpire] : redis 키 값 저장. 유효시간은 5분");
  }

  public void deleteData(String key) {
    redisTemplate.delete(key);
    log.info("[deleteData] : redis 키 값 삭제");
  }

}
