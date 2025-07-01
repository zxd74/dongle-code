# 自动读取文件下载
```bat
@REM windows批量下载
@setlocal EnableDelayedExpansion
@echo off
chcp 65001
set file=test.txt
echo > ffmpeg.log
for /f "tokens=1,2" %%a in (%file%) do (
    set command=ffmpeg -i %%a -vcodec copy %%b.mp4
    echo !command! >> ffmpeg.log
    call !command! >> ffmpeg.log
)
echo 'complate'
```

# 视频转换
```bat
@echo off
set /p path=input file's path
set /p file=input filename or file's regular
set tagert= %path%\%file%
if "%ffmpeg%"=="" set ffmpeg=ffmpeg
echo start video convert ......
for %%a in (%ext%) do (
	echo converting: %%a
	ffmpeg -loglevel quiet -y -i "%%a" -vcodec copy "%%~na.mp4"
)
echo convert is end
pause
```