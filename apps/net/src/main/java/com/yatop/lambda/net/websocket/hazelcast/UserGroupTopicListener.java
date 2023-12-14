package com.yatop.lambda.net.websocket.hazelcast;

import com.hazelcast.map.IMap;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;

import javax.cache.Cache;
import java.util.Iterator;

public class UserGroupTopicListener implements MessageListener<MessageBean> {
    @Override
    public void onMessage(Message<MessageBean> message) {
        MessageBean m = message.getMessageObject();
        String groupId = m.getGroupId();
        IMap<String, String> groupUsers = Hazelcast.getInstance().getMap(groupId);

//        Iterator<Cache.Entry<String, String>> iterator = groupUsers.iterator();
//        iterator.forEachRemaining((key) -> {
//            LocalSession local = Users.getInstance().getByUserId(key.getKey());
//            if (null != local) {
//                log.info("receviceGroup : {}", local);
//                Users.getInstance().send(local.getChannel(), m.getPacket());
//            }
//        });
    }
}
