package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.*;

@Table(name = "im_conversation", displayName = "会话")
public class Conversation {
    @Id
    private Long id;
    /**
     *
     */


    /**
     * 会话ID
     */
    @Column(label = "会话ID")
    private Long conversationId;

    /**
     * 所属用户

     */
    @Column(label = "所属用户")
    private Long userId;

    /**
     * 会话类型
     私聊：private
     群聊：group
     */
    @Column(label = "会话类型")
    @Selection(options = {
            @Option(label = "私聊", value = "private"),
            @Option(label = "群聊", value = "group")})
    private String type;

    /**
     * 未读数
     */
    @Column(label = "未读数")
    private Integer unread;

    /**
     * 最新消息ID
     */
    @Column(label = "最新消息ID")
    private Long lastMessageId;

    /**
     * 更新时间
     */
    @Column(label = "更新时间")
    private Long updatedAt;

    /**
     * 创建时间
     */
    @Column(label = "创建时间")
    private Long createdAt;


}
