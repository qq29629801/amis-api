package com.yatop.lambda.base.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.annotation.validate.NotBlank;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.loader.Loader;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Table(name = "base_app")
public class BaseApp extends Model<BaseApp> {

    @Id
    private Long id;

    @Column(name = "app_name")
    @NotBlank
    private String appName;
    @Column(name = "display_name")
    private String displayName;

    @Column(name = "version")
    private String version;

    @Column(name = "state")
    private int state;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "type")
    private String type;

    @Column(name = "jar_url")
    private String jarUrl;

    private String depends;

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
        app.put("depends", application.getDependencies());
        app.put("type", application.getTypeEnum().name());
        return app;
    }


    @Service(displayName = "安装")
    public void install(BaseApp metaApp) {
        if (StringUtil.isEmpty(metaApp.getJarUrl())) {
            return;
        }

        Application application = new Application();
        Loader.getLoader().build(metaApp.getJarUrl(), "com.yatop.lambda", Container.me(), application);
        metaApp.setVersion(application.getVersion());
        metaApp.setAppName(application.getName());
        metaApp.setDisplayName(application.getDisplayName());
        metaApp.setType("已安装");
        metaApp.update();
    }


    @Service(displayName = "卸载")
    public void uninstall(BaseApp metaApp) {
        Container.me().remove(metaApp.getAppName());
        metaApp.setType("卸载");
        metaApp.update();
    }


    public String getVersion() {
        return version;
    }

    public BaseApp setVersion(String version) {
        this.set("version", version);
        return this;
    }

    public String getDisplayName() {
        return (String) this.get("displayName");
    }

    public BaseApp setDisplayName(String displayName) {
        this.set("displayName", displayName);
        return this;
    }

    public String getAppName() {
        return (String) this.get("appName");
    }

    public BaseApp setAppName(String appName) {
        this.set("appName", appName);
        return this;
    }

    public int getState() {
        return (int) this.get("state");
    }

    public BaseApp setState(int state) {
        this.set("state", state);
        return this;
    }

    public String getImgUrl() {
        return (String) this.get("imgUrl");
    }

    public BaseApp setImgUrl(String imgUrl) {
        this.set("imgUrl", imgUrl);
        return this;
    }

    public String getType() {
        return (String) this.get("type");
    }

    public BaseApp setType(String type) {
        this.set("type", type);
        return this;
    }

    public String getJarUrl() {
        return (String) this.get("jarUrl");
    }

    public BaseApp setJarUrl(String jarUrl) {
        this.set("jarUrl", jarUrl);
        return this;
    }

    public String getUpdateUser() {
        return (String) this.get("updateUser");
    }

    public BaseApp setUpdateUser(String updateUser) {
        this.set("updateUser", updateUser);
        return this;
    }
}
