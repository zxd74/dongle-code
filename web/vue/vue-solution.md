# 引入非ES6 js文件(待验)
* 方法一：直接复制粘贴test源码到`index.html,index.html`里的js不会被编译，这种方式简单粗暴但难看
* 方法二：把test放在`static`目录里，**static目录不会被webpack所编译**，打包后的test被移到`dist/static/test.js`
* 方法三：在webpack.base.conf.js里配置移除项使其不被编译,然后在需要的地方引入`import test from 'test'`
```js
externals: {
    test: 'window.test'
}
```
* 方法四：直接在webpack.base.conf.js里配置别名，然后在需要的地方引入‘import test from 'test
```js
resolve: {
    extensions: ['.js', '.vue', '.json','.less'],
    alias: {
        'test': resolve('static/test.min.js')
    }
}
```
* 方法五：webpack.base.conf.js里配置别名，并配置plugins参数，就无需import引入，在任意组件或js中使用
```js
plugins:[
  	new webpack.ProvidePlugin({
  	    test : 'test'
  	})
]
```
* 方法六：通过render方法，当作组件使用引入
```js
export default{
    components:{
        'test-js':{
            render(createElement){
                return createElement('script',{
                    attrs:{
                        type:'text/javascript',
                        src:'static/test.min.js'
                    }
                })
            }
        }
    }
}
// <test-js></test-js>
```
* 方法七：包装成一个js插件，使用Promise
```js
function loadJS(src){
    return new Promise((resolve,reject)=>{
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = src;
        script.onload = resolve;
        script.onerror = reject;
        document.head.appendChild(script);
    })
}

export  default loadJS;
```
```js
import loadJS from '@/assets/js/loadJS'
export default{
    mounted(){
        loadjs('static/test.min.js').then(res=>{

        })
    }
}
```
* 方法八：手动在非es6的js文件中增加`export`
* 方法九：将内容挂载到全局`Vue.prototype.name=...类似等同增加`export`