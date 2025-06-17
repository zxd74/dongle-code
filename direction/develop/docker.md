# 安装
```shell
yum remove -y docker docker-client docker-client-latest docker-common docker-latest docker-latest-logrotate docker-logrotate docker-engine

yum install -y yum-utils

# curl http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -o /etc/yum.repos.d/docker-ce.repo
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

yum install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

systemctl enable docker
systemctl start docker
```
# 私有镜像
```shell
# registry server
docker pull registry
docker run -d -p <port>:5000 --restart=always --name registry -v /data/docker/registry:/var/lib/registry registry:latest
```
```shell
# docker client
# 配置
echo "127.0.0.1 registry.server.host" >> /etc/hosts
vi /data/docker/daemon.json
{
  "insecure-registries":["<registry.server.host>:<port>"]
}

# 重启
systemctl daemon-reload
systemctl restart docker # server docker restart

# 使用
docker pull/push <registry.server.host>:<port>/reposition_name:tag
```

## 有效镜像源(持续更新 20250104)
```txt
"https://docker.unsee.tech",
"https://docker.1panel.live",
"https://hub.rat.dev",
"https://proxy.1panel.live",
"https://docker.1panel.top",
"https://docker.m.daocloud.io",
"https://docker.1ms.run",
```
* 特定容器
  * google
    ```txt
    "https://registry.aliyuncs.com/google_containers"
    "https://registry.cn-hangzhou.aliyuncs.com/google_containers"
    ```

# docker脚本
```shell
# python environment
pip install docker
```
## 获取容器列表
```py
import docker
client = docker.from_env()
containers = client.containers.list()
```
