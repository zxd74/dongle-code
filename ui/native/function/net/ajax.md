# js原生
```js
function createXhrHttpRequest(){
    if (window.XMLHttpRequest) {
        return new XMLHttpRequest()
    } else if (window.ActiveObject) {
        // 兼容IE6以下版本
        return new ActiveXobject('Microsoft.XMLHTTP')
    }
}
var Ajax = {
    get: function(url,callback){
        // XMLHttpRequest对象用于在后台与服务器交换数据
        var xhr=createXhrHttpRequest();
        xhr.open('GET',url,false);
        // xhr.setRequestHeader('Access-Control-Allow-Origin','*'); 
        xhr.onreadystatechange=function(){
            // readyState == 4说明请求已完成
            if(xhr.readyState==4){
                if(xhr.status==200 || xhr.status==304){
                    callback(xhr.responseText);
                }
            }
        }
        xhr.send();
    },
    post: function(url,data,callback){
        var xhr=createXhrHttpRequest();
        xhr.open('POST',url,false);
        // 添加http头，发送信息至服务器时内容编码类型
        xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
        xhr.onreadystatechange=function(){
            if (xhr.readyState==4){
                if (xhr.status==200 || xhr.status==304){
                    // console.log(xhr.responseText);
                    callback(xhr.responseText);
                }
            }
        }
        xhr.send(data);
    }
  }
```
# JQuery
```js
$.ajax({
    type: "GET",
    url: "test.php",
    data: {username: "admin", password: "123456"},
    success: function(msg){
        alert( "Data Saved: " + msg );
    },
    error: function(jqXHR,textStatus,errorThrown){
        alert("Error: " + errorThrown);
    },
})