**提示**：
* 部分项目在github仓库中的action中有官方打包结果，可以参考使用
* 部分项目使用了hugo框架，本地部署采用docker容器时，需确保内外访问url一致(尤其端口)，否则会被跳转，导致访问失败(暂未解决)
    * 可修改`hogo.toml`配置中的`baseURL`


# Docker config
* env环境变量
```env
DONGLE_DOCKER_PORT=1310
DONGLE_GIT_PORT=1311
DONGLE_HUGO_PORT=1313
DONGLE_REACT_PORT=1314
DONGLE_REACT_NATIVE_PORT=1315
DONGLE_VITE_PORT=1316
DONGLE_VITE_CN_PORT=1317
DONGLE_VUE_PORT=1318
DONGLE_WEBPACK_PORT=1319
```
* compose.yml
```yaml

```

# docs.docker.com
* 克隆 ：`git@github.com:docker/docs.git`
* 构建项目
  * 修改hugo.yaml:`proxy:https://goproxy.cn,direct`,国内代理
  * 增加nginx配置文件`default.conf`，用于代理
  * 修改Dockerfile：适应本地部署，并补充DOCS_URL
    ```Dockerfile
    # syntax=docker/dockerfile:1

    # GO_VERSION sets the Go version for the base stage
    ARG GO_VERSION=1.22

    # base is the base stage with build dependencies
    FROM golang:${GO_VERSION}-alpine AS base
    WORKDIR /src
    RUN apk --update add nodejs npm git gcompat

    # node installs Node.js dependencies
    FROM base AS node
    COPY package.json .
    ENV NODE_ENV=production
    RUN npm config set registry https://registry.npmmirror.com
    RUN npm install

    # hugo downloads and extracts the Hugo binary
    FROM base AS hugo
    ARG HUGO_VERSION=0.127.0
    ARG TARGETARCH
    WORKDIR /tmp/hugo
    COPY hugo_extended_0.127.0_linux-amd64.tar.gz hugo.tar.gz
    RUN tar -xf "hugo.tar.gz" hugo

    # build-base is the base stage for building the site
    FROM base AS build-base
    COPY --from=hugo /tmp/hugo/hugo /bin/hugo
    COPY --from=node /src/node_modules /src/node_modules
    COPY . .

    # build creates production builds with Hugo
    FROM build-base AS build
    ARG HUGO_ENV=production
    # 配置的地址和外部访问地址应完全一致，否则跳转失败
    ARG DOCS_URL=http://localhost:1310
    RUN hugo --gc --minify -e $HUGO_ENV -b $DOCS_URL

    # deploy on nginx
    FROM nginx 
    COPY --from=build /src/public /usr/share/nginx/html
    COPY default.conf /etc/nginx/conf.d/default.conf
    EXPOSE 1310
    ```
  * 容器部署
    ```shell
    docker build -t dongle/docker-docs .
    docker run -d --name docker-docs -p 1310:1310 dongle/docker-docs
    ```
* 访问：`http://localhost:<port>`
* 提示：
  * docker官方仓库中有镜像`docs/docker.github.io`，可以参考使用,不过不再持续更新了

# git-scm.com
* 克隆仓库：`git@github.com:git/git-scm.git`
* 下载hugo: `hugo_extended_0.139.3_linux-amd64.tar.gz`
* 构建项目：做一些本地适配
    ```Dockerfile
    FROM dongle/node AS base
    WORKDIR /src
    RUN apk --update add gcompat # 重要，否则hugo即使在/bin也不会被发现

    FROM base AS hugo
    ARG HUGO_VERSION=0.139.3
    WORKDIR /tmp/hugo
    COPY hugo_extended_0.139.3_linux-amd64.tar.gz hugo.tar.gz
    RUN tar -xf "hugo.tar.gz" hugo

    FROM base AS build-base
    COPY --from=hugo /tmp/hugo/hugo /bin/hugo
    COPY git-scm/ .

    FROM build-base AS build
    RUN hugo

    FROM base
    COPY --from=build /src/public /src/public
    COPY --from=build /src/script /src/script
    EXPOSE 1311
    CMD ["node","script/serve-public.js"]
    ```
    ```shell
    docker build -t dongle/git-docs .
    docker run -d --name git-docs -p 1311:1311 dongle/git-docs  # 端口映射内外需要一致
    ```
* 访问：`http://localhost:1311`

