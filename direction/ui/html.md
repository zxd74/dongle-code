- [布局展示](#布局展示)
- [实战整理](#实战整理)
- [实践](#实践)
  - [Web保持图片清晰度](#web保持图片清晰度)

# 布局展示
属性|说明
:--|:---
position| 决定元素界面位置类型
display| 决定元素展示效果
float| 仅在`display=flex`浮动时有效
width,height| 决定元素尺寸
top,left,right,bottom| 仅在`position=absolute`时有效
margin,border,padding| 决定元素盒子模型，各有top,left,right,bottom属性

# 实战整理
功能|状态|说明
:--|:--|:---
循环滚动|-|例如公布获奖情况，循环滚动展示获奖名单
水平垂直居中|-|将内容在水平和垂直方向上都居中展示

# 实践
## Web保持图片清晰度
* 根据不同宽度制作不同尺寸图片
* **DPR**  物理像素/图片标签尺寸,`windows.devicePixelRatio`
* `-webkit--image-set`  CSS背景属性，根据DPR值使用不同图片，有兼容性问题
* `srcset` 图片标签根据DPR配置
* 实际需要的图片尺寸与DPR和标签尺寸有关
* 推荐自定义通过js动态调整：`css宽度*dpr`
    * 有一个`1px`的**透明图片**，使标签尺寸生效
    * 监听标签尺寸(响应式宽度) `ResizeObserver+watchEffect + computed(vue)`

