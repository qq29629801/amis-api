package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;

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

}
