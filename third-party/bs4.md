```shell
pip install Beautifulsoup4
```
```python
from bs4 import BeautifulSoup
soup = BeautifulSoup(html, 'html.parser')
```

# 解析HTML
* **查找元素**
  * 查找一个：`soup.find('a')`
  * 查找所有：`soup.find_all('a')`
  * id查找：`soup.find(id='link1')`
  * **class**查找：`soup.find_all(class_='sister')`
  * 标签查找：`soup.find_all('a')`
  * 属性查找：`soup.find_all(href=re.compile('elsie'))`
* **解析script 变量**
```python
import re
# 将VARIABLE_NAME换证对应变量值，注意等号之前内容应与script内保持一致
pattern = re.compile(r"var VARIABLE_NAME = (.*?);$", re.MULTILINE | re.DOTALL)
script = soup.find("script",text=pattern)
content = pattern.search(script.prettify()).group(1)
```
