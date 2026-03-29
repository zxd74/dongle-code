# 初始配置
* **修改ssh配置**`/etc/ssh/sshd_config`
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
* **基础工具**
```shell
yum update -y
yum install gcc-c++ wget openssl -y # 基础工具
yum lsof net-tools -y # 网络工具
```
* 软件源(CentOS)
  ```shell
  mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
  curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo

  yum clean all
  yum makecache
  ```
  * **Centos-Stream**
  ```bash
  mv /etc/yum.repos.d/centos.repo  /etc/yum.repos.d/centos.repo.backup
  mv /etc/yum.repos.d/centos-addons.repo  /etc/yum.repos.d/centos-addons.repo.backup
  ```
  ```ini
  # /etc/yum.repos.d/centos.repo
  [baseos]
  name=CentOS Stream $releasever - BaseOS
  baseurl=https://mirrors.aliyun.com/centos-stream/$stream/BaseOS/$basearch/os/
  gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial-SHA256
  gpgcheck=1
  enabled=1
  [appstream]
  name=CentOS Stream $releasever - AppStream
  baseurl=https://mirrors.aliyun.com/centos-stream/$stream/AppStream/$basearch/os/
  gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial-SHA256
  gpgcheck=1
  enabled=1

  # /etc/yum.repos.d/centos-addons.repo
  [extras-common]
  name=CentOS Stream $releasever - Extras packages
  baseurl=http://mirrors.aliyun.com/centos-stream/SIGs/$stream/extras/$basearch/extras-common/
  gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-Extras-SHA512
  gpgcheck=1
  enabled=1
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