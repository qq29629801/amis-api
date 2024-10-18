# 基于amis后端低代码平台

* 写这个平台是为了解决多年对于项目的困扰，不想碰到新项目就重新来，通过业务模块的积累，能进行模块化安装。
* 新的项目只需要安装模块就能搭建一套完整的业务系统。
* amis-api 的所有基础代码都以模块的形式组合在一起。这些模块可以随时从数据库中安装或卸载。
* 这些模块有两大目的。要么你可以添加新应用/业务逻辑，要么你可以修改已有应用。
* 简言之，amis-api 中的一切都始于模块也终于模块。amis-api 由不同规模的公司所使用，每个公司都有不同的业务流和要求。处理这一问题，amis-api 将应用的功能拆分到了不同的模块中。这些模块可按需在数据库中进行加载。基本上，用户可以在任何时间点启用/禁用这些功能。因此，相同的软件可以按不同的要求进行调整。

## 功能介绍

1. 模组管理
   安装模组，卸载模组
2. 权限管理
   用户管理，角色管理，部门管理
3. 开发管理
   菜单管理，模型管理，视图管理
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



## 从我开始

### 目录结构介绍

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


前端工程是经过amis 打包成js文件

amis 有两种使用方法：

* [JS SDK](https://aisuda.bce.baidu.com/amis/zh-CN/docs/start/getting-started#sdk)，可以用在任意页面中
* [React](https://aisuda.bce.baidu.com/amis/zh-CN/docs/start/getting-started#react)，可以用在 React 项目中

SDK 版本适合对前端或 React 不了解的开发者，它不依赖 npm 及 webpack，可以像 Vue/jQuery 那样外链代码就能使用。


## SDK

下载方式：

1. github 的 [releases](https://github.com/baidu/amis/releases)，文件是 sdk.tar.gz。
2. 使用 `npm i amis` 来下载，在 `node_modules\amis\sdk` 目录里就能找到。

新建一个 hello.html 文件，内容如下：

```html
<!DOCTYPE html>
<html lang="zh">
  <head>
    <meta charset="UTF-8" />
    <title>amis demo</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, maximum-scale=1"
    />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <link rel="stylesheet" href="sdk.css" />
    <link rel="stylesheet" href="helper.css" />
    <link rel="stylesheet" href="iconfont.css" />
    <!-- 这是默认主题所需的，如果是其他主题则不需要 -->
    <!-- 从 1.1.0 开始 sdk.css 将不支持 IE 11，如果要支持 IE11 请引用这个 css，并把前面那个删了 -->
    <!-- <link rel="stylesheet" href="sdk-ie11.css" /> -->
    <!-- 不过 amis 开发团队几乎没测试过 IE 11 下的效果，所以可能有细节功能用不了，如果发现请报 issue -->
    <style>
      html,
      body,
      .app-wrapper {
        position: relative;
        width: 100%;
        height: 100%;
        margin: 0;
        padding: 0;
      }
    </style>
  </head>
  <body>
    <div id="root" class="app-wrapper"></div>
    <script src="sdk.js"></script>
    <script type="text/javascript">
      (function () {
        let amis = amisRequire('amis/embed');
        // 通过替换下面这个配置来生成不同页面
        let amisJSON = {
          type: 'page',
          title: '表单页面',
          body: {
            type: 'form',
            mode: 'horizontal',
            api: '/saveForm',
            body: [
              {
                label: 'Name',
                type: 'input-text',
                name: 'name'
              },
              {
                label: 'Email',
                type: 'input-email',
                name: 'email'
              }
            ]
          }
        };
        let amisScoped = amis.embed('#root', amisJSON);
      })();
    </script>
  </body>
</html>
```


### 配置文件

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
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 1 FROM DUALhibernate-validator
    validationQueryTimeout: 10000
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true
    maxPoolPreparedStatementPerConnectionSize: 20
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
    useGlobalDataSourceStat: true
    filters:
      commons-log.connection-logger-name: stat,wall,log4j
```
