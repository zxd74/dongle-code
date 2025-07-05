# 并发请求
## 基础实现
* 使用`async`创建异步函数，支持并发多次执行
* 使用`await`等待异步函数执行完成
* 记录任务索引，保证任务结果顺序
* 记录任务完成数，保证所有任务完成
```js
// function task(){} // 异步任务
function request(tasks,max) {
    const res = [];
    let nextIndex = 0;
    let finnished = 0;
    async function _request() { // 异步请求函数
        if(nextIndex >= tasks.length) {
            return;
        }
        const  i = nextIndex;
        const task = tasks[i++];
        const result = await task();

        // res.push(result);
        res[i] = result; // 保留任务顺序
        finnished++; // 记录已完成任务数

        if(finnished === tasks.length){
            return res // 所有任务完成，回调成功结果
            return;
        }
        _request(); // 继续下一个任务
    }
    for(let i=0;i<MIN(tasks.length,max);i++) { // 启动并发任务
        _request();
    }
}
```
## Promise实现
* 使用`Promise.all`实现批次并发请求
* 使用`async`和`await`实现异步等待
```js
import { cpus } from 'node:os';
// maxConcurrency 最大并发数  cpus()
async function runParallel(maxConcurrency, tasks, run) {
  const ret = []
  const executing = []
  for (const item of source) {
    const p = Promise.resolve().then(() => run(item))
    ret.push(p)

    if (maxConcurrency <= tasks.length) {
      const e = p.then(() => {
        executing.splice(executing.indexOf(e), 1)
      })
      executing.push(e)
      if (executing.length >= maxConcurrency) {
        await Promise.race(executing)
      }
    }
  }
  return Promise.all(ret)
}
```
