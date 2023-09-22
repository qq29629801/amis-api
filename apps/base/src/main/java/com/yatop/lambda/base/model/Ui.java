package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.Date;

@Table(name = "base_ui")
public class Ui extends Model<Ui> {
    private String app;
    private String model;
    private String type;
    private String body;
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

}
