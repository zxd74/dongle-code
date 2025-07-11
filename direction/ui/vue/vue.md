- [创建项目](#创建项目)
  - [项目模板](#项目模板)
    - [离线模板](#离线模板)
- [动态组件](#动态组件)
- [通信](#通信)
  - [组件参数传递](#组件参数传递)
- [绑定](#绑定)
  - [组件事件绑定](#组件事件绑定)
  - [数据绑定](#数据绑定)
  - [事件绑定](#事件绑定)
  - [css样式绑定](#css样式绑定)
    - [方式一 style具体属性绑定](#方式一-style具体属性绑定)
    - [方式二 style对象绑定](#方式二-style对象绑定)
    - [方式三 自定义style元素组件](#方式三-自定义style元素组件)
    - [方式四 原生全局变量](#方式四-原生全局变量)
    - [方式五 原生局部变量](#方式五-原生局部变量)
- [打包配置](#打包配置)
  - [webpack](#webpack)
  - [Vite](#vite)

# 创建项目
* 创建空白项目`vue create`
```bash
vue create demo
# -n 忽略.git初始化，默认会初始化.git目录
```
* 创建模板项目`vue init`
```bash
# vue init [options] <template> <project-name>
vue init webpack demo # 如webpack
# -c --clone 使用git clone方式克隆template
# --offline 使用本地模板
```
* 第三方工具创建：如[`npm` + `vie`](#vite)
```bash
npm create vite demo #--template vue
```
* **手动创建**
  * 创建文件
```txt
/demo           -- 项目根目录
  /src              -- 源码目录
    /assets             -- 静态资源(可选)
    /components         -- 公共组件(可选)
    /views              -- 页面组件(可选)
    App.vue             -- 主组件
    main.js             -- 入口文件，创建Vue(v2.x)或App(v3.x)
  package.json      -- 负责包管理：依赖，入口，脚本等
  vue.config.json   -- 负责项目配置(可选)：打包，代理，路径等,其打包工具，类似，如vite：vite.config.js
  README.md         -- 项目说明
```
## 项目模板
* 可在[Github](https://github.com/vuejs-templates)查看官方支持的模板：webpack, pwa, simple
  * 其它模板可以自定义或有第三方提供
  * 亦可以由第三方命令自身支持，如vite
* **模板存储**路径`C:\Users\<UserName>\.vue-templates`

### 离线模板
* 每次创建模板项目时都会从远程仓库拉取模板，可以通过`--offline`参数指定本地模板
* **默认本地模板**路径为`C:\Users\<UserName>\.vue-templates`
  * 可以根据需要变更
```bash
vue init --offline webpack demo
# 提示 > Use cached template at ~\.vue-templates\webpack
```

# 动态组件
1. 动态添加组件
2. 动态组件复制
   1. 采用`v-for`循环自动追加组件
   ```vue
   <div v-for="item in data" v-key="item.key" v-value="item.value"></div>
   ```

# 通信
## 组件参数传递
1. 在`<script setup>`组件中，可以使用 `defineProps()`声明
2. 在非`<script setup>`组件中使用`props` 定义接收参数
3. `v-bind:`属性或直接属性传递参数
```vue
<template>
  <div>
    {{name}},{{age}}
  </div>
</template>
<script setup>
const props = defineProps(['sex'])
</script>
<script>
export default{
  props:{
    "name":String,
    "age":{
      type:Number,
      defaultValue:10,//可以是具体值也可以是函数
    }
  }
}
</script>
```
```vue
<!--动态绑定v-bind,静态绑定直接使用属性名，当变量合组件属性冲突时请使用动态绑定-->
<template>
  <demo v-bind:name="" age="" sex=""><demo> 
</template>
<script>
import Demo from '@/components/Demo'
export default{
  components:{Demo}
}
</script>
```


# 绑定
## 组件事件绑定
1. 通过`$emit`定义组件事件
```vue
<!-- $emit定义内建事件 -->
<button v-on:click="$emit('enlarge-text')"/>
```
```vue
<!-- v-on绑定事件 -->
<blog-post v-on:enlarge-text="postFontSize += 0.1"></blog-post>
```

## 数据绑定
1. 单向绑定`{{}}`
2. 双向绑定`v-model`
```vue
<template>
  <div>
    <input  v-model="data">
    {{data}}
  </div>
</template>
<script>
export default{
  data(){
    return{
      data:"Dongle"
    }
  }
}
</script>
```
## 事件绑定
1. `v-on`绑定
2. `@`绑定
```vue
<template>
  <div>
    <button type="button" v-on:click="clickButton">
    <button type="button" @click="clickButton">
  </div>
</template>
<script>
export default{
  methods:{
    clickButton(){}
  }
}
</script>
```
## css样式绑定
1. 方式一: 直接在元素上绑定具体样式
2. 方式二: 定义属性对象,绑定到style,可以在style中使用定义的变量
3. 方式二: 通过引入自定义组件引入`style`数据,直接绑定样式
4. 方式四: css原生定义`:root`全局变量
5. 方式五: css原生局部变量,仅可以在选择器内部定义,原生页面局部变量`element`不行

```vue
<template>
    <div class="header">
        Header
    </div>
</template>
<script>
export default {
    name: "Header",
    data() {
        return {
            className:"header"
            color: "red",
            changeColor:"#ff0000",
            testStyle:{
                "--color":'yellow'
            }
        };
    },
};
</script>
```

### 方式一 style具体属性绑定
```vue
  <div class="header" :style="'color:'+ color"></div>
```

### 方式二 style对象绑定
```vue
<div class="header" :style="testStyle"></div>

<style>
    .header{
      color:var(--color)
    }
</style>
```

### 方式三 自定义style元素组件
```vue
<template>
    <div class="header">
        Header
        <!-- // 增加自定义组件,指定元素内容为style -->
        <!-- <v-style>
            .header{
                color: {{ color }};
            }
        </v-style> -->
        <component is="style">
            .header{
                color: {{ color }};
            }
        </component>
    </div>
</template>
<script> 
// 等同上面的 <component is="style"></component>
// import Vue from 'vue';
// // 增加自定义组件,指定元素内容为style
// Vue.component('v-style', {
//   render: function (createElement) {
//     return createElement('style', this.$slots.default)
//   }
// });
export default {
    name: "Header",
    data() {
        return {
            className:"header",
            color: "red",
            changeColor:"#ff0000",
            testStyle:{
                "--color":'yellow'
            }
        };
    },
};
</script>
```

### 方式四 原生全局变量
```vue
<style>
:root{
  --header-color:red;
}
.header{
  color:var(--header-color)
}
</style>
```

### 方式五 原生局部变量
```vue
<style>
element{ /* 注意:当前页面的局部变量无效 */
  --header-color:red; 
}
.header{
  --size:5px; /* 个人感觉意义不大 */
  width:var(--size * 5);
  font-size:var(--size);
}
</style>
```
# 打包配置
## webpack
**Vue2.x官方推荐构建工具**。

1. 安装`webpack`相关依赖
```bash
yarn add webpack webpack-cli webpack-dev-server webpack-merge -D # 依各自环境使用命令
```
2. 配置`webpack.config.js`
   - `entry`: 入口文件
   - `output`: 输出文件
   - `module`: 模块配置
     - `rules`: 规则配置,不同文件后缀的加载器，如:
       - `vue`使用`vue-loader`
       - `js`使用`babel-loader`
       - `css`使用`style-loader`和`css-loader`
   - `plugins`: 插件配置,需额外安装插件
     - `HtmlWebpackPlugin` : 生成html文件的模板
   - `resolve`: 解析配置
     - `alias`: 别名配置,如`@`指向`src`目录
```js
const path = require('path')
const { VueLoaderPlugin } = require('vue-loader')
const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
  entry: './src/main.js',
  output: {
    path: path.resolve(__dirname, 'dist'),
    // filename: 'bundle.js',
    clean: true,
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'vue-loader'
      },
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: 'babel-loader'
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      }
    ]
  },
  plugins: [
    new VueLoaderPlugin(),
    new HtmlWebpackPlugin({
      template: './public/index.html'
    })
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    },
    extensions: ['.js', '.vue']
  }
}

```
3. 配置`package.json`脚本
   - 默认是`vue-cli-service`命令执行脚本
   - 可以使用`webpack`命令执行脚本(需安装`webpack-cli`)
```json
"scripts": {
    "dev:webpack": "webpack serve --mode development",
    "build:webpack": "webpack --mode production",
    "preview": "http-server ./dist -p 8888",
    "help:webpack":"webpack --help",
    ...
},
```

## Vite
**Vue3.x官方推荐构建工具**。

1. 安装`vite`相关依赖
```bash
# vite安装对于nodejs环境要求，vite6+，nodejs版本要求v22+
yarn add -D vite @vitejs/plugin-vue
```
2. 配置`vite.config.js`
   - `publicDir` 静态资源目录，默认读取项目根目录下的`index.html`，配置之后，会读取`${publicDir}/index.html`
```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [vue()],
  publicDir: 'public', // 静态资源目录,默认为`/`项目根目录
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
```
3. 配置`index.html`(区别于Webpack，需要修改项目源码)
   - 添加脚本元素`<script type="module" src="/src/main.js"></script>`
```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Vite + Vue</title>
  </head>
  <body>
    <div id="app"></div>
    <!-- 重要脚本 -->
    <script type="module" src="/src/main.js"></script>
  </body>
</html>
```
4. 配置`package.json`脚本
```json
  "scripts": {
    "dev:vite": "vite",
    "build:vite": "vite build",
    "preview": "vite preview",
  },
```
