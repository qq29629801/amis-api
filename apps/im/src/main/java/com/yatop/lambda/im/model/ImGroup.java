package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.OneToMany;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;

@Table(name = "im_group")
public class ImGroup extends Model<ImGroup> {
    @Id
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "avatar")
    private String avatar;

    @OneToMany
    private List<ImGroupUser> groupUserList;
}
