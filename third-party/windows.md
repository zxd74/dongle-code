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

## 任务栏
### 任务栏图标无法运行
* `该文件没有与之关联的应用来执行该操作。请安装应用，若已经安装应用，请在“默认应用设置”页面中创建关联`
```batch
@REM 需管理员运行

taskkill /f /im explorer.exe

reg add "HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\Shell Icons" /v 29 /d "C:\Windows\system32\imageres.dll,154" /t reg_sz /f

reg add "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\Shell Icons" /v 29 /d "C:\Windows\system32\imageres.dll,154" /t reg_sz /f

reg add "HKEY_CLASSES_ROOT\lnkfile" /v IsShortcut /t reg_sz /f

reg add "HEKY_CLASSES_ROOT\piffile" /v IsShortcut /t reg_sz /f

start explorer
```
### 任务栏图标边空白
**Tip**: 任务栏设置位于`%APPDATA%\Microsoft\Internet Explorer\Quick Launch\User Pinned\TaskBar`

**方式一**：替换新快捷方式(**临时**)
* 在任务栏目录中找到异常图标进行替换

**方式二**：更换图标
* 点击任务栏图标编辑属性进行更换图标(重新匹配目录)

**方式三**：ie4unit工具
```bash
# win7
ieuinit.exe -ClearIconCache

# win10
ie4unit.exe -show
```

**方式四：**：清楚图标缓存(全局)
```bash
taskkill /im explorer.exe /f

cd /d %userprofile%\appdata\local

del iconcache.db /a

start explorer.exe

exit
```

**其它**：重启计算机/Explorer