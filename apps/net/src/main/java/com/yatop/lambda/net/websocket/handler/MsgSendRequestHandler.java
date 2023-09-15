package com.yatop.lambda.net.websocket.handler;


import cn.hutool.core.bean.BeanUtil;
import com.yatop.lambda.net.websocket.protocol.packet.MessageSendRequestPacket;
import com.yatop.lambda.net.websocket.protocol.packet.MessageSendResponsePacket;
import com.yatop.lambda.net.websocket.session.Tio;
import com.yatop.lambda.net.websocket.session.Users;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 好友发送通道
 * 保留
 */
@ChannelHandler.Sharable
@Slf4j
public class MsgSendRequestHandler extends SimpleChannelInboundHandler<MessageSendRequestPacket> {

    public static final MsgSendRequestHandler INSTANCE = new MsgSendRequestHandler();
    protected final int GROUP = 1;
    protected final int FRIEND = 0;


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageSendRequestPacket req) throws Exception {
        MessageSendResponsePacket res = new MessageSendResponsePacket();
        BeanUtil.copyProperties(req, res);
        res.setSuccess(true);


        switch (req.getChatType()) {
            case FRIEND:
                Tio.send2B(req.getToUserId(), res);
                Tio.send(ctx, req, res);
                break;
            case GROUP:
                Tio.sendToGroup(req.getToUserId(), req, res);
                break;
            default: {
            }
        }
        log.info("{}", req);
        log.info("发送人  {}", Users.getInstance().get(req.getUserId()));
        log.info("接收人  {}", Users.getInstance().get(req.getToUserId()));
        Tio.send(ctx, req, res);
    }
}
