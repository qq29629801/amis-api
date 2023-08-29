package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;

@Table(name = "im_chat", displayName = "会话")
public class ImChat {
    @Id
    private Long id;
    @Column(name = "msg_context")
    private String msgContext;


}
