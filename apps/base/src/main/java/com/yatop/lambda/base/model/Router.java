package com.yatop.lambda.base.model;

import cn.dev33.satoken.stp.StpUtil;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.Map;

@Table(name = "base_router")
public class Router extends Model<Router> {
    @Id
    private Long id;


    @Service
    public void match() {

    }

    @Service
    public void check(Map<String, Object> arguments) {
        String model = (String) arguments.get("model");
        String service = (String) arguments.get("service");
        String app = (String) arguments.getOrDefault("app", "base");
        String perms = String.format("%s:%s:%s", app, model, service);
        if (!StpUtil.getPermissionList().contains("*:*:*")) {
            StpUtil.checkPermissionAnd(perms);
        }
    }

}