# 总结
## 视频列表
**视频响应结构**
```json
{
    "data":[
        {
            "aweme_info": {
                "author"：{
                    "nickname": "xxx", // 作者
                }
                "desc": "", // 类似标题，但可能存在多个tag
                "video": {
                    "play_addr": {
                        "url_list":[
                            ...
                        ]
                    },
                    "download_addr":{ // 视频下载地址, 若设置禁止下载时则无效
                        "url_list":[]
                    }
                },
                "text_extra":[], // tag
                "share_info": {
                    "share_url": "" // 分享链接
                },
                
            }
        }
    ]
}
```
**视频采集**
* 可以直接通过`requests`请求`api`，但需要携带必要的`header`信息(通过网页访问获取)，如`cookie`、`referer`、`user-agent`.
  * 但代码中会暴露隐私信息，如`msToken`、`uifid`等，故不推荐

## 集合视频
**集合列表**: `aweme/v1/web/mix/list`
```json
{
    "mix_infos":[
        {
            "mix_id": "xxx", // 集合id  构成集合id  https://www.douyin.com/collection/{mix_id}|/{mix_index}
            "mix_name": "xxx", // 集合名称
        }
    ]
}
```
**集合视频列表** : `aweme/v1/web/mix/aweme`
```json
{
    "aweme_list":[
        {
            "desc": "", // 类似标题，但可能存在多个tag
            "aweme_id": "xxx", // 视频id 构成视频访问地址  https://www.douyin.com/video/{aweme_id}
            "video": {
                "play_addr": {
                    "data_size": 0, // 视频大小
                    "url_list":[
                        ...
                    ]
                }
                "format": "mp4", // 视频格式
                "download_addr":{} // 视频下载地址, 若设置禁止下载时则无效
            }
        }
    ],
    "cursor": 6, // 下次视频索引游标
}
```

# 视频采集
## 用户视频采集
**request+DrissionPage自动化**
1. 先获取用户页地址(通过网页访问)
2. 获取视频列表的请求地址(通过浏览器网络功能搜索视频响应信息，定位请求地址,响应结果携带明文，容易查询)
3. 获取请求的信息
   - `header`信息：`cookie、referer、user-agent`
4. 使用**DrissionPage自动化**监听地址(`aweme/post`)，并等待响应结果
5. 读取响应数据中的视频信息，如地址，标题，作者，点赞数，评论数，分享数，发布时间等
6. 使用`requests`携带上面获取的`header`信息请求视频地址，保存到本地
   - `title`存在任意字符，可能无法作为文件名，需要处理
```python
import requests
import re,os
from DrissionPage import ChromiumPage

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36',
    'Referer': 'https://www.douyin.com/',
    'Cookie':'...' # 根据实际情况填写
}

Google = ChromiumPage()
# Google.listen.start("aweme/post") # 监听数据包
Google.get('https://www.douyin.com/user/MS4wLjABAAAAi2oukRVcHpgD-HbVdzsxE7tYykr91YuIKukR_X_Yy08EFWRQhRrECDF6FvbvT8Xa?from_tab_name=main')
sjb = Google.listen.wait() # 等待数据包
json = sjb.response.body
data= json['aweme_list']
for i in data:
    video_url = i['video']['play_addr']['url_list'][0]
    title = i['desc'] # 视频标题存在任意字符，可能无法作为文件名，需要处理
    title_re = re.sub(r'[\\/:*?"<>|]', '', title)
    req = requests.get(video_url, headers=headers).content
    with open(f'{title_re}.mp4', 'wb') as f:
        f.write(req)
```
## 搜索视频采集
1. 通过浏览器访问视频地址
   - 根据搜索访问`https://www.douyin.com/jingxuan/search/{search_text}`(每个`search_text`对应一个`aid`，会自动携带)
   - 根据视频列表中的视频信息在网络模块中搜索响应信息，定位请求地址
     - `aweme/v1/web/general/search/stream/` 网页首次加载时的数据请求，非标准json格式，内部包含首批视频列表
     - `aweme/v1/web/general/search/single` 网页单次批量请求视频列表，标准json格式，用于加载更多
   - 获取请求信息
     - `header`信息：`cookie、referer、user-agent`
2. 使用自动化工具监听地址，并等待响应结果
3. 读取响应数据中的视频信息，如地址，标题，作者，点赞数，评论数，分享数，发布时间等
4. 使用`requests`携带上面获取的`header`信息请求视频地址，保存到本地

```python
import re,os
import requests
from DrissionPage import ChromiumPage

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36',
    'Referer': 'https://www.douyin.com/',
    'Cookie':'...' # 根据实际情况填写
}

Google = ChromiumPage()
Google.listen.start("aweme/v1/web/general/search/single") # 监听数据包（非首批）
Google.get('https://www.douyin.com/jingxuan/search/%E4%B8%80%E5%8F%A3%E6%B0%94%E7%9C%8B%E5%AE%8C%E7%B3%BB%E5%88%97')
sjb = Google.listen.wait() # 等待数据包
json = sjb.response.body
data= json['aweme_list']
for i in data:
    video_url = i['video']['play_addr']['url_list'][0]
    title = i['desc']
    title_re = re.sub(r'[\\/:*?"<>|]', '', title)
    req = requests.get(video_url, headers=headers).content
    with open(f'{title_re}.mp4', 'wb') as f:
        f.write(req)
```

**扩展**：
* `aweme/v1/web/general/search/single`接口内部支持偏移量`offset`设置，首批视频列表也可通过该接口获取，但需要设置`offset`为`0`
* 亦可通过requests直接请求api，但需要携带必要的header信息，如`cookie`、`referer`、`user-agent`.
  * 不过代码请求会暴露隐私信息，如`msToken`、`uifid`等，故不推荐
```txt
https://www.douyin.com/aweme/v1/web/general/search/single/?device_platform=webapp&aid=6383&channel=channel_pc_web&search_channel=aweme_general&enable_history=1&keyword=%E4%B8%80%E5%8F%A3%E6%B0%94%E7%9C%8B%E5%AE%8C%E7%B3%BB%E5%88%97&search_source=normal_search&query_correct_type=1&is_filter_search=0&from_group_id=&offset=0&count=10&need_filter_settings=0&list_type=single&search_id=202507272149492A9C3C0A5FE8558CC436&update_version_code=170400&pc_client_type=1&pc_libra_divert=Windows&support_h265=1&support_dash=1&cpu_core_num=16&version_code=190600&version_name=19.6.0&cookie_enabled=true&screen_width=2560&screen_height=1440&browser_language=zh-CN&browser_platform=Win32&browser_name=Edge&browser_version=138.0.0.0&browser_online=true&engine_name=Blink&engine_version=138.0.0.0&os_name=Windows&os_version=10&device_memory=8&platform=PC&downlink=10&effective_type=4g&round_trip_time=50&webid=7530477843764348457&uifid=...&msToken=...&a_bogus=...
```
## 合集视频采集
1. 获取博主合集列表`aweme/v1/web/mix/list`
2. 获取集合地址`https://www.douyin.com/collection/{mix_id}|/{mix_index}`
   - 访问后会自动跳转执行mix_index的集合视频
3. 获取集合视频列表`aweme/v1/web/mix/aweme/`
4. 解析视频信息，获取播放地址或下载地址
5. 使用requests请求视频内容，并保存至本地
6. 可使用自动化工具，保证正常用户操作，避免被平台限制
