# 基于amis后端低代码平台

* 写这个平台是为了解决多年对于项目的困扰，不想碰到新项目就重新来，通过业务模块的积累，能进行模块化安装。
* 新的项目只需要安装模块就能搭建一套完整的业务系统。
* amis-api 的所有基础代码都以模块的形式组合在一起。这些模块可以随时从数据库中安装或卸载。
* 这些模块有两大目的。要么你可以添加新应用/业务逻辑，要么你可以修改已有应用。
* 简言之，amis-api 中的一切都始于模块也终于模块。amis-api 由不同规模的公司所使用，每个公司都有不同的业务流和要求。处理这一问题，amis-api 将应用的功能拆分到了不同的模块中。这些模块可按需在数据库中进行加载。基本上，用户可以在任何时间点启用/禁用这些功能。因此，相同的软件可以按不同的要求进行调整。

## 功能介绍

1. 模组管理
   安装模组，卸载模组

   ![1729260875350](images/README/1729260875350.png)
2. 权限管理
   用户管理，角色管理，部门管理
   ![1729260898811](images/README/1729260898811.png)
3. 开发管理
   菜单管理，模型管理，视图管理

   ![1729260908052](images/README/1729260908052.png)
4. 系统管理

## 版权声明

ORM参照Jfinal 框架  独创Db + Record模式，灵活便利 ActiveRecord支持，使数据库开发极致快速 [https://gitee.com/jfinal/jfinal.git](https://)

Odoo 是一个模块化系统，所有的业务功能都是由**模块**(module)提供的。

[https://gitee.com/mirrors/odoo.git](https://)

## 版权信息

软件遵循[MIT](https://baike.baidu.com/item/MIT/10772952)开源协议，意味着您无需支付任何费用，也无需授权，即可将 软件应用到您的产品中。
注意：这并不意味着您可以将软件应用到非法的领域，比如涉及赌博、色情、暴力、宗教等方面。
如因此产生纠纷等法律问题， 作者不承担任何责任。切勿以身试法!!! 网络不是法外之地
声明：软件仅供学习交流，严禁用于商业用途。

## 小白手册

### 目录介绍

amis-api

-apps 应用模块

```
--base 基础模块
```

```
--im 聊天模块
```

```
--net weboscket服务
```

-lowcode 引擎

-service 启动

-- resource 资源文件

```
-- templates 静态资源
```

前端工程是经过amis 打包成js SDK文件

amis 有两种使用方法：

* [JS SDK](https://aisuda.bce.baidu.com/amis/zh-CN/docs/start/getting-started#sdk)，可以用在任意页面中
* [React](https://aisuda.bce.baidu.com/amis/zh-CN/docs/start/getting-started#react)，可以用在 React 项目中

SDK 版本适合对前端或 React 不了解的开发者，它不依赖 npm 及 webpack，可以像 Vue/jQuery 那样外链代码就能使用。

```###

```java
lambda-portal:
  #数据源配置
  db-datasource:
    name: lambda-portal
    url: jdbc:mysql://127.0.0.1:3306/lowcode?serverTimezone=GMT%2B8&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useAffectedRows=true&useSSL=false
    username: 账号
    password: 密码
    driverClassName: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.xa.DruidXADataSource
    initialSize: 5
    minIdle: 5
    maxActive: 500
    maxWait: 60000
  
```

### 

# 注解

基础注解

在**Java**开发中，Java Persistence **API** (JPA) 是一个用于管理关系[数据库](https://cloud.tencent.com/solution/database?from_column=20065&from=20065)的对象关系映射 (**ORM**) 框架，它简化了数据访问层的编写。JPA通过一系列注解来定义实体类与数据库表之间的映射关系, `@Table`, `@Id`是最基础且常用的几个注解。本文将深入浅出地介绍这些注解的用法、常见问题、易错点及避免策略，并附上代码示例。

## @APP

标记模块名称，每个JAR取名。

示例代码

```java
@APP(displayName = "通讯服务", name = "im", depends = "net")
```

## @Table

标记表名信息，以及Entity实体信息。

示例代码

```java
@Table(name = "im_group", displayName = "群组")
public class Group extends Model<Group> {
    @Id
    private Long id;

    @Column(label = "群组名称")
    private String name;

    @File(label = "群组头像")
    private String avatarUrl;

}
```

## @Id

标记属性为主键

`@Id`注解用于标记实体类中的哪个属性作为数据库表的主键。每个实体必须有一个主键。

**易错点**：未正确设置主键，或者在实体类中使用了复合主键但未正确配置。

**避免策略**：确保每个实体类至少有一个属性被`@Id`注解，并理解复合主键的正确配置方法。

## @Column

标记表数据库表字段

## @NotBlank

标记创建更新表的时候字段不能为空

## @Service

标记方法公开


| 属性        | 属性描述           |
| ----------- | ------------------ |
| displayName | 需要展示的中文描述 |
|             |                    |
| event       | 模块加载后支持服务 |

示例代码

```java
@Service
    public Map<String,Object> login(String login, String password){
        Map<String,Object> result = new HashMap<>();

        //TODO LOGIN
        User user =  this.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName,login));
        if(null == user){
            return Collections.EMPTY_MAP;
        }

        //TODO SIGN TOKEN
        String sign = JWTUtil.sign(login, String.valueOf(user.getId()), true, password);
        String token = PortalUtil.encryptToken(sign);
        result.put("token", token);

        return result;
    }
```

## @ManyToOne

ManyToOne用于表示多对一的关系，其中“多”表示关系的拥有方，而“一”表示关系的维护方。在ManyToOne关系中，通常是多的一方持有对一的一方的引用，并在数据库中存储对应的外键值。

ManyToOne关系可以通过注解来表达，在Java实体类中使用@ManyToOne注解表示，同时还需要加上@JoinColumn注解，以指定外键列的名称和关联实体。

有两种级联操作：级联保存和级联删除。级联保存表示当保存多的一方时，同时保存对应的所有一的一方对象；级联删除表示当删除一的一方时，同时删除对应的所有多的一方对象。

在@ManyToOne注解中使用cascade属性即可实现级联操作：

示例代码

```java
@ManyToOne
    @JoinColumn(name = "model_id")
    private IrModel irModel;
```

## @ManyToMany

11

示例代码

```java
@ManyToMany
    @JoinTable(name = "base_role_user",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;
```

## @OneToMany

222

示例代码

```java
@OneToMany
    private List<IrField> fieldList;
```

## Validator参数校验常用注解

除了前四个 @Null，@ NotNull，@ NotBlank，@NotEmpty外，其他所有的注解，传 null 时都会被当作有效处理

注解常用参数值：message(校验不通过反馈信息)

**JSR303定义的基础校验类型：**


| 注解                     | 验证的数据类型                 | 备注                                                                                                                                        |
| ------------------------ | ------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------- |
| Null                     | 任意类型                       | 参数值必须是 Null                                                                                                                           |
| NotNull                  | 任意类型                       | 参数值必须不是 Null                                                                                                                         |
| NotBlank                 | 只能作用于字符串               | 字符串不能为 null，而且字符串长度必须大于0，至少包含一个非空字符串                                                                          |
| NotEmpty                 | CharSequenceCollectionMapArray | 参数值不能为null，且不能为空（字符串长度必须大于0，空字符串（“ ”）可以通过校验）                                                          |
| Size(min,max )           | CharSequenceCollectionMapArray | 字符串：字符串长度必须在指定的范围内Collection：集合大小必须在指定的范围内Map：map的大小必须在指定的范围内Array：数组长度必须在指定的范围内 |
| Pattern(regexp)          | 字符串类型                     | 验证字符串是否符合正则表达式                                                                                                                |
| Min(value)               | 整型类型                       | 参数值必须大于等于 最小值                                                                                                                   |
| Max(value)               | 整型类型                       | 参数值必须小于等于 最大值                                                                                                                   |
| DecimalMin(value)        | 整型类型                       | 参数值必须大于等于 最小值                                                                                                                   |
| DecimalMax(value)        | 整型类型                       | 参数值必须小于等于 最大值                                                                                                                   |
| Positive                 | 数字类型                       | 参数值为正数                                                                                                                                |
| PositiveOrZero           | 数字类型                       | 参数值为正数或0                                                                                                                             |
| Negative                 | 数字类型                       | 参数值为负数                                                                                                                                |
| NegativeOrZero           | 数字类型                       | 参数值为负数或0                                                                                                                             |
| Digits(integer,fraction) | 数字类型                       | 参数值为数字，且最大长度不超过integer位，整数部分最高位不超过fraction位                                                                     |
| AssertTrue               | 布尔类型                       | 参数值必须为 true                                                                                                                           |
| AssertFalse              | 布尔类型                       | 参数值必须为 false                                                                                                                          |
| Past                     | 时间类型(Date)                 | 参数值为时间，且必须小于 当前时间                                                                                                           |
| PastOrPresent            | 时间类型(Date)                 | 参数值为时间，且必须小于或等于 当前时间                                                                                                     |
| Future                   | 时间类型(Date)                 | 参数值为时间，且必须大于 当前时间                                                                                                           |
| FutureOrPresent          | 时间类型(Date)                 | 参数值为时间，且必须大于或等于 当前日期                                                                                                     |
| Email                    | 字符串类型                     | 被注释的元素必须是电子邮箱地址                                                                                                              |

**Validator 中附加的 constraint ：**


| 注解   | 验证的数据类型     | 备注                                             |
| ------ | ------------------ | ------------------------------------------------ |
| Length | 字符串类型         | 字符串的长度在min 和 max 之间                    |
| Range  | 数字类型字符串类型 | 数值或者字符串的值必须在 min 和 max 指定的范围内 |

**Pattern注解校验 常用正则表达式**

```java
@Pattern(regexp = "^[1-9]]\\d*$", message = "XX参数值必须是正整数")
```

## 过滤条件

### 条件构造器

保留了 MyBatis-Plus 强大的条件构造器（Wrapper），用于构建复杂的数据库查询条件。Wrapper 类允许开发者以链式调用的方式构造查询条件，无需编写繁琐的 SQL 语句，从而提高开发效率并减少 SQL 注入的风险。

在 MyBatis-Plus 中，Wrapper 类是构建查询和更新条件的核心工具。以下是主要的 Wrapper 类及其功能：

* **AbstractWrapper**：这是一个抽象基类，提供了所有 Wrapper 类共有的方法和属性。它定义了条件构造的基本逻辑，包括字段（column）、值（value）、操作符（condition）等。所有的 QueryWrapper、UpdateWrapper、LambdaQueryWrapper 和 LambdaUpdateWrapper 都继承自 AbstractWrapper。

#### 示例

**普通 Wrapper (`QueryWrapper`)**：

**Lambda Wrapper (`LambdaQueryWrapper`)**：

```
LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();lambdaQueryWrapper.allEq(Map.of("id", 1, "name", "老王", "age", null));
```

**带过滤器的普通 Wrapper (`QueryWrapper`)**：

```
QueryWrapper<User> queryWrapper = new QueryWrapper<>();queryWrapper.allEq((field, value) -> field.contains("a"), Map.of("id", 1, "name", "老王", "age", null));
```

**带过滤器的 Lambda Wrapper (`LambdaQueryWrapper`)**：

```
LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();lambdaQueryWrapper.allEq((field, value) -> field.contains("a"), Map.of("id", 1, "name", "老王", "age", null));
```

## eq

`eq` 方法是框架中用于构建查询条件的基本方法之一，它用于设置单个字段的相等条件。

#### 方法签名

```
// 设置指定字段的相等条件eq(R column, Object val)
// 根据条件设置指定字段的相等条件eq(boolean condition, R column, Object val)
```

#### 参数说明

* `column`：数据库字段名或使用 `Lambda` 表达式的字段名。
* `val`：与字段名对应的值。
* `condition`：一个布尔值，用于控制是否应用这个相等条件。

#### 示例

**Lambda Wrapper (`LambdaQueryWrapper`)**：

```
LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();lambdaQueryWrapper.eq(User::getName, "老王");
```
