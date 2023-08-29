package com.yatop.lambda.base.model.policy;

import java.util.Objects;


public enum ResourceType implements EnumClass {
    /**
     *
     */
    MODEL("model"),
    PROPERTY("property"),
    MENU("menu"),
    VIEW("view"),
    SPECIAL("special");
    private String id;

    ResourceType(String id) {
        this.id = id;
    }

    @Override
    public Object getId() {
        return id;
    }

    public static ResourceType fromId(String id) {
        for (ResourceType o : ResourceType.values()) {
            if (Objects.equals(id, o.getId())) return o;
        }
        return null;
    }

    @Override
    public String toString() {
        return id;
    }
}
