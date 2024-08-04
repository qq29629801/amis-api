package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;

@Table(name = "im_conversation", displayName = "会话")
public class Conversation {
    @Id
    private Long id;
    @Column(name = "msg_context")
    private String msgContext;


}
