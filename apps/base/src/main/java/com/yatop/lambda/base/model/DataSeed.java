package com.yatop.lambda.base.model;

import cn.dev33.satoken.secure.BCrypt;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.ConfigUtils;
import com.yuyaogc.lowcode.engine.util.StringUtils;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Table(name = "base_data_seed")
public class DataSeed extends Model<DataSeed> {

    @Service(event = true)
    public void startUp() {
        User user = new User();
        getContext().get("base.User");

        LambdaQueryWrapper wrapper = new LambdaQueryWrapper();
        wrapper.eq("userName","admin");
        wrapper.eq("password","1");

        List<User> userList = user.search(Criteria.equal("userName", "admin"), 0, 1, null);
        if (userList.isEmpty()) {
            user.set("userName", "admin");
            user.set("password", BCrypt.hashpw("admin"));
            user.set("nickName", "yuYaoGc");
            user.set("loginType", "sys_user");
            user.save();
        }
        Role role = new Role();
        getContext().get("base.Role");
        List<Role> roleList = role.search(Criteria.equal("roleKey", "admin"), 0, 1, null);
        if (roleList.isEmpty()) {
            role.set("roleName", "管理员");
            role.set("roleKey", "admin");
            role.save();
        }
        if (Objects.nonNull(user.getId()) && Objects.nonNull(role.getId())) {
            RoleUser roleUser = new RoleUser();
            roleUser.set("userId", user.getId());
            roleUser.set("roleId", role.getId());
            roleUser.save();
        }
    }
}
