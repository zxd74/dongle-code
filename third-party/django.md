# 基本项目
1. 创建项目
```bash
mkdir django-demo && cd django-demo
# 需要空白目录
django-admin startproject mysite ## 创建项目
```
2. 创建应用
```python
# 创建项目后，会同步生成一个manage.py文件，通过该文件可以管理项目
python manage.py startapp myapp ## 创建应用
```
3. 运行项目
```bash
python manage.py runserver
```

# 模块管理
## 管理模块
django内置简易管理模块，项目创建时默认支持

1. 创建管理员账号
```bash
python manage.py createsuperuser
# Username：
# Email address：
# Password：
# Superuser created successfully. 
```
1. 登录后台
```bash
python manage.py runserver
```
1. 打开浏览器访问`http://127.0.0.1:8000/admin/`，输入账号密码登录

# 模型管理
## 创建模型
Django**默认使用**sqlite数据库，会在项目目录下生成一个`db.sqlite3`文件.其他数据库需要额外安装驱动

1. 在models.py中创建模型(表)结构
   - 每个模型都继承自`models.Model`类
   - 每个字段变量都相当于数据库中的一列
   - 由`models.XXX`提供数据类型
```py
from django.db import models

# Create your models here.
class Question(models.Model):
    question_text = models.CharField(max_length=200)
    pub_date = models.DateTimeField('date published')

class Choice(models.Model):
    question = models.ForeignKey(Question, on_delete=models.CASCADE)
    choice_text = models.CharField(max_length=200)
    votes = models.IntegerField(default=0)
```
2. 模型更新：**每次变更时都需要执行**
   1. 生成迁移文件(**类似数据库结构创建语句**)
      - 在`${app}/migrations`生成`XXX_initial.py`
        ```bash
        python manage.py makemigrations polls  # polls为app名称，相当于数据库,生成的数字标识即该app的数据库
        ```
      - 会生成如下提示
        ```txt
        Migrations for 'polls':
        polls\migrations\0001_initial.py
            + Create model Question
            + Create model Choice
        ```
   2. 检查迁移文件(可选)
    ```bash
    python manage.py sqlmigrate polls 0001 # app与迁移文件数字标识要保持一致
    ```
    ```txt
    BEGIN;
    --
    -- Create model Question
    --
    CREATE TABLE "polls_question" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "question_text" varchar(200) NOT NULL, "pub_date" datetime NOT NULL);
    --
    -- Create model Choice
    --
    CREATE TABLE "polls_choice" ("id" integer NOT NULL PRIMARY KEY AUTOINCREMENT, "choice_text" varchar(200) NOT NULL, "votes" integer NOT NULL, "question_id" bigint NOT NULL REFERENCES "polls_question" ("id") DEFERRABLE INITIALLY DEFERRED);
    CREATE INDEX "polls_choice_question_id_c5b4b260" ON "polls_choice" ("question_id");
    COMMIT;

    D:\Project\Practice\back-demo\django-demo>python manage.py migrate
    Operations to perform:
        Apply all migrations: admin, auth, contenttypes, polls, sessions
    Running migrations:
        Applying polls.0001_initial... OK
    ```
   3. 执行迁移
    ```bash
    python manage.py migrate
    ```
    ```txt
    Operations to perform:
    Apply all migrations: admin, auth, contenttypes, polls, sessions
    Running migrations:
    Applying polls.0001_initial... OK
    ```
3. 通过`shell`访问数据库：python命令行工具
   - 自动加载环境，并支持数据库操作
   - 每次变更需要执行`save()`用于保存
   - `${ModelName}.objects.all()` 查询所有记录
```bash
python manage.py shell
```
```shell
>>> Question.objects.all()
>>> from django.utils import timezone
>>> q = Question(question_text="What's new?", pub_date=timezone.now())
>>> q.save()
>>> q.question_text

>>> q.question_text = "What's up?"
>>> q.save()
>>> q.question_text
>>> Question.objects.all()
```
## 数据库支持(MYSQL)
数据库	|Django 后端引擎 (ENGINE)	|需要额外安装的驱动
---	|---	|---
**MySQL**	|`django.db.backends.mysql`	|`mysqlclient` (**推荐**) 或 `pymysql`
**PostgreSQL**	|`django.db.backends.postgresql`	|`psycopg2`
**SQLite**	|`django.db.backends.sqlite3`	|无需安装 (Python 内置)
**Oracle**	|`django.db.backends.oracle`	|`cx_Oracle`
**MariaDB**	|`django.db.backends.mysql` (同 MySQL)	|`mysqlclient` 或 `pymysql`

