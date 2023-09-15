package com.yatop.lambda.net.model;

import com.yatop.lambda.api.common.SpringUtils;
import com.yatop.lambda.net.websocket.initializer.WebSocketServerInitializer;
import com.yatop.lambda.net.websocket.server.WebSocketChatServer;
import com.yuyaogc.lowcode.engine.annotation.APPInfo;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;

@APPInfo(name = "im", displayName = "websocket")
@Table(name = "im_netty_booter")
public class NettyBooter {

    @Id
    private Long id;


    @Service(displayName = "事件", event = true)
    public void onEvent() {
        System.err.println(SpringUtils.context().getStartupDate());

        WebSocketChatServer webSocketChatServer = new WebSocketChatServer(new WebSocketServerInitializer());
        webSocketChatServer.start();
    }
}
