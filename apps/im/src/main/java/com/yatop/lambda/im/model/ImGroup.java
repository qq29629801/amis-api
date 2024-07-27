package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;
@APP(displayName = "通讯服务", name = "im", depends = "net")
@Table(name = "im_group", displayName = "群组")
public class ImGroup extends Model<ImGroup> {
    @Id
    private Long id;

    @Column(name = "group_name", label = "群组名称")
    private String groupName;

    @Column(name = "avatar", label = "图片")
    private String avatar;

    @OneToMany
    private List<ImGroupUser> groupUserList;
}
