package com.seowonn.mymap.service;

import static com.seowonn.mymap.global.type.ErrorCode.CONNECTION_LOST;
import static com.seowonn.mymap.type.Prefix.REDIS_LOCK_PREFIX;

import com.seowonn.mymap.domain.myMap.exception.MyMapSystemException;
import com.seowonn.mymap.domain.visitLogForWriter.repository.VisitLogRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisLockService {

  private final RedissonClient redissonClient;
  private final RedisService redisService;

  private final VisitLogRepository visitLogRepository;

  public void controlViewers(Long visitLogId, String userId){
    String viewCountKey = REDIS_LOCK_PREFIX.getPrefix() + visitLogId + userId;

    RLock lock = redissonClient.getLock(viewCountKey);
    try {
      if(!lock.tryLock(10, 1, TimeUnit.SECONDS)){
        return;
      }
      if (redisService.getViewsData(visitLogId, userId) == null) { // 처음 접속, redis 데이터 생성, view +1
        redisService.makeViewCountExpire(visitLogId, userId);
        visitLogRepository.updateViews(visitLogId);
      }
    } catch (InterruptedException e){
      throw new MyMapSystemException(CONNECTION_LOST);
    } finally {
      lock.unlock();
    }

  }
}
