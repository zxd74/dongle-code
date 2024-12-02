**提示**：
* 部分项目在github仓库中的action中有官方打包结果，可以参考使用
* 部分项目使用了hugo框架，本地部署采用docker容器时，需确保内外访问url一致(尤其端口)，否则会被跳转，导致访问失败(暂未解决)
    * 可修改`hogo.toml`配置中的`baseURL`

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
* 构建项目：做一些本地适配
    ```Dockerfile
    # Dockerfile
    # 包含node和hugo环境
    FROM dongle/node   

    LABEL maintainer="Dongle"
    LABEL version="1.0.0"
    LABEL description="Git Docs by Dongle"
    WORKDIR /src
    # 拷贝打包后的文件
    COPY git-scm.com/ .
    RUN npm install
    RUN hugo -b http://localhost:1311  
    EXPOSE 1311
    CMD []
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
