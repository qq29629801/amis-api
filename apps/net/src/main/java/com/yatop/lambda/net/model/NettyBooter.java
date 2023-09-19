package com.yatop.lambda.net.model;

import com.yatop.lambda.api.common.SpringUtils;
import com.yatop.lambda.net.websocket.initializer.WebSocketServerInitializer;
import com.yatop.lambda.net.websocket.server.WebSocketChatServer;
import com.yuyaogc.lowcode.engine.annotation.APPInfo;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.loader.AppTypeEnum;

@APPInfo(name = "net", displayName = "网络通讯模块", type = AppTypeEnum.MODULE)
@Table(name = "im_netty_booter")
public class NettyBooter {
    @Service(displayName = "事件", event = true)
    public void onEvent() {
        System.err.println(SpringUtils.context().getStartupDate());

        WebSocketChatServer webSocketChatServer = new WebSocketChatServer(new WebSocketServerInitializer());
        webSocketChatServer.start();
    }
}
