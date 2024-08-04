package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.CascadeType;

@Table(name = "im_friend", displayName = "好友")
public class Friend extends Model<Friend> {
    @Id
    private Long id;

    private Long userId;

    private Long friendId;

    @Column(name = "user_remark")
    private String userRemark;
}
