# 动态组件
1. 动态添加组件
2. 动态组件复制
   1. 采用`v-for`循环自动追加组件


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