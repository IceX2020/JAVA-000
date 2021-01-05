package io.bruce.redis.service;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * 分布式锁
 */
public class DistributedLock {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        final RedissonClient redissonClient = Redisson.create(config);

        RLock lock = redissonClient.getLock("lock1");
        try {
            // 尝试加锁，默认锁30s，如果超时会自动续期
            lock.lock();
            //处理业务
            Thread.sleep(40*1000);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

}
