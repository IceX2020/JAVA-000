package io.bruce.redis.controller;

import io.bruce.redis.service.RedisCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/count")
public class CountController {

    @Autowired
    private RedisCount redisCount;

    @PostMapping("decr")
    public String decr(@RequestParam("key") String key){
        try {
            redisCount.decr(key);
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return redisCount.get(key);
    }

}
