# 基础实现
## 前端(Web)
```js
function downloadByAjax(){
	$.ajax({
		url:'http://localhost/download?appName=' + app + "&r=" + Math.random(),
		success:function(data,status,xhr){
			// Content-Disposition = attachment;filename=
			// content-type = application/octet-stream;charset=UTF-8
			let fileName = xhr.getResponseHeader('Content-Disposition').split(';')[1].split('=')[1].replace(/\"/g, '')
			let type = xhr.getResponseHeader("content-type")
			let blob = new Blob([data],{type:type})
			if (window.navigator && window.navigator.msSaveOrOpenBlob) { //IE浏览器
				window.navigator.msSaveOrOpenBlob(blob, fileName)
			}else{
				let objectUrl = (window.URL || window.webkitURL).createObjectURL(blob)
				let downFile = document.createElement('a')
				downFile.style.display = 'none'
				downFile.href = objectUrl
				downFile.download = fileName // 下载后文件名
				document.body.appendChild(downFile)
				downFile.click()
				document.body.removeChild(downFile) // 下载完成移除元素
				// window.location.href = objectUrl
				window.URL.revokeObjectURL(objectUrl)
			}
		}
	})
}
```
## 后端(Java)
```java
    @RequestMapping("export")
    public void export(HttpServletRequest request,HttpServletResponse response){
        try {
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition","attachment;filename=test.txt");

            // header 设置要在内容输出前配置
            response.getOutputStream().write(new byte[100]);

            response.getOutputStream().flush();
            response.getOutputStream().close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
```
