方式：
* 通过[linear-gradient](https://developer.mozilla.org/zh-CN/docs/Web/CSS/gradient/linear-gradient)实现颜色线性渐变
* 通过[animation](https://developer.mozilla.org/zh-CN/docs/Web/CSS/animation)实现颜色循环渐变

# 文字渐变 linear-gradient
注意：存在浏览器兼容问题。
* 使用`linear-gradient`调整背景颜色
* 使用`background-clip` 设置背景(图片或颜色)延申方向

## 向右渐变
<style text="text/css">
    #first-gradient{
        font-size: 50px;
        background-image: linear-gradient(to right, black, red, green, blue, black);
        /* 以下内容为组合样式 */
        background-clip: text; /**设置背景颜色延申方向 text即在文字上展示背景*/
        -webkit-background-clip: text; /** 兼容配置 */
        color: transparent; /** 设置文字内容透明，才会在文字上展示背景颜色 */
    }
</style>
<div id="first-gradient">
    DONGLE
</div>

## 向上渐变
<style>
    #second-gradient{
        font-size: 50px;
        background-image: linear-gradient(to top, green, blue);
        /* 以下内容为组合样式 */
        background-clip: text; /**设置背景颜色延申方向 text即在文字上展示背景*/
        -webkit-background-clip: text; /** 兼容配置 */
        color: transparent; /** 设置文字内容透明，才会在文字上展示背景颜色 */
    }
</style>
<div id="second-gradient">
    DONGLE
</div>


# 循环渐变
## 方式一 原生动画
<style text="text/css">
    #first-each span{
        font-size: 50px;
        animation: spanColor 1s ease-in-out infinite;
    }

    @keyframes spanColor{
        to{
            color: red;
            /** 增加颜色阴影*/
            /* text-shadow: 20px 0 70px red;  */
        }
    }
    /* 以下配置方便每个元素内容动画延迟实践，方便循环渐变 */
    #first-each span:nth-child(1) {
        animation-delay: 0s;
    }
    
    #first-each span:nth-child(2) {
        animation-delay: 0.2s;
    }

    #first-each span:nth-child(3) {
        animation-delay: 0.4s;
    }

    #first-each span:nth-child(4) {
        animation-delay: 0.6s;
    }

    #first-each span:nth-child(5) {
        animation-delay: 0.8s;
    }

    #first-each span:nth-child(6) {
        animation-delay: 1s;
    }
</style>
<div id="first-each">
    <span>D</span>
    <span>O</span>
    <span>N</span>
    <span>G</span>
    <span>L</span>
    <span>E</span>
</div>

```scss
#first-each span{
    padding-left: 2vh;
    animation: spanColor 1s ease-in-out infinite alternate;
}

@keyframes spanColor{
    to{
        color: red;
        text-shadow: 20px 0 70px red;
    }
    
}

@for $i from 1 through 10{
    #first-each span:nth-child(#{$i}){
        animation-delay: ($i - 1 ) * 0.2s;
    }
}
```

