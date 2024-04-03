package com.seowonn.mymap.service.Impl;

import com.seowonn.mymap.service.RedisService;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
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
  private final String VERIFICATION_PREFIX = "VERIFY:";

  @Override
  public String getData(String key) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(VERIFICATION_PREFIX + key);
  }

  @Override
  public long getRemainingExpireTime(String key) {
    Long expireTime = redisTemplate.getExpire(VERIFICATION_PREFIX + key,
        TimeUnit.SECONDS);
    log.info("[getRemainingExpireTime] : redis 남은 유효시간 확인");
    return expireTime != null ? expireTime : -1;
  }

  @Override
  public void setDataExpire(String key, String value, long duration) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    Duration expireDuration = Duration.ofSeconds(duration);
    valueOperations.set(VERIFICATION_PREFIX + key, value, expireDuration);
    log.info("[setDataExpire] : redis 키 값 저장. 유효시간 설정");
  }

  @Override
  public void deleteData(String key) {
    redisTemplate.delete(VERIFICATION_PREFIX + key);
    log.info("[deleteData] : redis 키 값 삭제");
  }

}
