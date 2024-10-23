# 服务


@Service

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
