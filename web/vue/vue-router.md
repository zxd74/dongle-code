# vue-router重复路由报错
```
vue-router.esm.js:2046  Uncaught (in promise) NavigationDuplicated: Avoided redundant navigation to current location: "/data-manage".
```
1. 在router/index.js重写VueRouter.push方法
```js
import VueRouter from 'vue-router'
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(to) {
    return VueRouterPush.call(this, to).catch(err => err)
}
```
2. 跳转时，校验路由合当前语录是否一致
```js
goto (item) {
  if (this.$route.path !== item.url) {
    this.$router.push({ path: item.url })
  }
}
```
3. 捕捉异常,避免影响使用
```js
this.$router.push(url).catch(err => {
  console.log('输出报错',err)
})
```

# 路由传参
1. 路径path传参方式
```js
{
  "path":"/user:id",
  ...
}
```
2. params传参方式：不修改路由，刷新数据丢失
```js
  // 传参方式
  this.$router.push({"path":"/user","params":{"id":"123456"}})

  // 接收方式
  var id = this.$route.params.id
```
3. query传参方式：直接修改路由，刷新不会丢失
```js
  // 传参方式
  this.$router.push({"path":"/user","query":{"id":"123456"}})

  // 接收方式
  var id = this.$route.query.id
```
