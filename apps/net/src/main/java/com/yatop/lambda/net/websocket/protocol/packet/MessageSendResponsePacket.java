package com.yatop.lambda.net.websocket.protocol.packet;

import com.yatop.lambda.net.websocket.protocol.Packet;
import com.yatop.lambda.net.websocket.protocol.command.Command;

import java.util.Date;

/**
 * 服务端发送至客户端的消息数据包
 *
 * @date 2019-04-20
 */
public class MessageSendResponsePacket extends Packet {
    private String toUserId;
    private String toUserHeadImg;
    private String toUserName;

    private String hasBeenSentId; //已发送过去消息的id
    private String content;// `很高兴认识你，这是第${i + 1}条消息。`,

    private String fromUserHeadImg;// isItMe ? this._user_info.headImg : this.fromUserInfo.fromUserHeadImg, //用户头像
    private String fromUserId;// isItMe ? this._user_info.id : this.fromUserInfo.fromUserId,
    private String fromUserName;

    private boolean isItMe;//true此条信息是我发送的 false别人发送的
    private Date createTime;//: Date.now(),
    private int contentType;  // 1文字文本 2语音
    private boolean anmitionPlay; //标识音频是否在播放

    private int chatType;// 1 群消息 0 好友消息  2游客消息

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}