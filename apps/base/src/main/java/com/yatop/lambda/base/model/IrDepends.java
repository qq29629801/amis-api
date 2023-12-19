package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_depends", displayName = "应用依赖")
public class IrDepends extends Model<IrDepends> {
    @Id
    private Long id;


    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private IrModule baseApp;
}
