# CSS
## 跑马灯文字效果
* 所有文字单字使用一个元素包裹，如`<span>`
* 创建一个动画
* 每个元素增加动画`animation`属性
* 按顺序给元素`nth-child(n)`增加`animation-delay`动画延迟属性
```html
<body>
    <div class="content">
        <span>D</span>
        <span>O</span>
        <span>N</span>
        <span>G</span>
        <span>L</span>
        <span>E</span>
    </div>
</body>
<style type="text/css">
    :root{
        background-color: black;
        --delay:0.1s;
    }
    .content span{
        color: red;
        animation: dongle 1s infinite ease-in-out alternate;
        font-size: 50px;
    }
    @keyframes dongle {
        to{
            color: white;
        }
    }
    .content span:nth-child(1) {
        animation-delay: calc(1*var(--delay));
    }
    .content span:nth-child(2) {
        animation-delay: calc(2*var(--delay));
    }
    .content span:nth-child(3) {
        animation-delay: calc(3*var(--delay));
    }
    .content span:nth-child(4) {
        animation-delay: calc(4*var(--delay));
    }
    .content span:nth-child(5) {
        animation-delay: calc(5*var(--delay));
    }
    .content span:nth-child(6) {
        animation-delay: calc(6*var(--delay));
    }

</style>
```
**考点**
1. `nth-child(n)`
2. `animation`
3. `animation-delay`
4. 延迟计算
   1. `calc()`
   2. `var()` 引用自定义变量
   3. 自定义变量`--xxx`，使用`:root`定义


# JavaScript(JS)
## 并发相关
### 并发执行
**思路**
* 使用`async`异步函数创建请求
  * 使用完成数检测任务完成
  * 保留任务顺序，需使用索引保存结果
  * 当索引超出后，不再创建索引
  * 使用`await`等待异步函数执行结束
* 使用最小并发数启动并发任务
```js
function task(){}
function request(urls,max) {
    return new Promise((resolve)=>{
        const res = [];
        let nextIndex = 0;
        let finnished = 0;
        async function _request() { // 异步请求函数
            if(nextIndex >= urls.length) {
                return;
            }
            const  i = nextIndex;
            const url = urls[i++];
            const result = await task(url);

            // res.push(result);
            res[i] = result; // 保留任务顺序
            finnished++; // 记录已完成任务数

            if(finnished === urls.length){
                resolve(res); // 所有任务完成，回调成功结果
                return;
            }
            _request(); // 继续下一个任务
        }
        for(let i=0;i<MIN(urls.length,max);i++) { // 启动并发任务
            _request();
        }
    })
}
```
**考点**
1. `async`异步函数，只有通过异步才能创建并发
2. `Promise`
3. `await`
4. `Promise.all`与`Promise.race`区别
   1. `Promise.all`所有任务完成才返回
   2. `Promise.race`任意一个任务完成就返回
5. `Promise`回调函数


## 请求相关
### 给Fetch怎加超时功能
```js
// 给Fetch怎加超时功能: 无给定方法及逻辑
```
**思路**
* 修改全局`fetch`方法（**不推荐**）
* 自定义一个方法，返回Promise，包装原生`fetch`方法
* 使用`AbortController`对象，通过`abort`方法取消函数调用
```js
// 影响范围太大,不能用
window.fetch=function(){} 

// 包装方法，通过options传递timeout：每次都需要传递timeout
function requestWithTimeOut(url, options) {}
requestWithTimeOut('https://www.baidu.com', {timeout: 5000});
requestWithTimeOut('https://www.example.com', {timeout: 6000});

// 给timeout设置默认值，同项目固定，但不同项目可能不固定
function requestWithTimeOut(url, options) {
    const timeout = options.timeout || 5000; // 默认值
}
requestWithTimeOut('https://www.example.com');

// 创建一个函数，传递timeout,返回包装函数
```
**最终形态**
```js
// 创建一个函数，传递timeout,返回包装函数
function createRequestWithTimeout(timeout){
    return function(url, options){ // 具体fetch包装函数
        return new Promise((resolve, reject) => { // 返回一个Promise异步操作
            const controller = new AbortController(); // 创建一个AbortController对象，用于取消请求
            const signal = controller.signal;
            options.signal = signal;
            const timeoutId = setTimeout(() => {
                controller.abort(); // 取消请求
                reject(new Error('timeout')); // 抛出超时错误
            },timeout)
            fetch(url, options) // 原生fetch方法
                .then(response=>{
                    clearTimeout(timeoutId);
                    if(!response.ok){
                        reject(new Error(`Http Error! status:${response.status}`));
                    }
                    return response.json(); // 具体返回依实际需要调整
                })
                .then(data=> resolve(data)) // 返回成功处理
                .catch(err=> reject(err)); // 返回失败处理
        })
    }
}
function createRequestWithTimeout(){ // 有默认的timeout
    return createRequestWithTimeout(5000);
}
const requestA = createRequestWithTimeout(); // 默认超时时间的请求
const requestB = createRequestWithTimeout(1000); // 不同超时时间的请求
const requestC = createRequestWithTimeout(3000);
```
**考点**
1. `fetch`方法
2. `AbortController`对象
3. `Promise`异步操作
4. `setTimeout,clearTimeout` 定时器方法
5. **公共方法**：传递timeout参数，返回包装函数

