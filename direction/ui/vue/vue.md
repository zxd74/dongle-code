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