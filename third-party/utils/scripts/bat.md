* `chcp 65001` 设置编码中文

# 文件操作
* `/f` 读取文件
* `/o` 写入文件

## 按行读取
```bat
@setlocal EnableDelayedExpansion
@echo off
set file=test.txt
echo > ffmpeg.log
for /f "tokens=1,2" %%a in (%file%) do (
    set command=ffmpeg -i %%a -vcodec copy %%b.mp4
    echo !command!
)
```