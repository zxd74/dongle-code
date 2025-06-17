# scss支持
1. 安装
```shell
npm install node-sass sass-loader --save-dev
```
2. 配置loder:`webpack.base.conf.js`文件`module.rules`增加以下配置
```js
{
   test: /\.scss$/,
   loader: 'sass-loader!style-loader!css-loader',
}
```
3. 使用
```vue
<style scoped lang="scss"></style>