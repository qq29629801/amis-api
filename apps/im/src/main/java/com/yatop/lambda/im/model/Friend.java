package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.CascadeType;

@Table(name = "im_friend", displayName = "好友")
public class Friend extends Model<Friend> {
    @Id
    private Long id;

    @Column(label = "用户id")
    private Long userId;

    @Column(label = "好友id")
    private Long friendId;

    @Column(name = "user_remark")
    private String userRemark;
}