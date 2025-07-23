# 构建模块
**模块目录结构**
```txt
-- LICENSE 文件授权协议
-- pyproject.toml 说明构建和安装应用程序
-- MANIFEST.in 额外文件
-- docs/ 可选模块文档
-- __init__.py 初始化模块
-- */*.py 源代码
```
* **LICENSE**
```LICENSE
============
django-polls
============

django-polls is a Django app to conduct web-based polls. For each
question, visitors can choose between a fixed number of answers.

Detailed documentation is in the "docs" directory.

Quick start
-----------

1. Add "polls" to your INSTALLED_APPS setting like this::

    INSTALLED_APPS = [
        ...,
        "django_polls",
    ]

2. Include the polls URLconf in your project urls.py like this::

    path("polls/", include("django_polls.urls")),

3. Run ``python manage.py migrate`` to create the models.

4. Start the development server and visit the admin to create a poll.

5. Visit the ``/polls/`` URL to participate in the poll.
```
* **pyproject.toml**
```toml
[build-system]
requires = ["setuptools>=69.3"]
build-backend = "setuptools.build_meta"

[project]
name = "django-polls"
version = "0.1"
dependencies = [
    "django>=X.Y",  # Replace "X.Y" as appropriate
]
description = "A Django app to conduct web-based polls."
readme = "README.rst"
requires-python = ">= 3.10"
authors = [
    {name = "Your Name", email = "yourname@example.com"},
]
classifiers = [
    "Environment :: Web Environment",
    "Framework :: Django",
    "Framework :: Django :: X.Y",  # Replace "X.Y" as appropriate
    "Intended Audience :: Developers",
    "License :: OSI Approved :: BSD License",
    "Operating System :: OS Independent",
    "Programming Language :: Python",
    "Programming Language :: Python :: 3",
    "Programming Language :: Python :: 3 :: Only",
    "Programming Language :: Python :: 3.10",
    "Programming Language :: Python :: 3.11",
    "Programming Language :: Python :: 3.12",
    "Programming Language :: Python :: 3.13",
    "Topic :: Internet :: WWW/HTTP",
    "Topic :: Internet :: WWW/HTTP :: Dynamic Content",
]

[project.urls]
Homepage = "https://www.example.com/"
```
* **MANIFEST.in**
```txt
recursive-include django_polls/static *
recursive-include django_polls/templates *
```
* **构建模块包**
```bash
# 确保build库安装
python -m pip install build 
python -m build # 在dist/下生成模块包 
# django_polls-0.1.tar.gz  ${name}-${version}.tar.gz
#  django_polls-0.1-py3-none-any.whl.
```
* **安装到本地库**
```bash
# --user 用户模块模块包地址
python -m pip install --user django-polls/dist/django_polls-0.1.tar.gz
```
