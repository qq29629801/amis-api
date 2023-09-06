package com.yatop.lambda.base.model;

import cn.dev33.satoken.stp.StpUtil;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.Map;
@Table(name = "base_login")
public class BaseLogin extends Model<BaseLogin> {

    @Service
    public Map<String, Object> login(Map<String, String[]> args) {
        StpUtil.login(1);

        return null;
    }

    @Service
    public void logout(){

    }

}
