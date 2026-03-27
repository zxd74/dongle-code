```py
from DrissionPage import ChromiumPage

Google = ChromiumPage() # 创建一个ChromiumPage对象
Google.listen.start("path") # 监听数据包
Google.get(url) # 发送请求
# Goolge.post(url, data)
sjb = Google.listen.wait() # 等待数据包
data = sjb.response.body
# 数据处理...

# 释放资源
tab.close()
browser.close()
```

# 批量打开并抓取数据问题
批量打开多个类似页面，并监听相同的数据包，容易导致数据错乱

**原因**：
- DrissionPage的监听器是全局的
- 当频繁访问URL，但不关闭或释放资源就会导致数据冲突

**解决**：
* 每一个页面创建一个新标签页
* 启用标签页级别监听
```py
from DrissionPage import ChromiumPage
browser = ChromiumPage()
tab = browser.new_tab()  # 新建标签页
tab.listen.start("aweme/detail")
tab.get(prefix + video_url)
sjb = tab.listen.wait() # 等待数据包
response = sjb.response.body
# 数据处理...

# 释放资源
tab.close()
browser.close()
```
