package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import com.yuyaogc.lowcode.engine.exception.EngineErrorEnum;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.IdWorker;
import com.yuyaogc.lowcode.engine.util.StringUtils;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Table(name = "base_permissions",displayName = "权限")
public class Permissions extends Model<Permissions> {





    @Id
    private Long id;

    @Column(label = "名称")
    private String name;

    @Column(label = "权限码")
    private String auth;





    @Service
    public boolean checkPermissions(String userId, String auth){

       List<Role> roleList = new Role().search(Criteria.equal("userList", userId),0,0,null);
       if(roleList.isEmpty()){
           throw new EngineException(EngineErrorEnum.Authenticator);
       }


        return false;
    }



    @Service
    public void refresh(){
      List<Permissions> permissions =  this.search(new Criteria(), 0, 0,null);
      for(Application app: Container.me().getApps()){
          for(EntityClass entityClass :app.getModels()){

              for(List<EntityMethod> entityMethod: entityClass.getMethods()){
                  if(entityMethod.isEmpty()){
                      continue;
                  }
                  String auth = String.format("%s.%s.%s",app.getName(), entityClass.getName(), entityMethod.get(0).getName());
                  Optional<Permissions> permissionsOptional = permissions.stream().filter(p-> StringUtils.equals(p.getAuth(), auth)).findFirst();
                  Permissions permissions1 = null;
                  if(permissionsOptional.isPresent()){
                      permissions1 = permissionsOptional.get();
                      permissions1.setAuth(auth);
                      permissions1.setName(entityMethod.get(0).getDisplayName());
                      permissions1.update();
                  } else {
                      permissions1 = new Permissions();
                      permissions1.setAuth( auth);
                      permissions1.setName(entityMethod.get(0).getDisplayName());
                      permissions1.save();
                  }
              }


          }
      }



    }




    public Long getId() {
        return (Long) this.get("id");
    }

    public Permissions setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getName() {
        return (String) this.get("name");
    }

    public Permissions setName(String name) {
        this.set("name", name);
        return this;
    }

    public String getAuth() {
        return (String) this.get("auth");
    }

    public Permissions setAuth(String auth) {
        this.set("auth", auth);
        return this;
    }
}
