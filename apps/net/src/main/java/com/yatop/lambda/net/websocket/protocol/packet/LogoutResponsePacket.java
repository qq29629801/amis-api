package com.yatop.lambda.net.websocket.protocol.packet;

import com.yatop.lambda.net.websocket.protocol.Packet;
import com.yatop.lambda.net.websocket.protocol.command.Command;
import lombok.Data;

/**
 * @author mm
 * @date 2019-04-21
 */
@Data
public class LogoutResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.LOGOUT_RESPONSE;
    }
}