# React.dev
1. 克隆仓库：`git@github.com:reactjs/react.dev.git` (`84f29eb20af17e9c154b9ad71c21af4c9171e4a2`)
2. 调整项目
    ```shell
    # 关闭增加部分文件ESlint校验(构建时会异常)
    vi .eslintignore
    **/HomeContent.js
    **/CodeDiagram.tsx

    # 为`/errors/default`路径添加对应文件(存在就无需处理了)
    cp src/content/errors/index.md src/content/errors/default.md

    # 修改`/community/translations`对应文件(translations可能是某些插件的内部成员)
    cp src/content/community/translations.md src/content/community/translations/translations.md
    # 同理修改各调用方的调用路径`/community/translations` 改为`/community/translations-doc`
    vi .sidebarCommunity.tsx
    vi src/components/Layout/TopNav/TopNav.tsx

    # 下载 https://raw.githubusercontent.com/facebook/react/main/scripts/error-codes/codes.json 存储为src/pages/errors/error-codes.json
    # 并修改 src/pages/errors/[errors].tsx中获取errorcodes的方式
    vi src/pages/errors/[errors].tsx
    # 导入error-codes.json
    import * as ErrorCodes from './error-codes.json';
    # 修改 let cachedErrorCodes: Record<string, string> | null = null;
    let cachedErrorCodes: Record<string, string> | null = ErrorCodes;

    # 删除patches目录
    rm -rf patches
    # 删除原依赖锁定(便于通过代理下载依赖)
    rm -f yarn.lock
    ```
3. 构建打包：为`next.config.js`配置`output:export`, 执行`yarn build`
   1. next v10之前需要额外使用`yarn export`命令才能生成静态HTML(也需要在`package.json`绑定脚本)
   2. 打包生成`out`目录
4. 部署(docker部署)
    ```Dockerfile
    # Dockerfile
    FROM nginx
    
    LABEL maintainer="Dongle"
    LABEL version="1.0.0"
    LABEL description="React Site by Dongle"
    WORKDIR /usr/share/nginx/html
    # 拷贝打包后的文件
    COPY out/ .
    ```
    ```shell
    docker build -t dongle/react-docs .
    docker run -d --name react-website -p <port>:80 dongle/react-docs

    # 注意，如果外部访问，需要对外开放<port>端口
    ```
5. 访问: `http://<host|ip>:<port>`

# redis.io
* 克隆仓库：`git@github.com:redis/redis-doc.git` (`v3.0.0`)
* 下载hugo: `hugo_extended_0.111.2_linux-amd64.tar.gz`
* 调整项目：做一些本地适配
  * 修改`redis-docs/build/components/component.py`,`def _git_clone`方法中取消`run(f'git fetch --all --tags', cwd=to)`内容
  * 修改`config.toml`配置`baseURL`为合适地址，如`http://localhost:1321`
  * 准备`sources.list`文件，使其采用国内源代理(python镜像基础系统debina)
  * 准备`default.conf`文件，修改端口监听与准备地址端口一致
* 构建项目
```Dockerfile
FROM python AS base 
COPY sources.list /etc/apt/sources.list
WORKDIR /src
RUN pip config set global.index-url https://mirrors.aliyun.com/pypi/simple
RUN apt-get update 
RUN apt-get install -y nodejs npm rsync
#RUN pipx ensurepath
RUN git config --global http.sslverify false
RUN echo '20.205.243.166 github.com' >> /etc/hosts
RUN npm config set registry https://registry.npmmirror.com

FROM base AS hugo
WORKDIR /tmp/hugo
COPY hugo_extended_0.111.2_linux-amd64.tar.gz hugo.tar.gz
RUN tar -xf "hugo.tar.gz" hugo

FROM base AS build-base
COPY --from=hugo /tmp/hugo/hugo /bin/hugo
COPY redis-docs .

FROM build-base AS build
RUN make 

FROM nginx
COPY --from=build /src/public usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/
```
```shell
docker build -t dongle/redis-docs .
# port 必需与配置的baseUrl中的port一致，否则跳转异常
docker run -d --name redis-website -p <port>:<port> dongle/redis-docs
```
* **注意**：认真阅读`redis-docs/README`，了解构建准备工作（hugo必需是`v0.111.2`版本）

# tailwindcss.com
* `git clone git@github.com:tailwindlabs/tailwindcss.com.git`
* 调整项目
  * 修改`next.config.js`
    ```txt
    export default {
        //...
        images:{unoptimized: true} // 允许使用非优化图片
    }
    ```
* 项目构建部署
    ```Dockerfile
    FROM dongle/node AS build 
    WORKDIR /src
    # RUN npm config set registry https://registry.npmmirror.com # 原始node镜像需配置代理仓库
    COPY tailwindcss.com/ .
    RUN npm install
    RUN npm run export

    FROM nginx
    COPY --from=build /src/out  /usr/share/nginx/html
    ```
    ```shell
    docker build -t dongle/tailwindcss-docs .
    docker run -d --name tailwindcss-website -p <port>:80 dongle/tailwindcss-docs
    ```
* 访问: `http://<host|ip>:<port>`