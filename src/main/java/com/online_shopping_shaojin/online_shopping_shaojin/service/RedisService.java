package com.online_shopping_shaojin.online_shopping_shaojin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Collections;

@Service
@Slf4j
public class RedisService {//redis is like a hashmap
    @Resource
    JedisPool jedisPool;
    public void set(String key, String value){
        Jedis resource = jedisPool.getResource();
        resource.set(key, value);
        resource.close();
    }
    public String get(String key){
        Jedis resource = jedisPool.getResource();
        String value=resource.get(key);
        resource.close();
        return value;
    }
    public long stockDeduct(String key){
        Jedis resource = jedisPool.getResource();
        String script =//redis里lua script 原子性，要么程序全做要么不做
                "if redis.call('exists', KEYS[1]) == 1 then\n" +
        " local stock = tonumber(redis.call('get', KEYS[1]))\n" +
        " if (stock<=0) then\n" +
                " return -1\n" +
                " end\n" +
                "\n" +
                " redis.call('decr', KEYS[1]);\n" +
                " return stock - 1;\n" +
                "end\n" +
                "\n" +
                "return -1;";
        Object eval = resource.eval(script, Collections.singletonList(key), Collections.emptyList());
        resource.close();
        return (long)eval;
    }

    public boolean getDistributedLock(String key, String requestId, Integer expireTime){
        Jedis resource=jedisPool.getResource();
        String res=resource.set(key, requestId, "NX", "PX", expireTime);//PX represents it has expire time, NX represents if there is already same key in resource, omit this line
        resource.close();
        if ("OK".equals(res)){
            return true;
        }
        return false;
    }

    public boolean releaseDistributedLock(String key, String requestId){
        Jedis resource = jedisPool.getResource();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1]" +
                " then return redis.call('del', KEYS[1])" +
                " else return 0 end";
        Long result = (Long) resource.eval(script,
                Collections.singletonList(key),
                Collections.singletonList(requestId));
        resource.close();
        if (result == 1L) {
            return true;
        }
        return false;
    }

    public void revertStock(String key){
        Jedis resource = jedisPool.getResource();
        resource.incr(key);
        resource.close();
    }

    public boolean isInDenyList(Long userId, Long commodityId) {//为防止有人恶意抢占名额，我们要设置名单限购，每个帐号只能下一单
        Jedis jedisClient = jedisPool.getResource();
        Boolean isInDenyList = jedisClient.sismember("denyList:" +
                        userId,
                String.valueOf(commodityId));
        jedisClient.close();
        log.info("userId: {} , commodityId {} is InDenyList result: {}",
                userId, commodityId,
                isInDenyList);
        return isInDenyList;
    }
    public void addToDenyList(Long userId, Long commodityId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.sadd("denyList:" + userId,
                String.valueOf(commodityId));
        jedisClient.close();
        log.info("Add userId: {} into denyList for commodityId: {}",
                userId, commodityId);
    }
    public void removeFromDenyList(Long userId, Long commodityId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.srem("denyList:" + userId,
                String.valueOf(commodityId));
        jedisClient.close();
        log.info("Remove userId: {} into denyList for commodityId: {}",
                userId, commodityId);
    }
}
