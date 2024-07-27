package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.loader.Loader;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@APP(name = "base", displayName = "基础应用")
@Table(name = "base_module")
public class IrModule extends Model<IrModule> {

    @Id
    private Long id;

    @Column(name = "app_name",label = "应用名")
    @NotBlank
    private String appName;
    @Column(name = "display_name",label = "中文名")
    private String displayName;

    @Column(name = "version",label = "版本")
    private String version;

    // 0 installed  1 uninstall 2 installing 3
    @Column(name = "state", label = "状态")
    private int state;

    @Column(name = "img_url",label = "封面")
    private String imgUrl;

    @Column(name = "type",label = "类型")
    private String type;

    @Column(name = "jar_url",label = "程序包")
    private String jarUrl;


    @OneToMany
    private List<IrDepends> dependsList;


    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;




    @Service
    public Map<String,Object> jarLoad(String jarUrl){
        Application application = new Application();
        Loader.getLoader().build(jarUrl, "com.yatop.lambda", Container.me(), application);
        Map<String,Object> app = new HashMap<>();
        app.put("appName", application.getName());
        app.put("displayName", application.getDisplayName());
        app.put("version", application.getVersion());
        app.put("depends", StringUtils.join(application.getDependencies(), ","));
        app.put("type", application.getTypeEnum().name());
        app.put("jarUrl", jarUrl);
        app.put("state", 0);
        List<IrModule> baseApps = this.search(Criteria.equal("appName", application.getName()),0,1, null);
        if(!baseApps.isEmpty()){
            app.put("id", baseApps.get(0).getLong("id"));
        }
        return app;
    }


    @Service
    public void create(IrModule value){
        List<Map<String,Object>> files = (List<Map<String, Object>>) value.get("file");
        String name = (String) files.get(0).get("name");

        IrModule  module =   selectOne(Criteria.equal("jarUrl", name));
        Application application = new Application();
        Loader.getLoader().install(name, "com.yatop.lambda", Container.me(), application, Context.getInstance());
        if(module == null){
            value.remove("file");
            value.setJarUrl(name);
            value.setDisplayName(application.getName());
            value.setAppName(application.getName());
            value.setVersion("master");
            value.setState(0);
            value.setType(application.getTypeEnum().name());
            value.save();

            for(String depend: application.getDependencies()){
                IrDepends depends = new IrDepends();
                depends.set("baseApp", value.getLong("id"));
                depends.setName(depend);
                depends.save();
            }
        } else {
            value.remove("file");
            value.remove("dependsList");
            value.set("id", module.get("id"));
            value.setJarUrl(name);
            value.setState(0);
            value.setDisplayName(application.getName());
            value.setAppName(application.getName());
            value.setVersion("master");
            value.setType(application.getTypeEnum().name());
            value.update();

            //TODO 修改依赖app
        }



    }

    @Service(displayName = "安装")
    public void install(IrModule metaApp) {
        if (StringUtil.isEmpty(metaApp.getJarUrl())) {
            return;
        }

        Application application = new Application();
        Loader.getLoader().build(metaApp.getJarUrl(), "com.yatop.lambda", Container.me(), application);
        metaApp.setVersion(application.getVersion());
        metaApp.setAppName(application.getName());
        metaApp.setDisplayName(application.getDisplayName());
        metaApp.setState(0);
        metaApp.update();
    }


    @Service(displayName = "卸载")
    public void uninstall(IrModule metaApp) {
        metaApp.setState(1);
        metaApp.update();
        Container.me().remove(metaApp.getAppName());
    }

    public String getVersion() {
        return version;
    }

    public IrModule setVersion(String version) {
        this.set("version", version);
        return this;
    }

    public String getDisplayName() {
        return (String) this.get("displayName");
    }

    public IrModule setDisplayName(String displayName) {
        this.set("displayName", displayName);
        return this;
    }

    public String getAppName() {
        return (String) this.get("appName");
    }

    public IrModule setAppName(String appName) {
        this.set("appName", appName);
        return this;
    }

    public int getState() {
        return (int) this.get("state");
    }

    public IrModule setState(int state) {
        this.set("state", state);
        return this;
    }

    public String getImgUrl() {
        return (String) this.get("imgUrl");
    }

    public IrModule setImgUrl(String imgUrl) {
        this.set("imgUrl", imgUrl);
        return this;
    }

    public String getType() {
        return (String) this.get("type");
    }

    public IrModule setType(String type) {
        this.set("type", type);
        return this;
    }

    public String getJarUrl() {
        return (String) this.get("jarUrl");
    }

    public IrModule setJarUrl(String jarUrl) {
        this.set("jarUrl", jarUrl);
        return this;
    }

    public String getUpdateUser() {
        return (String) this.get("updateUser");
    }

    public IrModule setUpdateUser(String updateUser) {
        this.set("updateUser", updateUser);
        return this;
    }
}
