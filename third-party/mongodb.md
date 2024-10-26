# 安装
* 创建repo文件`/etc/yum.repos.d/mongodb-org-6.0.repo`
```txt
[mongodb-org-6.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/7/mongodb-org/6.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-6.0.asc
```
* 安装
```shell
yum install -y mongodb-org
```
* 启动
```shell
service mongod start
```

## 卸载
```shell
sudo yum remove -y mongodb-org mongodb-org-database mongodb-org-server mongodb-mongosh mongodb-org-mongos mongodb-org-tools
```