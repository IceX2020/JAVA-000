# 作业

**4.（必做）**基于 Redis 封装分布式数据操作：

- 在 Java 中实现一个简单的分布式锁；

  ```
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
  ```

  

- 在 Java 中实现一个分布式计数器，模拟减库存。

  ```
   @Autowired
      private StringRedisTemplate redisTemplate;
  
      public void incr(String id){
          if(redisTemplate.boundValueOps(id).get()==null){
              redisTemplate.boundValueOps(id).set("1");
          }else{
              redisTemplate.boundValueOps(id).increment(1);
          }
      }
  ```

  

**5.（必做）**基于 Redis 的 PubSub 实现订单异步处理

```
JedisPool jedisPool = new JedisPool();
String channelName = "ORDER";

SubscribeOrder subscribeOrder = new SubscribeOrder(jedisPool, channelName);
PublishOrder publishOrder = new PublishOrder(jedisPool, channelName);
```

https://github.com/xiaoshen11/JAVA-000/tree/main/Week_11/redis





