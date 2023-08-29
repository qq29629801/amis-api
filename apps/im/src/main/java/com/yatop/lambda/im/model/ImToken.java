package com.yatop.lambda.im.model;

import com.yatop.lambda.im.util.JWTUtil;
import com.yatop.lambda.im.util.PortalUtil;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Table(name = "im_token")
public class ImToken extends Model<ImToken> {
    @Id
    private Long id;

    @Service(displayName = "创建密钥")
    public String createToken(Long uid, String login, String password) {
        String signToken = JWTUtil.sign(login, String.valueOf(uid), true, password);
        String token = PortalUtil.encryptToken(signToken);
        return token;
    }

    @Service(displayName = "获取用户")
    public Map<String, Object> getUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String decryptToken = PortalUtil.decryptToken(token);
            Map<String, Object> user = JWTUtil.getUser(decryptToken);
            return user;
        }
        return null;
    }

}
