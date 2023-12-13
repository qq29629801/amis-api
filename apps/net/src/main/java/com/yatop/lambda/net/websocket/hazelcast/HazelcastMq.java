package com.yatop.lambda.net.websocket.hazelcast;

import com.yatop.lambda.net.websocket.protocol.Packet;

public class HazelcastMq {

    public static final String userid_topc = "userId_topc";
    public static final String group_topc = "group_topc";


    private void receviceUser() {

    }

    private void receviceGroup() {

    }

    public void send2B(String toUserId, Packet packet) {

        MessageBean messageBean = new MessageBean();
        messageBean.setPacket(packet);
        messageBean.setToUserId(toUserId);

    }


    public void sendToGroup(String groupId, String userId, Packet packet) {
        MessageBean messageBean = new MessageBean();
        messageBean.setPacket(packet);
        messageBean.setUserId(userId);
        messageBean.setGroupId(groupId);
    }

}
