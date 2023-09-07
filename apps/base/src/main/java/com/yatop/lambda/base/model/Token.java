package com.yatop.lambda.base.model;

import cn.dev33.satoken.stp.StpUtil;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
@Table(name = "base_token")
public class Token extends Model<Token> {
    @Service
    public Object getLoginId(){
        return StpUtil.getLoginId();
    }
}
