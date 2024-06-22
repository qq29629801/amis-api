package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;


@Table(name = "base_data_seed", isAbstract = true)
public class Seed extends Model<Seed> {


    @Service(event = true)
    public void startUp() {
    }
}
