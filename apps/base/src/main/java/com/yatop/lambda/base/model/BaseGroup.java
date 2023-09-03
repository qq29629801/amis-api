package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.JoinColumn;
import com.yuyaogc.lowcode.engine.annotation.ManyToOne;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.CascadeType;

@Table(name = "base_group")
public class BaseGroup extends Model<BaseGroup> {
    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.DELETE)
    @JoinColumn(name = "user_id")
    private BaseUser userId;

}
