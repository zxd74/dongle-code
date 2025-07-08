# 注释
1. 注释要描述具体信息
   1. 不要废话
   2. 实在不需要可以删除
```js
let id = 1; // 将id赋值为1 // 错误，不要写废话

// 改为：将{数据|文章}id默认为1
```

# 命名
1. 成员和对象联系密切，可以省略以对象名为前缀命名成员
```js
class User{
    userName;
    userAge;
    userPwd;

    userLogin(){}
    getUserProfile(){}
}

// 改为
class User{
    name;
    age;
    pwd;

    login(){}
    getProfile(){}
}
```

# 代码块
## `if`代码块
1. `if`代码块不要嵌套过深
   * 代码块嵌套过深，可读性差，维护困难
   * 优先过滤无效条件或异常条件，减少嵌套
   * 合并重合条件
   * 提取为方法
```js
if (a) {
    if (b) {
        if (c) {
            if (d) {
                if (e) {
                    // ...逻辑
                }
            }
        }
    }
}
// 改为
if(!a) return;
if(!b) return;
if(!c) return;
if(!d) return;
if(!e) return;
// a true,b true,c true,d true,e true
// ...逻辑
```
2. 当`if`重合条件过多时，可提取条件为方法
```js
if(remaining === 0 || (remaining === 1 && remainPlayers ===1)|| remainPlayers === 0){
    quitGame();
}
// 提取判断为方法
function isGameOver {
    return remaining === 0 || (remaining === 1 && remainPlayers ===1)|| remainPlayers === 0;
}
if(isGameOver()){
    quitGame();
}
```
## lamdba表达式
1. 当表达式只有参数和函数关联时，可省略参数
```js
list.foreach((item) => console.log(item.name));
// 简写
list.foreach(console.log);
```

# 常量值
1. 将无法区分含义的常量值提取为常量或enum，对值进行描述性定义(代表一定含义)
```js
if(state == 1 || state == 2) //...

// 提取为常量
const STATE = {
    INIT: 1,
    SUCCESS: 2,
    FAIL: 3
}
if(state == STATE.INIT || state == STATE.SUCCESS) //...
```

2. **不要硬编码**，提取为常量或变量
```js
function responseInterceptor(resp) {
    const token = resp.headers['Authorization'];
    if (token) {
        store.commit('setToken', token);
    }
}
function requestInterceptor(req) {
    const token = localStorage.getItem('token');
    if (token) {
        req.headers['Authorization'] = token;
    }
}
// 上面两个方法属于隐式关联的硬编码，只要一个该表，另外也得同步改变，不利于维护

// 将硬编码提取为常量
const TOKEN_HEADER = 'Authorization';
function responseInterceptor(resp) {
    const token = resp.headers[TOKEN_HEADER];
    if (token) {
        store.commit('setToken', token);
    }
}
```



# 函数
## 参数
1. 当相同方法参数类型较多时，可使用对象传递参数
```js
const getUserInfo=(name,age,sex,phone)=>{}

// 将参数提取为对象形式
const getUserInfo=(params)=>{
    const {name,age,sex,phone,qq,wechat}=params;
    // ...
}
getUserInfo({name:'张三',age:18,sex:'男',phone:'123456789'}) // 调用
```


# 错误异常处理
1. 不要忽略错误异常，避免报错的空白
   * 有错要有处理，不要忽略错误，报错比不报错强
   * 日志记录，埋点上报，进一步处理等等
