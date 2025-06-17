# 验证码
* 随机验证码：用户在页面即可观测到验证码，容易被机器识别
* 邮箱验证：后端需配置email业务，将生成的验证码发送至用户邮箱，用户需前往邮箱获取
* 手机验证：后端需绑定手机通信业务，将验证码发送到用户手机上
* 图片验证：非验证码内容，采用图片方式，由用户处理，比如图片残缺移动或多图找存在指定内容的图等

## 随机验证码
* 前端生成自验证：不安全
* 后端生成并验证

### 前端Canvas方式
* 通过Canvas绘图实现
```html
<!doctype html>
<html lang="en">
 <head>
  <title>Document</title>
  <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
  <script type="text/javascript">
  		var codes = ['a','b','c','d','e','f','g','h','i','j','k',
  			'l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
  			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
  			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
  			'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];

      var codeLen = 6; // 验证码数量
      var lineNum = 6;//干扰线数量
      var width=140,height=40;
      var canvasId = "random-code-canvas"
      var auth_code = new Array(codeLen);

      window.onload = () => {
      	createCanvas();
      	createRandomCodeImage()
      }

      function createCanvas(){
      		var doc = document.getElementById("codeImage");
      		var canvas = document.createElement("canvas")
      		canvas.id = canvasId
      		canvas.width = width
      		canvas.height = height
      		canvas.style.cursor = "pointer";
      		doc.appendChild(canvas)
      		canvas.onclick = () => createRandomCodeImage()
      }

  		function createRandomCodeImage(){
  			// 创建canvas对象
  			var canvas = document.getElementById(canvasId);

  			if (canvas == undefined){
  				createCanvas()
  			}

  			ctx = canvas.getContext('2d');

  			ctx.textBaseline = "middle";
  			ctx.fillStyle = randomColor(180,240);
  			ctx.fillRect(0,0,width,height);

  			// 重新生成验证码内容
				randomCode()
  			// 绘制验证码
  			for(let i=0;i<codeLen;i++){
  				var code = auth_code[i];
  				ctx.font = randomNum(height / 2,height) + 'px SimHei'; // 设置字体
  				// 内容阴影设置
  				ctx.fillStyle = randomColor(50,160)
  				ctx.shadowOffsetX = randomNum(-3,3)
  				ctx.shadowOffsetY = randomNum(-3,3)
  				ctx.shadowBlur = randomNum(-3,3)
  				ctx.shadowColor = "rgba(0,0,0,0.3)"

  				var x = width / 6 * i;
  				var y = height / 6;
  				var deg = randomNum(-3,3)

  				ctx.translate(x,y); // 重新映射画布0，0位置
  				ctx.rotate(deg * Math.PI / 180); // 旋转画布
  				ctx.fillText(code,10,10); // 绘制内容，相对画布的坐标
  				ctx.rotate(-deg * Math.PI / 180)
  				ctx.translate(-x,-y)
  			}

  			//绘制干扰线
  			for(let i = 0;i < codeLen;i++){
  				ctx.strokeStyle = randomColor(40,180)
  				ctx.beginPath();
  				ctx.moveTo(randomNum(0,width),randomNum(0,height))
  				ctx.lineTo(randomNum(0,width),randomNum(0,height))
  				ctx.stroke();
  			}

  			// 绘制干扰点
  			for(let i=0;i< width / 20 ;i++){
  				ctx.fillStyle = randomColor(0,255)
  				ctx.beginPath();
  				ctx.arc(randomNum(0,width),randomNum(0,height),1,0,2*Math.PI)
  				ctx.fill();
  			}
  		}

  		function randomCode(){
  			 for(let i=0;i<codeLen;i++){
  			 		auth_code[i] = codes[randomNum(0,codes.length)]
  			 }
  		}

        // 随机数，js没有范围随机数函数
  		function randomNum(min,max){
  			return Math.floor(Math.random() * (max-min) + min);
  		}

  		function randomColor(min,max){
  			var r = randomNum(min,max)
  			var g = randomNum(min,max)
  			var b = randomNum(min,max)
  			return "rgb(" + r + "," + g + "," + b + ")";
  		}

  		function checkCode(){
				var el = document.getElementById("code");
				console.log(el.value);
				var code = el.value;
				if (code==undefined || code == "" || code.length != 6) {
					alert("验证码错误！")
					return;
				}
				// var authCode = "";
				// auth_code.forEach(c => authCode = authCode + c)
				var authCode = auth_code.join('');
				console.log(authCode)
				if (code == authCode) {
					alert("验证通过")
				}else{
					alert("验证失败")
					createRandomCodeImage();
				}
  		}
  </script>
 <body>
 		<div id="codeImage" style="display:inline;"></div>
 		<input id="code" type="text">
 		<button onclick="checkCode()">Check</button>
 </body>
</html>
```
### 后端Java方式
采用java.awt包实现，
* BufferedImage 缓存图
* Graphics（Graphics2D）绘图
  * Line(Line2D) 线条
  * Font 字体
  * AffineTransform 旋转/缩放
  * Color 颜色，颜色配置生效于下次绘图
  * BasicStroke 定义线条特征
* ImageIO 生成图

