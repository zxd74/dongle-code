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

# 开机自启
- 方式一：创建任务计划程序`taskschd.msc`,创建任务，触发器为**启动时**
- 方法二：启动文件夹`shell:startup`
- 方法三：注册表（普通权限，不推荐提权）
  - `HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Run`
  - 新建**字符串值** → 名称随便 → **数值填程序路径**

## 延迟开机自启
- 方式一：定时任务时触发为**启动时**时，配置高级设置`延迟任务时间`

# 权限
## 关闭管理员启动弹框警告(很重)
- **方式一**：启用管理员提升权限：启动策略`gpedit.msc`
   1. 定位`计算机配置 → Windows 设置 → 安全设置 → 本地策略 → 安全选项`
   2. 找到`用户帐户控制：管理员批准模式中管理员的提升权限提示的行为`策略，配置启用(若有选项显示是否提示选择`不提示`)
- **方式二**：**调整 UAC 滑块至最低**
  - `win+I`打开设置，搜索`更改用户账户控制设置`,并设置为**从不通知**
- **方式三**：命令行快速配置（**管理员**提权不弹窗）
  ```cmd
  # 设置管理员直接提升，不提示
  reg add "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Policies\System" /v EnableLUA /t REG_DWORD /d 1 /f
  reg add "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Policies\System" /v ConsentPromptBehaviorAdmin /t REG_DWORD /d 0 /f
  # 关闭安全桌面提示（可选）
  reg add "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Policies\System" /v PromptOnSecureDesktop /t REG_DWORD /d 0 /f
  ```

### 白名单形式关闭
- **方法一**：注册表 AppCompatFlags（原生，单应用快速配置）
  - 打开注册表`regedit`,定位`HKEY_CURRENT_USER\Software\Microsoft\Windows NT\CurrentVersion\AppCompatFlags\Layers`
  - 右键新建 字符串值，名称填目标程序完整路径（例：`D:\Tools\MyApp.exe`）
  - 双击该字符串，数值数据填 RUNASINVOKER（小写不敏感）
- **方法二**：任务计划程序（原生，多应用、开机自启场景）(**推荐**)
  - 打开任务计划程序`taskschd.msc`
  - 创建任务，配置 **使用最高权限**
  - **推荐**: 将多个应用启动方式改为使用脚本启动方式
- **方法三**：第三方工具（可视化，批量管理更省心）:`UACWhitelistTool`


# 命令行实践
## 网络控制
### 接口管理
- 查看接口信息
- 启用接口硬件(网卡)
```cmd
:: 以管理员身份运行
netsh interface show interface
netsh interface set interface name="WLAN" admin=enable # 硬件开启
netsh interface set interface "WLAN" enabled # 简化
netsh interface set interface name="WLAN" admin=disable # 关闭
```

### wifi连接
1. wifi接口硬件启用
2. wifi接口软件开启(**验证暂未通过**)
  ```cmd
  :: 以管理员身份运行
  netsh wlan set autoconfig enabled=yes interface="Wi-Fi" # 开启
  netsh wlan set autoconfig enabled=no interface="Wi-Fi" # 关闭
  ```
3. 查看wifi网卡状态`netsh wlan show interface`
4. 查看可用wifi列表 `netsh wlan show networks`
5. 新建wifi配置(**若无连接记录**)
   - 导入并连接
   ```cmd
   netsh wlan add profile filename="wifi.xml"
   ```
   ```xml
   <!-- wifi.xml -->
   <?xml version="1.0"?>
   <WLANProfile xmlns="http://www.microsoft.com/networking/WLAN/profile/v1">
    <name>WiFi名称</name>
    <SSIDConfig>
      <SSID>
        <name>WiFi名称</name>
      </SSID>
    </SSIDConfig>
    <connectionType>ESS</connectionType>
    <connectionMode>auto</connectionMode>
    <MSM>
      <security>
        <authEncryption>
          <authentication>WPA2PSK</authentication>
          <encryption>AES</encryption>
          <useOneX>false</useOneX>
        </authEncryption>
        <sharedKey>
          <keyType>passPhrase</keyType>
          <protected>false</protected>
          <keyMaterial>WiFi密码</keyMaterial>
        </sharedKey>
      </security>
    </MSM>
   </WLANProfile>
   ```
6. wifi配置管理
  ```cmd
  :: 以管理员身份运行
  netsh wlan show profiles # 查看所有已保存的wifi列表
  netsh wlan delete profile name="WiFi名称"

  netsh wlan connect name="WiFi名称" # 直连已保存的wifi
  netsh wlan disconnect
  ```