## Promise相关
### 给定大量长任务执行，优化使其不阻塞
```txt
运行一个耗时大批量任务,每次调用一次
若需要异步执行任务，请返回Promise
要尽快完成，同时不要让页面产生卡顿（检验方式：动画）
尽量多的兼容浏览器
```
**思路**
* `requestIdleCallback`：**空闲回调**函数（部分浏览器支持）
* `requestAnimationFrame`：**动画帧回调**函数(需要自定义实现)
```js
function execute(tasks) { // tasks.lenth is large
    tasks.forEach(task=>runTask(task));
}

function runTask(task) { // 若task也是耗时任务，那么仍旧会卡顿，需要优化任务逻辑
    return new Promise((resolve)=> _run(task,resolve));
}
function _run(task,callback){
    requestIdleCallback((dealine)=>{
        if(dealine.timeRemaining()>0){
            task();
            callback();
        }else{
            _run(task,callback);
        }
    })
    // const start = Performance.now();
    // requestAnimationFrame(()=>{
    //     if(Performance.now()-start<16){ 
    //         task();
    //         callback();
    //     }else{
    //         _run(task,callback);
    //     }
    // })
}
```
**考点**
1. `requestIdleCallback`：**空闲回调**函数（部分浏览器支持）
2. `requestAnimationFrame`：**动画帧回调**函数(需要自定义实现)
3. **公共方法**：传递任务，返回Promise
4. **注意**：若单任务也是耗时任务，那么仍旧会卡顿

### 给定一个Promise函数，将给定字符串中的内容异步替换成Promise返回值
```txt
给定一个异步函数getName，返回Promise
给定一个template字符串，其中包含N多个数字，使用其它符号分割
根据匹配到的整个数字通过getName获取结果只，然后替换，然后输出结果
```
**思路**
* 先匹配
* 根据匹配结果异步执行
* 等待所有异步任务完成
* 最后通过替换+关联结果（数组方法`shift`）：肯定一一对应
* **注意**：不能根据匹配列表的内容进行替换查询
  * 容易错位匹配，如分别存在15，1，5，
    * 15会替换成`namename1namename5`，理论应该为`name15`
      * 15替换成`name15`
      * 1再替换`namename1name5`
      * 5再替换`namename1namename5`
```js
const getName = (num)=> Promise.resolve("name" + num); // 模拟异步函数

const template = '15,1,5,6,8,,,5,99,655,....45,..3';

(async ()=>{
    const matchs = template.match(/\d+/g);
    let names = matchs.map(getName)
    names = await Promise.all(names);
    const result = template.replace(/\d+/g,()=>{ // replace匹配的顺序和matchs匹配顺序一致
        return names.shift(); // 获取并移除第一个元素
    })
})();
```
**考点**
1. `Promise`异步操作
2. `Promise.all`异步操作
3. `match`字符串匹配
4. `replace`字符串替换
5. `shift`数组移除并返回first元素
6. **注意**：字符串替换时不能根据匹配列表的内容进行替换查询

