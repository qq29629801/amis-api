package com.yatop.lambda.im.model;

import com.yatop.lambda.im.ex.RedisConnectException;
import com.yatop.lambda.im.util.RedisUtil;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;

@Table(name = "im_redis", displayName = "缓存")
public class ImRedis {

    @Service(displayName = "set")
    public void set(String key, String value) throws RedisConnectException {
        RedisUtil.me().set(key, value);
    }


    @Service(displayName = "get")
    public String get(String key) throws RedisConnectException {
        return RedisUtil.me().get(key);
    }
}
