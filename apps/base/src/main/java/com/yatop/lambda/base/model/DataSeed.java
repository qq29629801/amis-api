package com.yatop.lambda.base.model;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.Objects;

@Table(name = "base_data_seed")
public class DataSeed extends Model<DataSeed> {


    @Service(event = true)
    public void startUp() {
        User user = new User();

        getContext().get("base.User");

        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUserName, "admin");
        wrapper.eq(User::getLoginType, "sys_user");
        User user1 = user.selectOne(wrapper);

        if (Objects.nonNull(user1)) {
            user.setUserName("admin");
            user.setPassword(BCrypt.hashpw("admin"));
            user.setNickName(IdUtil.fastSimpleUUID());
            user.setLoginType("sys_user");
            user.save();
        }
        Role role = new Role();
        getContext().get("base.Role");

        Role roleList = role.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleKey,"admin"));
        if (Objects.nonNull(roleList)) {
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
