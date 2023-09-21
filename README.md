# lowcode

极大减少了代码量，降低了学习成本，并提升了用户体验。


## 愿景

不想碰到新项目就重新来，通过业务模块的积累，能进行模块化安装。

我可以继承拥有别人的能力，也可以改写别人的能力。

新的项目只需要安装模块就能搭建一套完整的业务系统。

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

  ​		im 聊天应用

  ​		base 基础应用

  ​		net 	网络通信netty

- lowcode 源码

- common 集成

- code 引擎启动

  首次启动注意事项：

  需要将打包后的im-1.0-SNAPSHOT.jar模块移动到项目根目录

  并且创建自己的数据库名 talk-lowcode 表是自动生成的不需要导入表

## 常用注解

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

@Service

标记方法公开

| 属性        | 属性描述           |
| ----------- | ------------------ |
| displayName | 需要展示的中文描述 |
| event       | 模块加载后支持服务 |

@ManyToOne

ManyToOne用于表示多对一的关系，其中“多”表示关系的拥有方，而“一”表示关系的维护方。在ManyToOne关系中，通常是多的一方持有对一的一方的引用，并在数据库中存储对应的外键值。

ManyToOne关系可以通过注解来表达，在Java实体类中使用@ManyToOne注解表示，同时还需要加上@JoinColumn注解，以指定外键列的名称和关联实体。

有两种级联操作：级联保存和级联删除。级联保存表示当保存多的一方时，同时保存对应的所有一的一方对象；级联删除表示当删除一的一方时，同时删除对应的所有多的一方对象。

在@ManyToOne注解中使用cascade属性即可实现级联操作：



Model

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
// 1.跨模块call查询ImUser
List<ImUser> users =  getEntity("ImUser").call("search", new Criteria(), 0, 0, null);
 
// 2.本模型查询ImGroupUser
ImGroupUser imGroupUsers = this.search(new Criteria(), 0, 0, null);

// 3.本模型查询一条
ImGroupUser imGroupUser =  findById(1);
// 4.更新ImGroupUser
 imGroupUser.set("userId",1);
 imGroupUser.update();

// 5.保存ImUser
new ImUser().setLogin("test").save();
```

2.接口参数

```json
{
    "id":"guid",
    "jsonrpc":"2.0",
    "method":"service",
    "params":{
        "args":{
            "criteria":[["groupId.id","=",1]],
            "offset":0,
            "limit":0,
            "order":null
        },
        "app": "im",
        "service": "search",
        "context": {},
        "model": "ImGroupUser",
        "tag": "master"
    }
}
```

**操作符**

```text
=,!=,>,>=,<,<=, 
```

这些就是我们平常用的“等于”，“不等于”，“大于”，“大于等于”，“小于”，“小于等于“。

```python
=?
```

未设置或者等于，未设置就是当值是None或者是False，其余和“=”一样。

```text
=like
```

可以使用模式匹配：下划线“_”匹配一个字符，百分号“%”匹配零或者多个字符。 这里默认的匹配模式是：value（不加其他通配符）。

```text
like
```

通过%value%匹配。 常用于模糊搜索（例如：搜索名字包含“123”的记录）



## 模块服务

模块与模块之间相互调用call的方式

```java
List<ImUser> users =  getEntity("ImUser").call("search", new Criteria(), 0, 0, null);
```

模块与spring调用SpringUtils.getBean的方式

```
SpringUtils.getBean("");
```

Spring与模块调用，使用controller配置的方式

## IDEA模板
get模板
```java
#if($field.modifierStatic)
static ##
#end
$field.type ##
#if($field.recordComponent)
${field.name}##
#else
#set($name = $StringUtil.capitalizeWithJavaBeanConvention($StringUtil.sanitizeJavaIdentifier($helper.getPropertyName($field, $project))))
#if ($field.boolean && $field.primitive)
is##
#else
get##
#end
${name}##
#end
() {
return ($field.type)this.get("$field.name");
}
```
set模板
```java
#set($paramName = $helper.getParamName($field, $project))
#if($field.modifierStatic)
static ##
#end
$classname set$StringUtil.capitalizeWithJavaBeanConvention($StringUtil.sanitizeJavaIdentifier($helper.getPropertyName($field, $project)))($field.type $paramName) {
#if ($field.name == $paramName)
#if (!$field.modifierStatic)
this.##
#else
$classname.##
#end
#end
set("$field.name", $paramName);
return this;
}
```


## 版权申明

参考文献

jFinal odoo  
