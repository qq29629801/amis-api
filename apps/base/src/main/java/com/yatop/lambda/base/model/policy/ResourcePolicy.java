package com.yatop.lambda.base.model.policy;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_resource_policy")
public class ResourcePolicy extends Model<ResourcePolicy> {
    @Column(name = "action")
    private String action;

    @Column(name = "effect")
    private Effect effect;

    @Column(name = "resource")
    private String resource;

    @Column(name = "type")
    private ResourceType type;

    @Column(name = "policy_group")
    private String policyGroup;
}
