# 路由无法跳转问题
## vue-router重复路由报错
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
2. 跳转时，校验路由合当前路径是否一致
```js
goto (currentUrl) {
  if (this.$route.path !== currentUrl) {
    this.$router.push({ path: currentUrl })
  }
}
```
3. 捕捉异常,避免影响使用
```js
this.$router.push(url).catch(err => {
  console.log('输出报错',err)
})
```

## 子路由无法跳转问题
1. 子路由组件需要单独设置`<router-view/>`组件：即在`<router-view/>`组件外层再包裹一层，通俗而言就是在父路由对应的组件内增加一个`<router-view/>`
2. 子路由路径配置非全路径而增加了前缀`/`:非全路径配置时，子路径开头无需`/`，主动继承父路径，如`/user/:id`，子路径配置为`user/:id`
3. 其他无法跳转问题同正常路由跳转问题类似
```vue
<!-- App.vue -->
<template>
  <div id="app">
    <router-view/> <!--顶层路由，一级路由才可以使用，即最外层路由配置-->
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>
```
```vue
<!-- Parent.vue -->
<template>
  <div id="parent">
    Parent Content
    <router-view/> <!--关键点：子层路由，子路由可以使用，即路由配置中的childen，router-view在第几层，就需要第几层的路由使用-->
  </div>
</template>

<script>
export default {
  name: 'Parent',
}
</script>
```
```vue
<!-- Child.vue -->
<template>
  <div id="child">
      Child Content
  </div>
</template>

<script>
export default {
  name: 'Child',
}
</script>
```
```js
// router配置
export default new Router({
  routes: [
    {
      path: '/',
      redirect:'/parent',
    },
    {
      path: '/parent',
      name: 'Parent',
      component: Parent,
      children: [
        {
          path: 'child', // 非全路径配置时，子路径开头无需`/`,只需子路径`child`
          // path : '/parent/child', // 也可以配置全路径
          name: 'Child',
          component: Child,
        },
      ],
    },
  ],
})
```

# 路由配置
## 路由传参
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
