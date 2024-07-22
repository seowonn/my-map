package com.seowonn.mymap.infra.redis.service;

import static com.seowonn.mymap.global.type.Prefix.VERIFICATION_PREFIX;
import static com.seowonn.mymap.global.type.Prefix.VIEW_COUNT_PREFIX;
import static com.seowonn.mymap.global.type.TimeSettings.VIEWS_COUNT_EXPIRE_TIME;

import com.seowonn.mymap.global.type.Boolean;
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
public class RedisService {

  private final RedisTemplate<String, String> redisTemplate;

  public String getVerificationData(String key) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(VERIFICATION_PREFIX.getPrefix() + key);
  }

  public String getViewsData(Long visitLogId, String userId) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(
        VIEW_COUNT_PREFIX.getPrefix() + visitLogId + userId);
  }

  public void setValidationExpireTime(String key, String value,
      long duration) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    Duration expireDuration = Duration.ofSeconds(duration);
    valueOperations.set(VERIFICATION_PREFIX.getPrefix() + key, value, expireDuration);
  }

  public void makeViewCountExpire(long visitLogId, String userId) {
    ValueOperations<String, String> valueOperations =
        redisTemplate.opsForValue();

    valueOperations.set(
        VIEW_COUNT_PREFIX.getPrefix() + visitLogId + userId,
        Boolean.TRUE.getFlag()
    );

    Duration expireDuration = Duration.ofSeconds(
        VIEWS_COUNT_EXPIRE_TIME.getTime()
    );

    redisTemplate.expire(
        VIEW_COUNT_PREFIX.getPrefix() + visitLogId + userId, expireDuration
    );
  }

}
