# 桌面
## 快捷方式
### 去除小箭头
* 注册表修改：`regedit`
  * `HKEY_CLASSES_ROOT\lnkfile`,匹配`IsShortcut`删除
  * `HKEY_CLASSES_ROOT\piffile`,匹配`IsShortcut`删除
  * 需重启
* 批处理修改：管理员运行
```batch
@echo off
reg delete "HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\Shell Icons" /v 29 /f
taskkill /f /im explorer.exe
attrib -s -r -h "%userprofile%\Appideres.dll,197" /t reg_sz /f
taskkill /f /im explorer.exe
attrib -s -r -h "%userprofile%\AppData\Local\IconCache.db"
del "%userprofile%\AppData\Local\IconCache.db" /f /q
start explorer
```
* 单行命令：
```batch
cmd /k reg delete "HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\Shell Icons" /v 29 /f & taskkill /f /im explorer.exe & attrib -s -r -h "%userprofile%\AppData\Local\IconCache.db" & del "%userprofile%\AppData\Local\IconCache.db" /f /q & start explorer
```
### 恢复小箭头
* 新建注册脚本：`*.reg`
```reg
Windows Registry Editor Version 5.00
[HKEY_CLASSES_ROOT\lnkfile]
"IsShortcut"=""
[HKEY_CLASSES_ROOT\piffile]
"IsShortcut"=""
```
* 双击运行，导入注册表
* 重启电脑
