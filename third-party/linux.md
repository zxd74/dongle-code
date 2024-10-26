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
# 更换软件源
```shell
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo

yum clean all
yum makecache

yum update -y
yum install gcc-c++ wget openssl -y # 基础工具
yum lsof net-tools -y # 网络工具
```
# 第三方
* [docker](./docker.md#安装)