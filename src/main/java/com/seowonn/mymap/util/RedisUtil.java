package com.seowonn.mymap.util;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {

  private final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
  private final RedisTemplate<String, String> redisTemplate;

  public String getData(String key) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void setDataExpire(String key, String value, long duration) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    Duration expireDuration = Duration.ofSeconds(duration);
    valueOperations.set(key, value, expireDuration);
    LOGGER.info("[setDataExpire] : redis 키 값 저장. 유효시간은 5분");
  }

  public void deleteData(String key) {
    redisTemplate.delete(key);
    LOGGER.info("[deleteData] : redis 키 값 삭제");
  }

}
