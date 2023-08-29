package com.yatop.lambda.im.websocket.protocol.packet;

import com.yatop.lambda.im.websocket.protocol.Packet;
import com.yatop.lambda.im.websocket.protocol.command.Command;
import lombok.Data;

/**
 * 登录响应数据包
 *
 * @author mm
 * @date 2019-04-20
 */
@Data
public class LoginResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
