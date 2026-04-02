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
  * **Centos-Stream** ：可参考各大镜像源配置，目前参考清华源
  ```bash
  mv /etc/yum.repos.d/centos.repo  /etc/yum.repos.d/centos.repo.backup
  mv /etc/yum.repos.d/centos-addons.repo  /etc/yum.repos.d/centos-addons.repo.backup
  ```
`/etc/yum.repos.d/centos.repo`:
```ini
# /etc/yum.repos.d/centos.repo
[baseos]
name=CentOS Stream $releasever - BaseOS
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/BaseOS/$basearch/os
# metalink=https://mirrors.centos.org/metalink?repo=centos-baseos-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
countme=1
enabled=1

[baseos-debuginfo]
name=CentOS Stream $releasever - BaseOS - Debug
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/BaseOS/$basearch/debug/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-baseos-debug-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[baseos-source]
name=CentOS Stream $releasever - BaseOS - Source
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/BaseOS/source/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-baseos-source-$stream&arch=source&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[appstream]
name=CentOS Stream $releasever - AppStream
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/AppStream/$basearch/os
# metalink=https://mirrors.centos.org/metalink?repo=centos-appstream-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
countme=1
enabled=1

[appstream-debuginfo]
name=CentOS Stream $releasever - AppStream - Debug
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/AppStream/$basearch/debug/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-appstream-debug-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[appstream-source]
name=CentOS Stream $releasever - AppStream - Source
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/AppStream/source/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-appstream-source-$stream&arch=source&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[crb]
name=CentOS Stream $releasever - CRB
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/CRB/$basearch/os
# metalink=https://mirrors.centos.org/metalink?repo=centos-crb-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
countme=1
enabled=1

[crb-debuginfo]
name=CentOS Stream $releasever - CRB - Debug
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/CRB/$basearch/debug/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-crb-debug-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[crb-source]
name=CentOS Stream $releasever - CRB - Source
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/CRB/source/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-crb-source-$stream&arch=source&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0


[extras-common]
name=CentOS Stream $releasever - Extras packages
baseurl=http://mirrors.aliyun.com/centos-stream/SIGs/$stream/extras/$basearch/extras-common/
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-Extras-SHA512
gpgcheck=1
enabled=1
```
`/etc/yum.repos.d/centos-addons.repo`
```ini
# /etc/yum.repos.d/centos-addons.repo
[highavailability]
name=CentOS Stream $releasever - HighAvailability
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/HighAvailability/$basearch/os
# metalink=https://mirrors.centos.org/metalink?repo=centos-highavailability-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
countme=1
enabled=0

[highavailability-debuginfo]
name=CentOS Stream $releasever - HighAvailability - Debug
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/HighAvailability/$basearch/debug/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-highavailability-debug-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[highavailability-source]
name=CentOS Stream $releasever - HighAvailability - Source
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/HighAvailability/source/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-highavailability-source-$stream&arch=source&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[nfv]
name=CentOS Stream $releasever - NFV
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/NFV/$basearch/os
# metalink=https://mirrors.centos.org/metalink?repo=centos-nfv-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
countme=1
enabled=0

[nfv-debuginfo]
name=CentOS Stream $releasever - NFV - Debug
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/NFV/$basearch/debug/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-nfv-debug-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[nfv-source]
name=CentOS Stream $releasever - NFV - Source
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/NFV/source/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-nfv-source-$stream&arch=source&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[rt]
name=CentOS Stream $releasever - RT
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/RT/$basearch/os
# metalink=https://mirrors.centos.org/metalink?repo=centos-rt-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
countme=1
enabled=0

[rt-debuginfo]
name=CentOS Stream $releasever - RT - Debug
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/RT/$basearch/debug/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-rt-debug-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[rt-source]
name=CentOS Stream $releasever - RT - Source
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/RT/source/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-rt-source-$stream&arch=source&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[resilientstorage]
name=CentOS Stream $releasever - ResilientStorage
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/ResilientStorage/$basearch/os
# metalink=https://mirrors.centos.org/metalink?repo=centos-resilientstorage-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
countme=1
enabled=0

[resilientstorage-debuginfo]
name=CentOS Stream $releasever - ResilientStorage - Debug
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/ResilientStorage/$basearch/debug/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-resilientstorage-debug-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[resilientstorage-source]
name=CentOS Stream $releasever - ResilientStorage - Source
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/$releasever-stream/ResilientStorage/source/tree/
# metalink=https://mirrors.centos.org/metalink?repo=centos-resilientstorage-source-$stream&arch=source&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0

[extras-common]
name=CentOS Stream $releasever - Extras packages
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/SIGs/$releasever-stream/extras/$basearch/extras-common
# metalink=https://mirrors.centos.org/metalink?repo=centos-extras-sig-extras-common-$stream&arch=$basearch&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-Extras-SHA512
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
countme=1
enabled=1

[extras-common-source]
name=CentOS Stream $releasever - Extras packages - Source
baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos-stream/SIGs/$releasever-stream/extras/source/extras-common
# metalink=https://mirrors.centos.org/metalink?repo=centos-extras-sig-extras-common-source-$stream&arch=source&protocol=https,http
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-Extras-SHA512
gpgcheck=1
repo_gpgcheck=0
metadata_expire=6h
enabled=0
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