```java
public class ImageUtils {

    private final static int CODE_NUM = 6; // 定义验证码长度
    private final static Random random = new Random(); // 定义随机数对象
    private final static char[] CODE_CHARS = { 'a','b','c','d','e','f','g','h','i','j','k',
            'l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' }; // 定义验证码内容集

    private final static Map<String,String> CODE_MAP = new ConcurrentHashMap<>(); // 存储验证码并验证；线上请考虑其它方式

    /**
     *
     * @param width 验证码图片的宽度
     * @param height 验证码图片的高度
     * @param token 绑定唯一信息
     * @return
     */
    public static BufferedImage randomAuthCode(int width, int height, String token){
        // 定义RGB图片
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics(); // 定义绘图对象
        Graphics2D g2d = (Graphics2D) g; // 线条2d绘图对象

        // 绘制背景及字体
        Font font = new Font("",Font.BOLD,25); // 定义字体样式及尺寸
        g.setColor(randomColor(200,255));// 设置颜色
        g.fillRect(0,0,width,height); // 背景图片大小
        g.setFont(font);

        // 绘制随机线条
        g.setColor(randomColor(180,200));
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int x1 = random.nextInt(6) + 1;
            int y1 = random.nextInt(12) + 1;
            BasicStroke bs = new BasicStroke(2f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL); // 定义线条样式
            Line2D line = new Line2D.Double(x,y,x + x1,y + y1);
            g2d.setStroke(bs);
            g2d.draw(line); // 绘制线条
        }

        // 生成并绘制验证码，建议验证码获取与绘图分开
        StringBuilder sb = new StringBuilder();String tmp;
        for (int i = 0; i < CODE_NUM; i++) { // 随机生成N个字符
            char ch = CODE_CHARS[random.nextInt(CODE_CHARS.length)];
            tmp = String.valueOf(ch);
            sb.append(tmp);
            Color color = new Color(20 + random.nextInt(110),20 + random.nextInt(110),random.nextInt(110));
            g.setColor(color);

            // 文字2d绘图对象
            Graphics2D g2dw = (Graphics2D) g;
            // 文字旋转，自行调整合适数据
            AffineTransform transform = new AffineTransform();
            transform.rotate(45*3.14/180,15*i + 8,7);

            // 缩放文字
            float scaleSize = random.nextFloat() + 0.8f;
            if (scaleSize>1f) scaleSize = 1f;
            transform.scale(scaleSize,scaleSize);
            g2dw.setTransform(transform);

            // 绘制验证码
            g.drawString(tmp,15 * i + 18,14);
        }

        // 释放绘画占用的资源
        g.dispose();
        // 记录code，便于后续验证，可考虑存入缓存中，并设置过期失效时间。
        CODE_MAP.put(token,sb.toString());
        System.out.println("为" + token + "生成验证码：" + sb);
        sb = null;
        return image;
    }

    private static Color randomColor(int s,int e){
        if (s>255) s= 255;
        if (e>255) e = 255;
        int r,g,b;
        r = s + random.nextInt(e-s);
        g = s + random.nextInt(e-s);
        b = s + random.nextInt(e-s);
        return new Color(r,g,b);
    }

    // 检查验证码
    public static boolean checkCode(String token, String code) {
        String u_code = CODE_MAP.get(token);
        if (u_code == null) return false;
        return u_code.equals(code);
    }

    private char[] randomCode(){
        char[] chars = new char[CODE_NUM];
        for (int i = 0; i < CODE_NUM; i++) { // 随机生成N个字符
            chars[i] = CODE_CHARS[random.nextInt(CODE_CHARS.length)];
        }
        return chars;
    }
}
```
```java
@RestController()
@RequestMapping("/")
public class IndexController {

    private static final String UID = "uuid";
    // 示例固定UUID，需根据业务场景处理
    private static final String DEFAULT_UUID = UUID.randomUUID().toString();

    @RequestMapping("code")
    public void randomCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");

        // 设置不缓存图片
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);

        // 设置响应内容类型为image/jpeg内容
        response.setContentType("image/jpeg");

        BufferedImage image = ImageUtils.randomAuthCode(120,40, DEFAULT_UUID);
        ImageIO.write(image,"JPEG",response.getOutputStream()); // 输出图片
    }

    @RequestMapping("check")
    public boolean checkCode(@RequestParam("code")String code, HttpServletResponse response, @SessionAttribute(value = UID,required = false) String uid){
        // 示例为单html文件，采用ajax校验，需要处理跨域，根据实际项目处理
        response.setHeader("Access-Control-Allow-Origin","*");
        boolean flag = ImageUtils.checkCode(DEFAULT_UUID,code);
        System.out.println(DEFAULT_UUID + "验证Code:" + code + (flag?"成功。":"失败！"));
        return ImageUtils.checkCode(DEFAULT_UUID,code);
    }
}
```
```html
<!doctype html>
<html lang="en">
 <head>
  <title>Test</title>
  <script type="text/javascript">
  	var Ajax = {
	    get: function(url,callback){
	        // XMLHttpRequest对象用于在后台与服务器交换数据
	        var xhr=new XMLHttpRequest();
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
    }
    function checkCode(){
        var el = document.getElementById("code");
        console.log(el.value);
        var code = el.value;
        if (code==undefined || code == "" || code.length != 6) {
            alert("验证码错误！")
            return;
        }
        Ajax.get("http://localhost:8080/check?code=" + code,function(data){
                console.log(data)
                if (data == "true") {
                    alert("验证通过！")
                }else{
                    alert("验证失败！")
                }
        })
    }
  </script>
 <body>
 		<img src="http://localhost:8080/code" onclick="this.src='http://localhost:8080/code?r='+ Math.random()">
 		<input id="code" type="text">
 		<button onclick="checkCode()">Check</button>
 </body>
</html>
```