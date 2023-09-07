<template>
    <div class='slide-show'>
        <div class="slide-data">
            <template v-for="(item,index) in data">
                <img :class="'slide-img-' + index" :src="require('@/assets/'+item.src)" :actived="slideIndex == (index+1)?'actived':''" @click="info(item.value)" :alt="item.title">
            </template>
        </div>
        <div class="slide-option" @mouseover="clearSlideTimer" @mouseout="createSlideTimer">
            <div class="slide-border">
                <div class="move slide-prev el-icon-arrow-left" @click="slide(slideIndex-1)"></div>
                <div class="move slide-next el-icon-arrow-right" @click="slide(slideIndex+1)"></div>
            </div>
            <div class="slide-options">
                <ul>
                    <template v-for="item in data.length">
                        <li  :class="'slide-option-' + item" :actived="slideIndex == item?'actived':''" @click="slide(item)">{{item}}</li>
                    </template>
                </ul>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    name: 'SlideShow',
    props: {
        "data":{
            type: Array,
            default:[],
        },
        "width":100, // 宽度
        "height":100, // 高度
    },
    data() {
        return {
            interval: 1000, // 间隔时间
            timer: null, // 定时器
            slideIndex:1, // 当前轮播图的索引
        }
    },
    created() {
        // 定时器
        this.createSlideTimer();
    },
    methods: {
        createSlideTimer(){
            this.timer = setInterval(() => this.slide(this.slideIndex + 1), this.interval);
        },
        clearSlideTimer(){
            clearInterval(this.timer);
            this.timer = null;
        },
        slide(num){
            console.log(num)
            if(this.data.length == 0 )
                return
            var slideNum = this.data.length
            if (num === undefined)
                num == 1
            else if(num > slideNum)
                num = 1            
            else if(num < 1)
                num = slideNum
            this.slideIndex = num;
        },
        info(url){
            console.log(url)
        }
    },
};
</script>
<style>
:root {
    --slide-width: 400px;
    --slide-height: 400px;
}
.slide-show{
    position: relative;
    overflow: hidden;
    margin: 0;
    border: 0;
    padding: 0;
}
.slide-show,.slide-data img{
    height: var(--slide-height);
    width: var(--slide-width);
}
.slide-data img{
    position:absolute;
    left: 0;top: 0;
    display: none;
    &[actived='actived']{
        display: block;
    }
}

.slide-option{
    bottom: 2px;
    font-size: calc(var(--slide-height) * 0.1 - 5px);
    height: calc(var(--slide-width) * 0.1 + 5px);
    position: absolute;
    width: 100%;
    overflow: hidden;
}

.slide-options,.slide-border{
    position: absolute;
    height: 100%;
    width: 100%;
    margin: 0;
}
.slide-options{
    display: flex;
    justify-content: center;
    pointer-events: none;
    & ul{
        width: auto;
        margin: 0;
        padding:0;
        text-align: center;
        pointer-events: auto;
    }
    & li{
        list-style: none;
        margin-left: 2px;
        margin-right: 2px;
        float: left;
        :focus{
            background-color: yellow;
        }
        &[actived='actived']{
            background-color: greenyellow;
        }
    }
}

.slide-border{
    text-align: left;
    & .move{
        position:absolute;
        width: calc(var(--slide-width) * 0.1);
        text-align: left;
        pointer-events: auto;
        :hover{
            background-color: rgb(0, 0, 0,0.4);
        }
    }
    & .slide-next{
        right: 0;
    }
}
</style>