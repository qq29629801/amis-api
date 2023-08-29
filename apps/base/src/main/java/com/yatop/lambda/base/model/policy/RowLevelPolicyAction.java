package com.yatop.lambda.base.model.policy;

import java.util.Objects;

public enum RowLevelPolicyAction implements EnumClass<String> {

    CREATE("create"),
    READ("read"),
    UPDATE("update"),
    DELETE("delete");

    private String id;

    RowLevelPolicyAction(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public static RowLevelPolicyAction fromId(String id) {
        for (RowLevelPolicyAction value : RowLevelPolicyAction.values()) {
            if (Objects.equals(id, value.getId())) return value;
        }
        return null;
    }
}
