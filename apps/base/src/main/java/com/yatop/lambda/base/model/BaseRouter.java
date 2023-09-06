package com.yatop.lambda.base.model;

import cn.dev33.satoken.stp.StpUtil;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_router")
public class BaseRouter extends Model<BaseRouter> {
    @Id
    private Long id;


    @Service
    public void match() {

    }

    @Service
    public void check(String app,String model,String method) {
        StpUtil.checkPermissionAnd(method);
    }

}
