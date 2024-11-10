js类型：
* **CJs: CommonJS**
  * 使用`require`导入module
  * 使用`module.exports`导出module
* **ESM: ECMAScript Module,又ModuleJS**
  * 使用`import`而非require导入module
  * 使用`export`而非module.exports导出module

# 参数解析
1. 通过`process`模块获取参数列表
```js
const argvs = require('process').argv
// 或
const args = process.argv.slice(2);
```
```shell
# 示例
[
  '~/Nodejs/node.exe',  # 执行命令
  '~/index.js', # 执行js文件
  'init', # 后续都是命令参数
  '--name',
  'kevin'
]
```
2. 通过`yargs`模块获取参数列表
```js
const argv = require('yargs').argv;
```
## 命令行工具
1. [commander](https://github.com/tj/commander.js)
2. [yargs](https://github.com/yargs/yargs)

## 优化命令解析
* 创建命令集
```js
// commands.js  // 此方式为CommonJS
const init = require('dongle-node-lib').init
const version = require('../package.json').version;
const help = ()=>{
    console.log("Usage: dongle-node-cli [command] [options]")
    console.log("Commands:")
    console.log("  help - Show help information")
    console.log("  version - Show version information")
    console.log("  init - Show dongle information")
    console.log("Command <init> options:")
    console.log("  --name <value> - config dongle name")
    console.log("Global Options:")
    console.log("  --version,-v - Show version information")
    console.log("  --help,-h - Show help information")
}

module.exports = {
    init,
    version,
    help
}
```
* 创建命令解析器
```js
// index.js
const commands = require('./commands')
const argv = require('process').argv

let command = argv[2]
const options = argv.slice(3)

if(command){
    if(options.length > 1) { // 代表命令参数
        const [option,param] = options // 目前仅适用于两个参数，后续需要优化
        // console.log(option,param)
        if(commands[command]){
            commands[command]({option,param})
        }else{
            console.log("Command not found")
            commands.help()
        }
    } 
    // 其它为全局命令或参数
    else if(command.startsWith('-')){
        const globalOption = command.replace(/^(--|-)/g,'')  // /^--|-/g 所有位置匹配   /^(--|-)/g 仅匹配开头
        // console.log("Global Option: " + globalOption)
        if(globalOption === 'version' || globalOption.toUpperCase() === 'V'){
            console.log(commands.version);
        }else{
            console.log("Global Option not found")
            commands.help()
        }
    }else{
        console.log("Command not found")
        commands.help()
    }
}else{
    console.log("Command not found")
    commands.help()
}
```

# 数据类型