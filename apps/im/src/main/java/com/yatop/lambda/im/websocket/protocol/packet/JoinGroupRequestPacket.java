package com.yatop.lambda.im.websocket.protocol.packet;

import com.yatop.lambda.im.websocket.protocol.Packet;
import com.yatop.lambda.im.websocket.protocol.command.Command;
import lombok.Data;

@Data
public class JoinGroupRequestPacket extends Packet {
    private String chatId;
    private int chatType;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }
}
