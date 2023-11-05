package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Table(name = "base_role_user")
public class RoleUser extends Model<RoleUser> {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role roleId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;



    public Long getRoleId(){
        return getLong("roleId");
    }

    public Long getUserId(){
        return getLong("userId");
    }


    @Service(displayName = "授权用户选择")
    public void authUserSelectAll(Long roleId, List<Long> userIds){
        for(Long userId: userIds){
            RoleUser roleUser = new RoleUser();

            Criteria criteria =  new Criteria();
            criteria.and(Criteria.equal("roleId", roleId));
            criteria.and(Criteria.equal("userId", userId));
            Long count = roleUser.count(criteria);
            if(count> 0){
                return;
            }

            roleUser.set("roleId", roleId);
            roleUser.set("userId", userId);
            roleUser.save();
        }
    }

    @Service(displayName = "保存授权角色")
    public void authRole(Long userId, List<Long> roleIds){
        for(Long roleId: roleIds){

            RoleUser roleUser = new RoleUser();

            Criteria criteria =  new Criteria();
            criteria.and(Criteria.equal("roleId", roleId));
            criteria.and(Criteria.equal("userId", userId));
            Long count = roleUser.count(criteria);
            if(count> 0){
                return;
            }

            roleUser.set("roleId", roleId);
            roleUser.set("userId", userId);
            roleUser.save();
        }
    }

    @Service(displayName = "授权用户选择")
    public void authUserCancel(Long roleId, Long userId){
        //TODO 优化代码量
        Criteria criteria =  new Criteria();
        criteria.and(Criteria.equal("roleId", roleId));
        criteria.and(Criteria.equal("userId", userId));

       List<RoleUser> roleUserList = this.search(criteria, 0, 1, null);
       if(!roleUserList.isEmpty()){
           RoleUser roleUser =  roleUserList.get(0);
           roleUser.delete();
       }
    }


    @Service(displayName = "查询授权角色")
    public Map<String,Object> getAuthRole(Long userId){

        User user = new User().findById(userId);

        RoleUser roleUser = new RoleUser();
        List<RoleUser> roleUserList = roleUser.search(Criteria.equal("userId", userId), 0,0, null);
        List<Long> roleIds = roleUserList.stream().map(RoleUser::getRoleId).collect(Collectors.toList());

        Map<String,Object> result = new LinkedHashMap<>();

        result.put("user", user);
        result.put("roleIds", roleIds);
        result.put("list", new Role().search(new Criteria(), 0,0, null));

        return result;
    }

}
