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

## 动态路由配置
**思路**
1. 路由页面要有一定的约定(约定大于配置)：
   - 如页面为目录，`page.js`为页面的元数据，`index.vue`为页面Module
2. 使用编译插件读取目录文件(`page.js,index.vue`)
   - **webpack**使用`require.context`
   - **vite**使用`import.meta.glob`
3. 遍历读取到的文件列表，并根据元数据和Module生成路由配置

### webpack
* `require.context`方法：读取目录文件
* 结果处理
  * `files.keys()`：获取文件列表(**不包含输入的目录**,以`./`开头)
  * `files(path)`: 将整个文件作为Module
  * `files(path).default`：获取文件`export default`的Module
* `()=>import()`：动态加载Module,只能传入路径，不能传入Module
* `component:IndexView`等价于`files(path).default`
```js
import {createRouter,createWebHistory} from 'vue-router'

/* eslint-disable */
// const comps = require.context('@/views', true, /\/index\.vue$/)
// comps(compath).default
const pages = require.context('@/views', true, /\/page\.js$/)
export const routes = pages.keys().map(path => {
    var compath = path.replace('/page.js','/index.vue'));
    const meta = pages(path).default
    var path = path.replace('./', '/').replace('/page.js', '')||'/';
    const name = path.split('/').filter(Boolean).join('-')||'index';
    return {
        path,
        name,
        component:()=>import(`@/views${compath.substring(1)}`),
        // component: comps(compath).default, 等价于 component: IndexView，直接绑定export default
        meta
    }
})

export default createRouter({
    'history': createWebHistory(),
    'base': '/',
    routes
})

```
### Vite
Vite通过`import.meta.glob`可以配置直接读取文件为Module
```js
import {createRouter,createWebHistory} from 'vue-router'

/* eslint-disable */
const comps =import.meta.glob('./views/**/index.vue')
const pages = import.meta.glob('./views/**/page.js',{
    eager: true,import: 'default',
})

export const routes = Object.entries(pages).map(([path,meta]) => {
    var compath = path.replace('/page.js','/index.vue');
    var path = path.replace('./views', '').replace('/page.js', '')||'/';
    const name = path.split('/').filter(Boolean).join('-')||'index';
    const component = comps[compath];
    console.log({path,name,component,meta})
    return {path,name,component,meta: meta}
})

export default createRouter({
    'history': createWebHistory(),
    'base': '/',
    routes,
})
```

## 路由页面可以跳转，但地址栏无法访问
