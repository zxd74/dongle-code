# 跨域处理（vue）
1. 安装axios（略）
2. 引入axios，配置main.js(可省，需要时引入)
```js
import axios from 'axios'
Vue.prototype.$axios = axios
```
3. 修改配置config/index.js
```js
module.exports = {
  dev: {
    proxyTable: {     //axios跨域处理
      '/api': {       //接口前缀，自定义，如果实际没有，则需要将其重写置空
        target:'http://localhost:8888/', // 接口服务地址
        changeOrigin:true, //允许跨域  关键
        pathRewrite:{
          '^/api': '' // 若非api前缀需要置空
        }
      }
    }
  }
}
```
4. 使用方式
```js
this.$axios(
    {
        method: 'post',
        url: '/api/data/all' // 此处是直接以/api前缀开头，与代理配置拦截一致，实际会自动访问target(http://localhost:8888/)，若api非需要，则会变成访问 http://localhost:8888/data/all
    })
.then(function (res) {console.log(res);})
.catch(function (err) {console.log(err);})
```