1. 安装驱动
```bash
pip install mysqlclient # 性能更好
# pip install pymysql # 兼容性更好
```
2. 修改`settings.py`配置
```py
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': 'your_database_name',      # 数据库名
        'USER': 'your_username',           # MySQL 用户名
        'PASSWORD': 'your_password',       # MySQL 密码
        'HOST': 'localhost',               # 数据库服务器地址
        'PORT': '3306',                    # MySQL 默认端口
        'OPTIONS': {
            'charset': 'utf8mb4',          # 支持完整的 Unicode（如表情符号）
            'init_command': "SET sql_mode='STRICT_TRANS_TABLES'",  # 严格模式
        }
    }
}
```
3. 处理时区问题（可选）
```py
USE_TZ = True  # 启用时区支持
TIME_ZONE = 'Asia/Shanghai'  # 根据实际时区修改
```

# 视图渲染
## 模板渲染
    需要确保模板配置能读取到模板文件
* `django.template.loader.get_template(template_name, using=None)`：加载模板文件
* `template.render(context=None, request=None)`：渲染模板，返回渲染后的 HTML 字符串
  * `content`是一个字典，用于向模板传递数据
```python
from django.http import HttpResponse
from django.template import loader
from .models import Question

def index(request):
    latest_question_list = Question.objects.order_by("-pub_date")[:5]
    template = loader.get_template("polls/index.html")
    context = {"latest_question_list": latest_question_list}
    return HttpResponse(template.render(context, request))
```
* 快捷`django.shortcuts.render`函数: 简化模板加载和渲染过程
```python
from django.shortcuts import render
from .models import Question

def index(request):
    latest_question_list = Question.objects.order_by("-pub_date")[:5]
    context = {"latest_question_list": latest_question_list}
    return render(request, "polls/index.html", context)
```
* `Http404`:404页面
```python
    try:
        ...
    except Question.DoesNotExist:
        raise Http404("Question does not exist")
```

## 数据响应
* `HttpResponse`：返回文本/字符串
* `JsonResponse`：返回JSON数据
```python
from django.http import JsonResponse

def json_response(request):
    data = {"name": "Alice", "age": 25}
    return JsonResponse(data)  # 自动设置 Content-Type: application/json

# 手动序列化（等效于 JsonResponse）
import json
def manual_json_response(request):
    data = {"name": "Bob", "age": 30}
    return HttpResponse(json.dumps(data), content_type="application/json")
```
* 返回文件
```python
def file_download(request):
    file_path = "/path/to/file.pdf"
    with open(file_path, "rb") as f:
        response = HttpResponse(f.read(), content_type="application/pdf")
        response["Content-Disposition"] = 'attachment; filename="file.pdf"'
        return response
```
* 返回图片
```python
def image_response(request):
    with open("image.png", "rb") as f:
        return HttpResponse(f.read(), content_type="image/png")
```
* `StreamingHttpResponse`：返回流式数据(大文件/实时生成)
```python
from django.http import StreamingHttpResponse

def stream_response(request):
    def generate_data():
        yield "First chunk\n"
        yield "Second chunk\n"
    return StreamingHttpResponse(generate_data())
```
### JSON序列化
* 原生序列化`json`
```python
data = {"name": "Bob", "age": 30}
json.dumps(data)
```
* python3.7+支持的`dataclasses + asdict`
```python
from dataclasses import dataclass, asdict
from django.http import JsonResponse

@dataclass
class User:
    name: str
    age: int

def get_user(request):
    user = User(name="Charlie", age=35)
    return JsonResponse(asdict(user))  # 自动序列化
```
* 字典数据，由`JsonResponse`自动处理
  * 类自定义实现`to_dict`方法
  * 使用类`__dict__`：需谨慎，暴露所有属性(含私有)，无法处理嵌套属性
```python
class User:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def to_dict(self): # 自定义 to_dict 方法
        return {
            "name": self.name,
            "age": self.age
        }
```
* 自定义JSON编码器`json.JSONEncoder`
```python
import json
from datetime import datetime
from django.http import JsonResponse

class User:
    def __init__(self, name, join_date):
        self.name = name
        self.join_date = join_date  # 假设是 datetime 对象

class CustomEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, datetime):
            return obj.isoformat()  # 转换 DateTime 为字符串
        return super().default(obj)

def get_user(request):
    user = User(name="Dave", join_date=datetime.now())
    user_dict = {"name": user.name, "join_date": user.join_date}
    return JsonResponse(user_dict, encoder=CustomEncoder)  # 指定自定义编码器
```
* django内置序列化：`django.core.serializers`
  * 返回格式是 Django 特定的（包含 `fields` 和 `pk`），需额外处理。