### 给定Promise输出顺序
```js
Promise.resolve()
.then(() => { // p0
    console.log('0');
    return Promise.resolve(4); // p4
})
.then((res) => { // p4
    console.log(res);
});
Promise.resolve()
.then(() => { // p1
    console.log('1');
})
.then(() => { // p2
    console.log('2');
})
.then(() => { // p3
    console.log('3');
})
.then(() => { // p5
    console.log('5');
})
.then(() => { // p6
    console.log('6');
})
```
**思路**
1. 给上面的`then`分别别名`p0,p1,p2,p3,p4,p5,p6`
2. `Promise.resolve().then()`会立即进入微队列等待执行
3. 若`then`中返回了Promise，则同等Promise的下一个`then`为返回的Promise的逻辑
4. 返回Promise，具体逻辑分别见
   1. `PromiseA+规范`定义状态吸收`p0 吸收 p4状态`(即`p4`挂起，`p0`也挂起)
   2. `V8`引擎 定义状态的吸收具体实现(浏览器环境决定)：将吸收状态放在`then`队列执行`p4.then(p0->吸收状态->p4)`
```txt
Promise 状态: 
    p0:F p1:F p0:P(随p4挂起) p2:F p0:F p4:F p5:F p6:F

微队列： 
    0 1  p4.then(p0->吸收状态->p4) 2 p0->吸收状态->p4 3 4 5 6

输出结果：
    0 1 2 3 4 5 6
```
**考点**
1. Promise状态
2. Promise一般执行顺序
3. Promise返回Promise执行顺序(特殊情况)
   1. Promise状态吸收(**PromiseA+规范**)
   2. Promise状态吸收时机(**V8引擎**)


# Typescript(TS)
## 标注相关
### 给不定量函数添加标注
```ts
/**
 * 说明：由N个参数和一个函数组成，函数的参数类型和数量与前面的参数类型和数量一致
 * 如：
 *      addImpl(`number`,`string`,`boolean`,(a,b,c)=>{})    a:number b:string c:boolean
 *      addImpl(`string`,`boolean`,(a,b)=>{})      a:string b:boolean
 *      addImpl(`number`,`boolean`,(a,b)=>{})      a:number b:boolean
 */
addImpl(`number`,`string`,`boolean`,(a,b,c)=>{})
```
**思路**
1. 先声明出基本标注
2. 根据错误进行修订
```ts
// 基础声明 
declare function addImpl(...args,Function):void;

// 不可变数组参数应为最后一个参数，将所有参数包装为数组参数
declare function addImpl(...args:[...string[],Function]):void;

// 将前面的所有参数类型声明为一个数组参数类型,并展开Function参数
type JSTypes = 'boolean' | 'bigint'|'number' | 'string'|'symbol' |  'undefined' | 'object' | 'function';
declare function addImpl(...args:[...JSTypes[],(?) => any]):void;

// 由于Funcation参数的参数列表和前面参数类型列表一致，故需要将其与之关联
declare function addImpl(...args:[...JSTypes[],(...args:JSTypes[]) => any]):void;

// 可以提取出类型声明
declare function addImpl<T extents JSTypes[]>(...args:[...T,(...args:T) => any]):void;

// 由于JSType是字符串类型，而Function中需要明确类型，故需要一个映射关系
```
**最终形态**
```ts
type JSTypeMap={ // 映射关系
    boolean:boolean,
    bigint:bigint,
    number:number,
    string:string,
    symbol:symbol,
    undefined:undefined,
    object:object,
    function:(...args:any[])=>any,
}
type JSTypes = keyof JSTypeMap; // 映射关系的key列表
type GetType<T extends JSTypes[]> = {[V in keyof T]:JSTypeMap[T[V]]} // 根据映射关系生成类型
declare function addImpl<T extents JSTypes[]>(...args:[...T,(...args:GetType<T>) => any]):void; // 最终标注
```
**考点**
1. 类型声明
2. 可变参数
3. 参数数组
4. 类型映射
5. 类型推导
6. 类型约束
