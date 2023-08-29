package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "im_msg", displayName = "消息")
public class ImMsg extends Model<ImMsg> {
    @Id
    private Long id;
    @Column(name = "msg_context")
    private String msgContext;
    @Column(name = "to_user_id")
    private String toUserId;
    @Column(name = "to_user_name")
    private String toUserName;
    @Column(name = "to_user_head_img")
    private String toUserHeadImg;
    @Column(name = "from_user_id")
    private String fromUserId;
    @Column(name = "from_user_name")
    private String fromUserName;
    @Column(name = "from_user_head_img")
    private String fromUserHeadImg;

}
