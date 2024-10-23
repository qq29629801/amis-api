# 实体


@Table

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
