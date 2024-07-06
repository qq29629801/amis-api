package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
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

    @Service
    public void create(Map<String,Object> values){
        System.out.println(1);
    }

}
