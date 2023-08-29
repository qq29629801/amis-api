package com.yuyaogc.lowcode.engine.context;

import java.util.HashMap;
import java.util.Map;

public class ContextHandler {

    private final static String CONTEXT = "context";

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static void setContext(Object value) {
        set(CONTEXT, value);
    }

    public static Context getContext() {
        return (Context) get(CONTEXT);
    }

}
