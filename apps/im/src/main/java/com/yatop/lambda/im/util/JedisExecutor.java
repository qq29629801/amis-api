package com.yatop.lambda.im.util;


import com.yatop.lambda.im.ex.RedisConnectException;

@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}
