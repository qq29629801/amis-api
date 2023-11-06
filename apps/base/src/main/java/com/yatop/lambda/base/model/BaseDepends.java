package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_depends", displayName = "应用依赖")
public class BaseDepends extends Model<BaseDepends> {
    @Id
    private Long id;


    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private BaseApp baseApp;
}
