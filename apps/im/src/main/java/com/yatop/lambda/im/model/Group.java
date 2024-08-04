package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
@APP(displayName = "通讯服务", name = "im", depends = "net")
@Table(name = "im_group", displayName = "群组")
public class Group extends Model<Group> {
    @Id
    private Long id;
    @Column(label = "群组名称")
    private String name;
    @File(label = "群组头像")
    private String avatarUrl;
    @Column(label = "群主用户ID")
    private Long leaderUserId;
    @Column(label = "加群方式")
    private Integer joinMode;
    @Column(label = "群介绍")
    private String introduction;
    @Column(label = "群公告")
    private String notification;
    @Dict(typeCode = "mute", label = "全体禁言")
    private String isMute;
    @Dict(typeCode = "dissolve", label = "解散群组")
    private String isDissolve;
    @Column(label = "创建时间")
    private Date createdAt;
}
