# 跨域问题
    只有在浏览器中才会由同源校验，其它情况不存在，可以直接访问或请求。
## 前端解决方案
### 使用JSONP，是协议
JSONP，是 JSON with Padding 的简称，它是 json 的一种补充使用方式，利用 script 标签来解决跨域问题。JSONP 是非官方协议，他只是前后端一个约定，如果请求参数带有约定的参数，则后台返回 javascript 代码而非 json 数据，返回代码是函数调用形式，函数名即约定值，函数参数即要返回的数据。
```js

const hander = (data)=>{console.log(data)}
const url = "http://localhsot:8888/jsonp?calllback=handler" // 指定callback
var script = document.createElement("script")
script.setAttribute("src",url)
document.getElementByTagName('head')[0].appendChildren(script)
```
* 只能发送Get请求
* 非XHR请求，许多依赖XHR请求的事件无法处理
* 服务端需要同步改动

### 打破浏览器限制
以Chrome为例，增加参数 `--disable-web-security` 关闭安全检查，同时关闭跨域安全检查
```txt
Chrome.exe/ --args --disable-web-security
```
### 设置跨域请求头
```
Access-Control-Allow-Origin: *   # 允许跨域请求
Access-Control-Allow-Credentials: true  # 接收响应 
```
## 后端解决方案
## 1. 前端支持跨域处理
'''

'''

## 3. 后端设置支持跨域处理(java+spring)
```java

```

```