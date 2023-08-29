package com.yatop.lambda.base.model.policy;

import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.HashMap;
import java.util.Map;

@Table(name = "base_rowLevel_policy")
public class RowLevelPolicy extends Model<RowLevelPolicy> {
    private static final long serialVersionUID = -2332931381828513664L;

    private String entityName;

    private RowLevelPolicyAction action;

    private RowLevelPredicate<Object> predicate;

    private String whereClause;

    private String joinClause;

    private String script;

    private Map<String, String> customProperties = new HashMap<>();

}
