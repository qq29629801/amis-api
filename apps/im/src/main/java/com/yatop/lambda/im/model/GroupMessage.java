package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "im_msg", displayName = "消息")
public class GroupMessage extends Model<GroupMessage> {
    @Id
    private Long id;
    @Column(name = "msg_context", label = "内容")
    private String msgContext;
    @Column(name = "to_user_id", label = "发送人")
    private String toUserId;
    @Column(name = "to_user_name", label = "发送用户名")
    private String toUserName;
    @Column(name = "to_user_head_img", label = "发送头像")
    private String toUserHeadImg;
    @Column(name = "from_user_id", label = "接收人")
    private String fromUserId;
    @Column(name = "from_user_name", label = "接收人用户名")
    private String fromUserName;
    @Column(name = "from_user_head_img", label = "接收人头像")
    private String fromUserHeadImg;

}
