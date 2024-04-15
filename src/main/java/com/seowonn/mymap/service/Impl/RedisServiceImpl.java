package com.seowonn.mymap.service.Impl;

import static com.seowonn.mymap.type.Prefix.VERIFICATION_PREFIX;
import static com.seowonn.mymap.type.Prefix.VIEW_COUNT_PREFIX;
import static com.seowonn.mymap.type.TimeSettings.VIEWS_COUNT_EXPIRE_TIME;

import com.seowonn.mymap.service.RedisService;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public String getVerificationData(String key) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(VERIFICATION_PREFIX.getPrefix() + key);
  }

  @Override
  public Set<String> getViewsData(Long key) {
    SetOperations<String, String> setOperations = redisTemplate.opsForSet();
    return setOperations.members(VIEW_COUNT_PREFIX.getPrefix() + key);
  }

  @Override
  public long getRemainingExpireTime(String key) {
    Long expireTime = redisTemplate.getExpire(VERIFICATION_PREFIX + key,
        TimeUnit.SECONDS);
    log.info("[getRemainingExpireTime] : redis 남은 유효시간 확인");
    return expireTime != null ? expireTime : -1;
  }

  @Override
  public void setEmailValidationExpire(String key, String value,
      long duration) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    Duration expireDuration = Duration.ofSeconds(duration);
    valueOperations.set(VERIFICATION_PREFIX.getPrefix() + key, value, expireDuration);
    log.info("[setEmailValidationExpire] : redis 키 값 저장. 유효시간 설정");
  }

  @Override
  public void makeViewCountExpire(long visitLogId, String userId) {
    SetOperations<String, String> setOperations = redisTemplate.opsForSet();
    setOperations.add(VIEW_COUNT_PREFIX.getPrefix() + visitLogId, userId);
    Duration expireDuration = Duration.ofSeconds(
        VIEWS_COUNT_EXPIRE_TIME.getTime());
    redisTemplate.expire(VIEW_COUNT_PREFIX.getPrefix() + visitLogId,
        expireDuration);
  }

  public void addViewCount(long visitLogId, String userId) {
    SetOperations<String, String> setOperations = redisTemplate.opsForSet();
    setOperations.add(VIEW_COUNT_PREFIX.getPrefix() + visitLogId, userId);
  }

  @Override
  public void deleteVerificationData(String key) {
    redisTemplate.delete(VERIFICATION_PREFIX + key);
    log.info("[deleteData] : redis 키 값 삭제");
  }

}
