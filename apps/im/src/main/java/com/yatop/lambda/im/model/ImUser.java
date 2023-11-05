package com.yatop.lambda.im.model;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.yatop.lambda.im.exception.LoginException;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.annotation.NotBlank;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Table(name = "im_user", parent = "ImBase")
public class ImUser extends Model<ImUser> {
    @Id
    private Long id;


    @Column(name = "login", length = 64)
    @NotBlank
    private String login;


    @Column(name = "password", length = 64)
    @NotBlank
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    public Long getId() {
        return (Long) this.get("id");
    }

    public ImUser setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getLogin() {
        return (String) this.get("login");
    }

    public ImUser setLogin(String login) {
        this.set("login", login);
        return this;
    }

    public String getPassword() {
        return (String) this.get("password");
    }

    public ImUser setPassword(String password) {
        this.set("password", password);
        return this;
    }

    public String getEmail() {
        return (String) this.get("email");
    }

    public ImUser setEmail(String email) {
        this.set("email", email);
        return this;
    }

    public String getPhone() {
        return (String) this.get("phone");
    }

    public ImUser setPhone(String phone) {
        this.set("phone", phone);
        return this;
    }

    @Service(displayName = "登录")
    public Map<String, Object> login(String login, String password) {
        if (StringUtils.isEmpty(login)) {
            throw new LoginException("登录名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw new LoginException("密码不能为空");
        }


        List<ImUser> imUsers = this.search(Criteria.equal("login", login), 0, 1, null);
        if (!imUsers.isEmpty()) {
            ImUser imUser = imUsers.get(0);

            String token = getEntity("ImToken").call("createToken", imUser.getId(), imUser.getLogin(), imUser.getPassword());

            getContext().get("ImRedis").call("set", String.format("redis.user.token.%s", imUser.getId()), token);
        }

        return null;
    }


    @Service(displayName = "注册")
    public void register(ImUser baseUser) {
        String encryptionPassword = getEntity("ImUser").call("createPassword", password);
        baseUser.set("password", encryptionPassword);
    }


    @Service(displayName = "生成密码")
    public String createPassword(String password) {
        // key：AES模式下，key必须为16位
        String key = "1234567812345678";
        // iv：偏移量，ECB模式不需要，CBC模式下必须为16位
        String iv = "1234567812345678";
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv.getBytes());
        // 加密并进行Base转码
        String encrypt = aes.encryptBase64(password);
        return encrypt;
    }

}
