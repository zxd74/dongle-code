用于加载更多内容，如电商的商品列表，尤其图片列表，瀑布流等。

**思路**
* 监听元素可见性`IntersectionObserver`
  * `observe`方法只支持`Element|null|NodeList`参数类型

# 基础图片列表显示内容
* **思路**
  * 定义列表长度的空`img`
  * 通过`IntersectionObserver`监听`img`的可见性
  * 当`img`可见时，给`src`赋值真实的图片地址，并移除监听
* **注意**
  * 此场景需要前端提前知道图片地址，以及图片列表长度，动态加载不适合
  * 主要了解使用`IntersectionObserver`监听元素可见性
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <style type="text/css">
        .content{
            display: flex;
            flex-wrap: wrap;
        }
        .content div{
            width: 200px;
            height: 300px;
            border: 1px solid red;
            margin: 10px;
        }
        .content div img{
            width: 100%;
            height:100%;
        }
    </style>
</head>
<body>
    <div class="content">
        <div><img src="" alt=""></div>
        <!-- N img -->
    </div>
    <script type="text/javascript">
        const ob = new IntersectionObserver(
            (entries)=>{
                // 出现交叉情况了
                // console.log(entries); 初始时所有元素都会触发一次，并且当前视图内的所有元素都会被标记isIntersecting==true
                entries.filter(entry=>entry.isIntersecting).forEach(entry=>{
                    // 出现交叉情况了
                    const img = entry.target;
                    let src = img.getAttribute('src'); 
                    if(!src){ // 若为空，则替换
                        console.log('为空需要替换替换');
                        //const src = img.getAttribute('data-src'); // 获取自定义属性，
                        //img.setAttribute('src',src);// 替换原有src，即会直接请求图资源并显示
                        ob.unobserve(img); // 取消观察，因为已经替换了src，不需要再观察了
                    }
                });
            },
            {  // 监听配置
                // root:null, // 观察的元素的父元素，null为视口(窗口)，默认null
                // rootMargin:'0px', // 是否扩散或缩小观察范围 默认0px，负数代表内缩
                threshold:0.1 // 交叉阈值，当元素与视口交叉程度达到该值时触发回调函数，默认0
            }
        );
        const imgList = document.querySelectorAll('img');
        console.log(imgList.length)
        imgList.forEach((img)=>{
            ob.observe(img);
        })
    </script>
</body>
</html>
```
# 动态加载更多
* **思路**
  * 在图片下方添加一个加载更多按钮
  * 并用监听元素可见性`IntersectionObserver`监听该按钮
  * 当按钮可见时，加载指定数量的图片，并添加到图片列表中
```js
<!DOCTYPE html>
<html lang="en">
<head>
    <style type="text/css">
        .content{
            display: flex;
            flex-wrap: wrap;
        }
        .content div{
            width: 200px;
            height: 300px;
            border: 1px solid red;
            margin: 10px;
        }
        .content div img{
            width: 100%;
            height:100%;
        }
        .load-more{
            margin: 10px auto;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="content">
    </div>
    <div class="load-more" onclick="loadImages(11)"><span>加载更多...</span></div>
    <script type="text/javascript">
        const ob = new IntersectionObserver(
            (entries)=>{
                entries.filter(entry=>entry.isIntersecting).forEach(entry=>{
                    loadImages(11);
                });
            },{
                threshold:0
            }
        );
        {/* const loadMore = document.querySelector('span') */}
        const loadMore = document.getElementsByClassName('load-more')[0];
        ob.observe(loadMore);
        function loadImages(n){
            let content = document.querySelector('.content');
            for(let i=0;i<n;i++){
                let div = document.createElement('div');
                let img = document.createElement('img');
                div.appendChild(img);
                content.appendChild(div);
            }
            console.log('当前已有图片',document.getElementsByClassName('content')[0].children.length)
        }
    </script>
</body>
</html>
```
