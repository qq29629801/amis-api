package com.yatop.lambda.im.util;

import com.yatop.lambda.im.ex.RedisConnectException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class RedisUtil {

    private static JedisPool jedisPool;
    private static JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    private static RedisUtil redisUtil;

    private static String host = "127.0.0.1";
    private static int port = 6379;

    public static RedisUtil me() {
        if (redisUtil == null) {
            redisUtil = new RedisUtil();
            jedisPool = new JedisPool(jedisPoolConfig, host, port, 1, "296629801");
        }
        return redisUtil;
    }


    private <T> T executeByJedis(JedisExecutor<Jedis, T> j) throws RedisConnectException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return j.excute(jedis);
        } catch (Exception e) {
            throw new RedisConnectException(e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Map<String, Object> getKeysSize() throws RedisConnectException {
        return null;
    }

    public Map<String, Object> getMemoryInfo() throws RedisConnectException {
        return null;
    }

    public Set<String> getKeys(String pattern) throws RedisConnectException {
        return this.executeByJedis(j -> j.keys(pattern));
    }

    public String get(String key) throws RedisConnectException {
        return this.executeByJedis(j -> j.get(key.toLowerCase()));
    }

    public String set(String key, String value) throws RedisConnectException {
        return this.executeByJedis(j -> j.set(key.toLowerCase(), value));
    }

    public String set(String key, String value, Long milliscends) throws RedisConnectException {
        String result = this.set(key.toLowerCase(), value);
        this.pexpire(key, milliscends);
        return result;
    }

    public Long del(String... key) throws RedisConnectException {
        return this.executeByJedis(j -> j.del(key));
    }

    public Boolean exists(String key) throws RedisConnectException {
        return this.executeByJedis(j -> j.exists(key));
    }

    public Long pttl(String key) throws RedisConnectException {
        return this.executeByJedis(j -> j.pttl(key));
    }

    public Long pexpire(String key, Long milliseconds) throws RedisConnectException {
        return this.executeByJedis(j -> j.pexpire(key, milliseconds));
    }

    public Long pexpireAt(String key, Long millisecondsTimestamp) throws RedisConnectException {
        return this.executeByJedis(j -> j.pexpireAt(key, millisecondsTimestamp));
    }

    public Long zadd(String key, Double score, String member) throws RedisConnectException {
        return this.executeByJedis(j -> j.zadd(key, score, member));
    }

    public Set<String> zrangeByScore(String key, String min, String max) throws RedisConnectException {
        return null;
    }

    public Long zremrangeByScore(String key, String start, String end) throws RedisConnectException {
        return this.executeByJedis(j -> j.zremrangeByScore(key, start, end));
    }

    public Long zrem(String key, String... members) throws RedisConnectException {
        return this.executeByJedis(j -> j.zrem(key, members));
    }

    public Set<String> zrevrangebyscore(String key, String max, String min, int offset, int count) throws RedisConnectException {
        return null;
    }

    public Long sadd(String key, String... member) throws RedisConnectException {
        return executeByJedis(j -> j.sadd(key, member));
    }

    public Set<String> smembers(String key) throws RedisConnectException {
        return executeByJedis(j -> j.smembers(key));
    }

    public boolean sismember(String key, String member) throws RedisConnectException {
        return executeByJedis(j -> j.sismember(key, member));
    }

    public Long srtem(String key, String... members) throws RedisConnectException {
        return executeByJedis(j -> j.srem(key, members));
    }

    public Set<String> sunion(String... key) throws RedisConnectException {
        return executeByJedis(j -> j.sunion(key));
    }

    public Long pfadd(String key, String... value) throws RedisConnectException {
        return executeByJedis(j -> j.pfadd(key, value));
    }

    public Long scard(String key) throws RedisConnectException {
        return executeByJedis(j -> j.scard(key));
    }

    public Long zcard(String key) throws RedisConnectException {
        return executeByJedis(j -> j.zcard(key));
    }

    public Long ttl(String key) throws RedisConnectException {
        return executeByJedis(j -> j.ttl(key));
    }

    public Long incr(String key) throws RedisConnectException {
        return executeByJedis(j -> j.incr(key));
    }
/*
    @Override
    public Number execute(ImmutableList<String> keys, Long times, Long pexpire) {
        String luaScript = buildLuaScript();
        RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
        return limitRedisTemplate.execute(redisScript, keys, times, pexpire);
    }*/


    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('pexpire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }


    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 过期时间，秒
     * @return 是否获取成功
     */
    public Boolean tryGetLock(String lockKey, String requestId, int expireTime) throws RedisConnectException {
//        return executeByJedis(j -> {
//            SetParams params = new SetParams();
//            params.nx();
//            params.px(expireTime * 1000);
//            String result = j.set(lockKey, requestId, params);
//            if ("OK".equals(result)) {
//                return true;
//            }
//            return false;
//        });
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String requestId) throws RedisConnectException {
        return executeByJedis(j -> {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = j.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            Long success = 1L;
            if (success.equals(result)) {
                return true;
            }
            return false;
        });
    }


}
