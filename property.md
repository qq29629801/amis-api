# 属性

@Id

标记属性为主键

`@Id`注解用于标记实体类中的哪个属性作为数据库表的主键。每个实体必须有一个主键。

**易错点**：未正确设置主键，或者在实体类中使用了复合主键但未正确配置。

**避免策略**：确保每个实体类至少有一个属性被`@Id`注解，并理解复合主键的正确配置方法。

## @Column

标记表数据库表字段

## @NotBlank

标记创建更新表的时候字段不能为空


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
