# lowcode

低代码平台

## 功能介绍

支持热部署，一键部署发布。

支持模块化安装，需要使用什么业务就安装什么业务。

支持属性服务的继承、扩展。

继承，我可以拥有别人的属性，服务。

扩展，我可以改写别人的服务，从而使用我的服务。

支持注解自动生成CURD的能力。

## 快速上手

代码结构介绍

- apps 模块代码

  ​		im 通讯应用

  ​		base 基础应用

- lowcode 源码

- code 引擎启动

  首次启动注意事项：

  需要将打包后的im-1.0-SNAPSHOT.jar模块移动到项目根目录

  并且创建自己的数据库名 talk-lowcode 表是自动生成的不需要导入表

## 注解

@APPInfo

标记模块名称，每个JAR取名。

@Table

标记表名信息，以及Entity实体信息。

@Id

标记属性为主键

@Column

标记表数据库表字段

@NotBlank

标记创建更新表的时候字段不能为空

## 方法

1.基本用法

  Model是ActiveRecord中最重要的组件之一，它充当MVC模式中的Model部分。以下是Model定义示例代码：

```    Model是ActiveRecord中最重要的组件之一，它充当MVC模式中的Model部分。以下是Model定义示例代码：
@Table(name = "im_user")
public class ImUser extends Model<ImUser> {

}
```

  以上代码中的User通过继承Model，便立即拥有的众多方便的操作数据库的方法。在User中声明的dao静态对象是为了方便查询操作而定义的，该对象并不是必须的。基于ActiveRecord的Model无需定义属性，无需定义getter、setter方法，无需XML配置，无需Annotation配置，极大降低了代码量。

  以下为Model的一些常见用法：

```java
List<ImUser> users =  getEntity("ImUser").call("search", new Criteria(), 0, 0, null);
     
ImGroupUser imGroupUser =  findById(1);
 imGroupUser.set("userId",1);
 imGroupUser.update();

new ImUser().setLogin("test").save();
```

