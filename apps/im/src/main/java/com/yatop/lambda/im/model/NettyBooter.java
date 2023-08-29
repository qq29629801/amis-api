package com.yatop.lambda.im.model;

import com.yatop.lambda.im.websocket.initializer.WebSocketServerInitializer;
import com.yatop.lambda.im.websocket.server.WebSocketChatServer;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;

@Table(name = "im_netty_booter", displayName = "websocket")
public class NettyBooter {

    @Id
    private Long id;


    @Service(displayName = "事件", event = true)
    public void onEvent() {
        WebSocketChatServer webSocketChatServer = new WebSocketChatServer(new WebSocketServerInitializer());
        webSocketChatServer.start();
    }
}
