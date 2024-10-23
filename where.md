# 过滤条件


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
