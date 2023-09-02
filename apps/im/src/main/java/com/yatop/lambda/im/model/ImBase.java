package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.sql.Timestamp;

@Table(name = "im_base")
public class ImBase extends Model<ImBase> {
    @Id
    private Long id;

    @Column
    private Timestamp createDate;

    @Column
    private Timestamp updateDate;

    @Column
    private String createUser;

    @Column
    private String updateUser;

}
