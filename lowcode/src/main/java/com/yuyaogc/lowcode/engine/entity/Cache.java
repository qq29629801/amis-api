package com.yuyaogc.lowcode.engine.entity;

import java.util.HashMap;
import java.util.Map;

public class Cache {
    /**
     * 对象缓存
     */
    private Map<String, Object> data = new HashMap<>(1);

    /**
     * 缓存赋值
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        data.put(key, value);
    }


    public Object get(String key) {
        if (data.containsKey(key)) {
            return data.get(key);
        }
        return null;
    }


    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

}
