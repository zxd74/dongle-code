# 动态组件
1. 动态添加组件
2. 动态组件复制
   1. 采用`v-for`循环自动追加组件
   ```vue
   <div v-for="item in data" v-key="item.key" v-value="item.value"></div>
   ```

# 组件参数传递
1. `props` 定义接收参数
2. `v-bind:`属性或直接属性传递参数
```vue
<template>
  <div>
    {{name}},{{age}}
  </div>
</template>
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
  <demo v-bind:name="" age=""><demo> 
</template>
<script>
import Demo from '@/components/Demo'
export default{
  components:{Demo}
}
</script>
```

# 组件事件绑定
1. 通过`$emit`定义组件事件
```vue
<!-- $emit定义内建事件 -->
<button v-on:click="$emit('enlarge-text')"/>
```
```vue
<!-- v-on绑定事件 -->
<blog-post v-on:enlarge-text="postFontSize += 0.1"></blog-post>
```

# 数据绑定
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
# 事件绑定
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
