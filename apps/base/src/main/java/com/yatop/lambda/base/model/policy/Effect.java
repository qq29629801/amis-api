package com.yatop.lambda.base.model.policy;

import java.util.Objects;

public enum Effect implements EnumClass {
    /**
     * 允许
     * 拒绝
     */
    ALLOW("allow"),
    REFUSE("refuse");

    private String id;

    Effect(String id) {
        this.id = id;
    }

    public static Effect fromId(String id) {
        for (Effect o : Effect.values()) {
            if (Objects.equals(id, o.getId())) return o;
        }
        return null;
    }

    @Override
    public Object getId() {
        return id;
    }
}
