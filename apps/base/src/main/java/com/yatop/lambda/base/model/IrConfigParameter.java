package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_config_parameter")
public class IrConfigParameter extends Model<IrConfigParameter> {
    private String key;
    private String value;

}
