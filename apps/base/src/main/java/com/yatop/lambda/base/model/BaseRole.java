package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_role")
public class BaseRole extends Model<BaseRole> {
    @Id
    private Long id;

    @Column
    private String code;
    @Column
    private String roleName;

}
