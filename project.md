# 项目说明


## 目录介绍

amis-api


| 模块    | 说明        | 进度 |
| ------- | ----------- | ---- |
| base    | 基础模块    | 90%  |
| im      | 聊天软件    | 90%  |
| net     | websocket   | 100% |
| demo    | demo模块    | 90%  |
| lowcode | 引擎源代码  | 90%  |
| service | spring boot | 100% |


## 工程配置文件


```
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