```python
from django.core import serializers
user = User.User('John', 25)
json_data = serializers.serialize('json', [user])
```
* 自定义django序列化类：`django.core.serializers.python.Serializer`
```python
from django.core.serializers.python import Serializer

class MyModelSerializer(Serializer):
    def get_dump_object(self, obj):
        data = {
            'id': obj.id,
            'name': obj.name,
            'age': obj.age,
            'related_objects': self.get_related_data(obj),
        }
        return data

    def get_related_data(self, obj):
        related_data = {}
        if obj.related_model:
            related_objects = obj.related_model.all()
            related_data = [{'id': related_obj.id, 'name': related_obj.name} for related_obj in related_objects]
        return related_data

serializer = MyModelSerializer()
user = User.User('John', 25)
json_data = serializer.serialize(user, use_natural_foreign_keys=True)
```
## 响应配置
* 自定义Header
```python
def custom_header(request):
    response = HttpResponse("OK")
    response["X-Custom-Header"] = "123"
    return response
```
* 状态码
```python
from django.http import HttpResponseNotFound

def not_found(request):
    return HttpResponseNotFound("<h1>Page not found</h1>")

# 或使用通用 HttpResponse
def server_error(request):
    return HttpResponse("Server Error", status=500)
```
* 重定向：`HttpResponseRedirect` 或 `redirect`
```python
from django.shortcuts import redirect
from django.http import HttpResponseRedirect

def redirect_view(request):
    return redirect("/new-url/")  # 等效于 HttpResponseRedirect

def http_redirect(request):
    return HttpResponseRedirect("/another-url/")
```


# 配置
## 路由配置
* 可以统一在项目的`urls.py`中配置，也可以在应用中`polls/urls.py`配置
* `django.urls.path` 函数：定义路由规则
* `django.urls.include` 函数：导入其他路由配置(应用路由配置)
```python
# */*.urls.py
from django.urls import path,include

from . import views

urlpatterns = [
    # ex: /polls/
    path("", views.index, name="index"),
    # ex: /polls/5/
    path("<int:question_id>/", views.detail, name="detail"),
    # ex: /polls/5/results/
    path("<int:question_id>/results/", views.results, name="results"),
    # ex: /polls/5/vote/
    path("<int:question_id>/vote/", views.vote, name="vote"),
]
``` 
* 外部通过路径访问，内部(**模板访问**)通过`name`访问
* **模板访问**：反向解析`reverse`函数
  * 不能直接使用URL，即直接url路径错误
  * 使用命名空间

### 命名空间
* 默认没有命名空间
* 配置命名空间：需要在`urls.py` 指定`app_name`
```python
# web/urls.py
from django.urls import path
from . import views

app_name = 'web'  # 显式定义命名空间
urlpatterns = [
    path("index", views.index, name="index"),
]
```
* 使用命名空间
  * 使用普通方式时，通过`name`
  * 使用命名空间方式时，通过`app_name:name`
    ```html
    <a href="{% url 'index' "/> <!--普通方式-->
    <a href="{% url 'web:index' "/> <!--命名空间方式-->
    ```
    ```python
    HttpResponseRedirect(reverse("index"))  # 重定向时普通方式
    HttpResponseRedirect(reverse("web:index"))  # 重定向时命名空间方式
    ```

### 参数路径
* 顺序不可更换
```python
url(r'^users/([a-z]+)/(\d{4})/$', views.get_user),
  
def get_user(request, name, ID):
    pass
```
* 具名参数路径:`?P`修饰
```python
url(r'^users/(?P<name>[a-z]+)/(?P<id>\d{4})/$', views.users),
  
def weather(request, name, id):
    pass
```
### 请求数据解析
* **表单数据**：根据请求方式，以字典形式解析请求参数
```python
  
def get_value(request):
    a = request.GET.get('a')  #3
    b = request.GET.get('b')  #2
    num_list = request.GET.getlist('a') #['1','3']

def get_body(request):
    form_data = request.POST.get('c')
```
* **非表单数据**: `request.body` 获取原始数据
```python
def get_body_json(request):
    json_str = request.body
```
* **请求头解析**:`request.META.get("header key")`
  * `request.method`
  * `request.path`
  * `request.user`
  * `request.FILES`
  * `request.encoding`



## 模板路径
* 静态配置：相对项目目录的绝对路径
```python
TEMPLATES = [
    {
        ...
        "DIRS": [BASE_DIR / "templates"],  # 添加模板目录
        ...
    },
]
```
* **动态配置**
```python
TEMPLATE_DIRS = [
    str(app_dir / "templates")
    for app_dir in BASE_DIR.iterdir()
    if app_dir.is_dir() and (app_dir / "templates").exists()
]
TEMPLATES = [
    {
        ...
        "DIRS": TEMPLATE_DIRS,  # 添加模板目录
        ...
    },
]
```
* **模板规范**
  * 每个app目录下创建一个templates目录，并用app名称作为子目录
  * 如`polls`应用，创建模板文件`index.html`，路径应为`polls/templates/polls/index.html`

