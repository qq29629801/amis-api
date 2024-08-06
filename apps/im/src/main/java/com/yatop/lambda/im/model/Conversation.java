package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;

@Table(name = "im_conversation", displayName = "会话")
public class Conversation extends Model<Conversation> {
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





    @Service
    public void updateConversation(Conversation value){
        Long userId = Context.getInstance().getUserId();

        LambdaQueryWrapper<Conversation> lambdaQueryWrapper = Wrappers.lambdaQuery();
        Conversation conversation =   this.selectOne(lambdaQueryWrapper.eq(Conversation::getConversationId, value.getConversationId()).eq(Conversation::getUserId, userId));

        System.out.println(1);
    }



    public Long getId() {
        return (Long) this.get("id");
    }

    public Conversation setId(Long id) {
        this.set("id", id);
        return this;
    }

    public Long getConversationId() {
        return (Long) this.get("conversationId");
    }

    public Conversation setConversationId(Long conversationId) {
        this.set("conversationId", conversationId);
        return this;
    }

    public Long getUserId() {
        return (Long) this.get("userId");
    }

    public Conversation setUserId(Long userId) {
        this.set("userId", userId);
        return this;
    }

    public String getType() {
        return (String) this.get("type");
    }

    public Conversation setType(String type) {
        this.set("type", type);
        return this;
    }

    public Integer getUnread() {
        return (Integer) this.get("unread");
    }

    public Conversation setUnread(Integer unread) {
        this.set("unread", unread);
        return this;
    }

    public Long getLastMessageId() {
        return (Long) this.get("lastMessageId");
    }

    public Conversation setLastMessageId(Long lastMessageId) {
        this.set("lastMessageId", lastMessageId);
        return this;
    }

    public Long getUpdatedAt() {
        return (Long) this.get("updatedAt");
    }

    public Conversation setUpdatedAt(Long updatedAt) {
        this.set("updatedAt", updatedAt);
        return this;
    }

    public Long getCreatedAt() {
        return (Long) this.get("createdAt");
    }

    public Conversation setCreatedAt(Long createdAt) {
        this.set("createdAt", createdAt);
        return this;
    }
}
