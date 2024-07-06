package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.Map;

@Table(name = "base_sys_config", displayName = "系统配置")
public class SysConfig extends Model<SysConfig> {
    @Id
    private Long id;

    @Column(name = "sys_icon", label = "系统图标")
    private String sysIcon;

    @Column(name = "sys_name" ,label = "系统名称")
    private String sysName;

    @Dict(typeCode = "sys_status")
    private String status;


    @Service
    public void create(Map<String,Object> value){
        System.out.println(1);
    }

}
