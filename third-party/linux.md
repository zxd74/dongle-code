# 初始配置
* 修改ssh配置`/etc/ssh/sshd_config`
```txt
UseDNS no
GSSAPIAuthentication no
# 可选
ClientAliveInterval 60
ClientAliveConutMax 5
```
```shell
service sshd restart
```
* 修改安装源，已centos为例
```shell
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
# 或
curl http://mirrors.aliyun.com/repo/Centos-7.repo -o /etc/yum.repos.d/CentOS-Base.repo

yum clean all
yum makecache fast
```
# 软件源
```shell
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo

yum clean all
yum makecache

yum update -y
yum install gcc-c++ wget openssl -y # 基础工具
yum lsof net-tools -y # 网络工具
```
# 服务
## 自定义服务
* 创建服务文件`/etc/systemd/system/xxx.service`
  * `Type`:`simple|forking|oneshot|dbus|notify`
  * `ExecStart|ExecStop|ExecReload`
  * `Restart`:`no|always|on-failure|on-abort`
  * `RestartSec`:`3`
```ini
[Unit]
Description=My Custom Service # 服务描述
After=network.target # 服务依赖（再这些服务后启动本服务）
 
[Service]
Type=simple # 服务类型
ExecStart=/opt/my-service/run.sh # 启动
#ExecStop=/opt/my-service/stop.sh # [可选]停止
#ExecReload=/opt/my-service/reload.sh # [可选]重启
#Restart=on-failure # [可选]重启策略
#RestartSec=3 # [可选]重启间隔
 
[Install]
WantedBy=multi-user.target # 启动级别,多租户
```
* 检查服务`systemctl status xxx`
* 重启服务`systemctl daemon-reload`

## 自启动服务
* 服务不存在时：创建自定义服务文件
* 开启服务自启：`systemctl enable xxx`
  * 原理：在服务`/etc/systemd/system/multi-user.target.wants/`下建立自定义服务软连接
* 禁止自启动：`systemctl disable xxx`

# 第三方
* [docker](./docker.md#安装)