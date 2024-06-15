package com.yatop.lambda.base.model;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;

import java.util.Objects;

@Table(name = "base_data_seed", isAbstract = true)
public class Seed extends Model<Seed> {


    @Service(event = true)
    public void startUp() {
        User user = new User();
        Role role = new Role();


        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUserName, "admin");
        wrapper.eq(User::getLoginType, "sys_user");
        User user1 = user.selectOne(wrapper);

        if (Objects.isNull(user1)) {
            user.setUserName("admin");
            user.setPassword(BCrypt.hashpw("admin"));
            user.setNickName(IdUtil.fastSimpleUUID());
            user.setLoginType("sys_user");
            user.save();
        }


        Role roleList = role.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleKey,"admin"));
        if (Objects.isNull(roleList)) {
            role.setRoleName("管理员");
            role.setRoleKey("admin");
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
