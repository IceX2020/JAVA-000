package io.bruce.redis.service;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCount {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void decr(String id){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        final RedissonClient redissonClient = Redisson.create(config);

        RLock lock = redissonClient.getLock("lock1");
        try {
            lock.lock();
            if(redisTemplate.opsForValue().get(id)==null){

                redisTemplate.opsForValue().set(id,"1");
            }else{
                if(redisTemplate.opsForValue().increment(id,-1) < 0){
                    throw new RuntimeException("库存不足");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            lock.unlock();
        }
    }

    public String get(String id){
        return redisTemplate.boundValueOps(id).get();
    }

}
