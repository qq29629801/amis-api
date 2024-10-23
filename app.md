# 模块说明

## @APP

标记模块名称，每个JAR取名。



| 属性        | 描述   | 可选值 |
| ----------- | ------ | ---- |
| displayName | 名称   |    聊天  |
| name        | 应用名 |     IM |
| depends        | 依赖 |   base    |

示例代码

```java
@APP(displayName = "通讯服务", name = "im", depends = "net")
```


