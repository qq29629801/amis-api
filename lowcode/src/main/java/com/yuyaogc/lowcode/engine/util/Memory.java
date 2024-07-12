package com.yuyaogc.lowcode.engine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.exception.EngineException;


/**
 * 加载到内存的模型记录数据
 * 
 */
public class Memory {
    Map<EntityField, Map<String, Object>> data = new HashMap<>();

    Map<String, Object> getFieldCache(EntityField field) {
        Map<String, Object> cache = data.getOrDefault(field, Collections.emptyMap());
        // TODO field_depends_context
        return cache;
    }

    Map<String, Object> setFieldCache( EntityField field) {
        Map<String, Object> cache;
        if (data.containsKey(field)) {
            cache = data.get(field);
        } else {
            cache = new HashMap<>(16);
            data.put(field, cache);
        }
        // TODO field_depends_context
        return cache;
    }

    public boolean contains(String id, EntityField field) {
        return getFieldCache(field).containsKey(id);
    }

    public Object get(String id, EntityField field) {
        Map<String, Object> cache = getFieldCache(field);
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        throw new EngineException(String.format("找不到%s.%s的值", id, field.getName()));
    }

    public Object get(String id, EntityField field, Object fallback) {
        Map<String, Object> cache = getFieldCache( field);
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        return fallback;
    }

    public void set(String id, EntityField field, Object value) {
        Map<String, Object> cache = setFieldCache(field);
        cache.put(id, value);
    }

    public void update(String[] rec, EntityField field, Collection<Object> values) {
        Map<String, Object> cache = setFieldCache(field);
        for (List<Object> idValue : ArrayUtils.zip(Arrays.asList(rec), values)) {
            cache.put((String) idValue.get(0), idValue.get(1));
        }
    }

    public void remove(String id, EntityField field) {
        Map<String, Object> cache = setFieldCache(field);
        cache.remove(id);
    }

    public Collection<Object> getValues(String[] ids, EntityField field) {
        List<Object> values = new ArrayList<>();
        Map<String, Object> cache = getFieldCache(field);
        for (String id : ids) {
            values.add(cache.get(id));
        }
        return values;
    }

    public List<String> getRecordsDifferentFrom(String[] rec, EntityField field, Object value) {
        Map<String, Object> cache = getFieldCache( field);
        List<String> ids = new ArrayList<>();
        for (String id : rec) {
            if (cache.containsKey(id)) {
                Object val = cache.get(id);
                if (!Objects.equals(val, value)) {
                    ids.add(id);
                }
            } else {
                ids.add(id);
            }
        }
        return ids;
    }


    public Collection<String> getMissingIds(String[] rec, EntityField field) {
        Map<String, Object> cache = getFieldCache(field);
        List<String> ids = new ArrayList<>();
        for (String id : rec) {
            if (!cache.containsKey(id)) {
                ids.add(id);
            }
        }
        return ids;
    }

    public void invalidate() {
        data.clear();
    }
}