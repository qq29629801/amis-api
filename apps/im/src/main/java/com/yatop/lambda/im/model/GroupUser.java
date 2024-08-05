package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.CascadeType;

import java.util.List;

@Table(name = "im_group_user", displayName = "群组用户")
public class GroupUser extends Model<GroupUser> {
    @Id
    private Long id;
    @Column(name = "group_nick_name", label = "群组昵称")
    private String groupNickName;


    @Column(label = "用户id")
    private Long userId;

    @ManyToOne(cascade = CascadeType.DELETE)
    @JoinColumn(name = "group_id")
    private Group groupId;


}
