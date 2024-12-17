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

# react.dev
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
    cp src/content/community/translations.md src/content/community/translations-doc.md
    # 同理修改各调用方的调用路径`/community/translations` 改为`/community/translations-doc`
    vi .sidebarCommunity.tsx
    vi src/components/Layout/TopNav/TopNav.tsx

    # 下载 https://raw.githubusercontent.com/facebook/react/main/scripts/error-codes/codes.json 存储为src/pages/errors/codes.json
    # 并修改 src/pages/errors/[errors].tsx中获取errorcodes的方式
    vi src/pages/errors/[errors].tsx
    import * as ErrorCodes from './codes.json';
    let cachedErrorCodes: Record<string, string> = ErrorCodes;

    # 对scripts/downloadFonts.mjs的下载功能做try..catch处理，防止下载失败导致构建失败

    # 删除部分文件
    rm -rf patches yarn.lock

    # 由于需要导出静态html，额外做一些配置
    vi next.config.js
    export: {
        output:'export',
        images:{unoptimized:true,},
    },
    ```
3. 构建打包：为`next.config.js`配置`output:export`, 执行`yarn build`
   1. next v10之前需要额外使用`yarn export`命令才能生成静态HTML(也需要在`package.json`绑定脚本)
   2. 打包生成`out`目录
4. 部署(docker部署)
    ```Dockerfile
    FROM dongle/node AS build
    RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
    RUN apk add curl
    RUN yarn config set registry https://registry.npmmirror.com
    WORKDIR /src
    COPY react.dev .
    RUN yarn install
    RUN yarn add react@0.0.0-experimental-16d053d59-20230506 react-dom@0.0.0-experimental-16d053d59-20230506
    RUN yarn build

    FROM nginx:alpine
    COPY --from=build /src/out /usr/share/nginx/html
    ```

## reactnative.dev
* 克隆仓库：`git@github.com:reactnative/react-native-website.git`
* 调整项目：做一些本地适配
  * 修改`website/package.json`中`build`脚本，只执行`docusaurus build`即可
* 构建项目镜像
    ```Dockerfile
    FROM dongle/node AS build
    RUN yarn config set registry https://registry.npmmirror.com
    WORKDIR /src
    COPY react-native-website .
    RUN yarn install
    RUN yarn build

    FROM nginx:alpine
    COPY --from=build /src/website/build /usr/share/nginx/html
    ```

# redis.io
* 克隆仓库：`git@github.com:redis/redis-doc.git` (`v3.0.0`)
* 下载hugo: `hugo_extended_0.111.2_linux-amd64.tar.gz`
* 调整项目：做一些本地适配
  * 准备`sources.list`文件，使其采用国内源代理(python镜像基础系统debina)
  * 修改Makefile，为components添加`--skip-clone`，将以下克隆仓库提前克隆
    * 修改`build/components/component.py#ALL.__init__`方法中`self._skip_clone = self.get('skip_clone')`为`self._skip_clone = args.get('skip_clone')`（可能时bug还未修复）
    ```Makefile
    components:
        @python3 build/make.py --skip-clone
    hugo:
	    @hugo $(HUGO_DEBUG) $(HUGO_BUILD) -b /
    ```
  * 提前克隆`example`仓库：详情见`data/components/index.json#clients`,具体地址见`data/components/{name}.json#examples.git_uri`
    ```shell
    git clone https://github.com/redis/NRedisStack.git
    git clone https://github.com/redis/go-redis.git
    git clone https://github.com/redis/node-redis.git
    git checkout emb-examples # 对node-redis切换emb-examples分支

    git clone https://github.com/redis/jedis.git
    git clone https://github.com/redis/lettuce.git
    git checkout doctests # 对lettuce切换doctests分支

    git clone https://github.com/redis/redis-vl-python.git
    git clone https://github.com/redis/redis-py
    ```
  * 修改layout目录中html中定义的部分本地url跳转：因hugo会将所有页面生成对应文档，其内包含index.html，若访问的URL后没有根`/`，则会直接找对应文件，而不是目录中的index.html
    * 注意：仅限`layout`目录
    ```txt
    正则：(\{\{ .Scratch.Get "path" \}\}/.*[^/])(') =》 $1/'
    正则：("./.*[^/])(") =》 $1/"
    ```
    * 另外对于以`https://redis.io/docs/`和`https://redis.io/docs/latest`开头的链接，是本项目的链接，可移除前缀，其它不变
* 构建项目
    ```Dockerfile
    FROM dongle/node AS base
    RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
    RUN apk add make python3 py3-pip git bash rsync gcompat 
    RUN mv /usr/lib/python3.12/EXTERNALLY-MANAGED /usr/lib/python3.12/EXTERNALLY-MANAGED.bk
    RUN pip config set global.index-url https://mirrors.aliyun.com/pypi/simple
    WORKDIR /src

    FROM base AS hugo
    WORKDIR /tmp/hugo
    COPY hugo_extended_0.111.2_linux-amd64.tar.gz hugo.tar.gz
    RUN tar -xf "hugo.tar.gz" hugo

    FROM base AS build-base
    COPY --from=hugo /tmp/hugo/hugo /bin/hugo
    COPY redis-docs .
    COPY clients/ /tmp/

    FROM build-base AS build
    RUN make 

    FROM nginx:alpine
    COPY --from=build /src/public usr/share/nginx/html
    ```
* **注意**：认真阅读`redis-docs/README`，了解构建准备工作（如hugo必需是`v0.111.2`版本）

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

# nodejs.org
* `git clone git@github.com:nodejs/nodejs.org.git`
* 调整项目: 因下载相关模块需要关联访问一些国外网站
  * 因环境问题
    * 将本地下载地址转到官网地址: `/download`=> `https://nodejs.org/en/download`
    * 将本地博客转到官网地址：`/blog`=> `https://nodejs.org/en/blog`
  * 因静态化操作比较复杂，同样会涉及外网，故不做静态处理
  * 修改`package.json`: 为`dev`脚本绑定端口
    ```json
    //...
    "scripts": {
        "dev": "cross-env NODE_NO_WARNINGS=1 next dev --turbopack -p 1312",
        //...
    }    
    ```
* 项目构建部署
    ```Dockerfile
    FROM dongle/node
    WORKDIR /src
    COPY nodejs.org/ .
    RUN npm config set registry https://registry.npmmirror.com
    RUN npm install
    EXPOSE 1312 # 与pakcage.json中dev命令绑定的端口一致
    ENTRYPOINT ["npm", "run","dev"]
    ```
    ```shell
    docker build -t dongle/nodejs.org .
    docker run -d --name nodejs.org -p 1312:1312 dongle/nodejs.org
    ```

# mongodb.com
* `git clone git@github.com:mongodb/docs.git`
* 调整项目
  * 修改requirements.txt: 指定sphin版本，参考`https://github.com/mongodb/docs-tools/requirements.txt`
* 构建项目(python2环境)
  * 构建环境Dockerfile
    ```Dockerfile
    FROM python:2 AS build
    RUN pip config set global.index-url https://mirrors.aliyun.com/pypi/simple
    COPY hosts /etc/hosts
    COPY sources.list /etc/apt/sources.list
    WORKDIR /src
    COPY docs/ .
    # 推荐启动容器，在内部执行构建过程，存在部分问题需要解决
    #RUN apt update && apt install vim rsync
    #RUN pip install -r requirements.txt
    #RUN make html

    #FROM nginx:alpine
    #COPY --from=build /src/build/master/html /usr/share/nginx/html
    ```
  * 容器内构建
    ```shell
    python2 -m pip install giza # 注意giza包依赖的部分版本比实际限制版本高，需要查看docs-tools/giza/requirements.txt重新安装冲突依赖
    echo "185.199.109.133 raw.githubusercontent.com" >> /etc/hosts
    make html # 构建可能需要多次，原因未知，这也是不直接通过Dockerfile直接构建的原因
    # 打包成功后
        # 在 build/master/html 目录为为打包为html的结果
        # 在 build/public/master/manual-master.tar.gz 为build/master/html的打包结果
    ```
  * 部署新Dockerfile
    ```Dockerfile
    FROM nginx
    COPY html /usr/share/nginx/html
    ```

# vuejs.org
* 克隆项目
  * v2.x：`git clone git@github.com:vuejs/v2.vuejs.org.git`
  * v3.x: `git clone git@github.com:vuejs/docs.git`
    * cn版：`git@github.com:vuejs-translations/docs-zh-cn.git`
* 构建项目
  * 2.x构建，采用`hexo`构建,默认输出路径为 `public`
    ```Dockerfile
    FROM dongle/node AS build
    WORKDIR /src
    # Vue2.x强制node>=18 and npm=10.9.2版本,并且构建时需要git功能
    COPY v2.vuejs.org/ . 
    RUN apk add --no-cache git
    RUN npm install -g npm@10.9.2

    RUN npm install && npm build

    FROM nginx:alpine
    COPY --from=build /src/public/ /usr/share/nginx/html/
    ```
  * 3.x构建，采用`vitepress`构建,默认输出路径为 `.vitepress/dist`
    ```Dockerfile
    FROM dongle/node AS build
    WORKDIR /src
    COPY docs .
    RUN npm install -g pnpm@9.1.4 # 指定pnpm版本
    RUN pnpm install
    RUN pnpm run build

    # vue3.x 强制使用pnpm包管理，并且采用vitepree构建项目，默认输出路径为 .vitepress/dist
    FROM nginx:alpine
    COPY --from=build /src/.vitepress/dist /usr/share/nginx/html
    ```
# sphinx-docs.org
* `git clone git@github.com:sphinx-doc/sphinx-docs.git`
* 构建项目
  * 构建环境Dockerfile
    ```Dockerfile
    FROM dongle/node AS build
    RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
    RUN apk add make python3 py3-pip 
    RUN mv /usr/lib/python3.12/EXTERNALLY-MANAGED /usr/lib/python3.12/EXTERNALLY-MANAGED.bk
    RUN pip install docutils sphinx
    WORKDIR /src
    COPY sphinx-master/ .
    RUN npm install
    RUN make docs target=html

    FROM nginx:alpine
    COPY --from=build /src/doc/_build/html /usr/share/nginx/html
    ```

# dubbo
* `git clone git@github.com:apache/dubbo-website.git`
* 构建项目
  * 构建环境Dockerfile
    ```Dockerfile
    FROM dongle/node AS base
    RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
    RUN apk add go git gcompat
    RUN go env -w GO111MODULE=on && go env -w  GOPROXY=https://goproxy.cn,direct
    WORKDIR /src

    FROM base AS hugo
    COPY hugo_extended_0.139.3_linux-amd64.tar.gz hugo.tar.gz
    RUN tar -zxvf hugo.tar.gz -C /tmp/hugo

    FROM  base AS build
    COPY --from=hugo /tmp/hugo/hugo /bin/hugo
    COPY dubbo-website .
    RUN npm install
    RUN hugo -b /

    FROM nginx:alpine
    COPY public /usr/share/nginx/html
    ```

# django
* `git clone git@github.com:django/django.git`
* 构建项目
  * 构建环境Dockerfile
    ```Dockerfile
    FROM dongle/python:alpine AS build
    RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
    RUN pip config set global.index-url https://mirrors.aliyun.com/pypi/simple
    RUN apk add make
    WORKDIR /src
    COPY  django/docs .
    RUN pip install -r requirements.txt
    RUN make html

    FROM nginx:alpine
    COPY --from=build /src/_build/html /usr/share/nginx/html
    ```

## docs.django.com
* `git clone git@github.com:django/django.git`
* 构建项目
  * 构建环境Dockerfile
```Dockerfile
```

# go-docs
* `git clone git@github.com:go/website.git`
* 构建项目
  * 构建环境Dockerfile
    ```Dockerfile
    FROM dongle/golang # alpine
    WORKDIR /src
    COPY go-website .
    RUN go mod tidy
    EXPOSE 80
    CMD ["go","run","./cmd/golangorg","-http","0.0.0.0:80"]
    ```

# docusaurus
* `git clone git@github.com:facebook/docusaurus.git`(3.6.3)
  * 最好从github仓库下载tag已经发布的版本，不要使用`git clone`命令，否则会出现构建失败的问题(部分环境问题)
    * 例如`website/_dogfooding/_asset-tests/image with spaces.png`空格问题，导致引用失败(需要转码引用，如空格转%20)
* 环境：`node>=18`(推荐18)
* 调整项目
  * 删除`yarn.lock`文件(加速)
  * 将`package.json`中的`workspaces`去除`packages/*`内容，完全依赖官方资源库(本地构建存在bug)
  * 将`website/docusaurus.config.ts`中的`svgr`相关配置移除(docusaurus暂不支持)
* 构建项目
  * 构建环境Dockerfile
    ```Dockerfile
    FROM dongle/node:18-alpine AS build
    # node 22需要额外安装(会因环境不同触发不同编译) add python3 py3-pip make gcc g++ vips
    # RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
    # RUN apk add python3 py3-pip make gcc g++ vips
    RUN yarn config set registry https://registry.npmmirror.com
    COPY docusaurus /src
    WORDIR /src/website
    RUN yarn install
    RUN yarn add @docusaurus/faster @docusaurus/logger
    RUN yarn build

    FROM nginx:alpine
    COPY --from=build /src/website/build /usr/share/nginx/html/
    ```

# k8s website
* `git clone git@github.com:kubernetes/website.git`
* 准备工作: 可省略`make module-init`操作(`git clone github`)
  * `https://github.com/kubernetes-sigs/reference-docs/` 保存为`website/api-ref-generator`
  * `https://github.com/google/docsy`保存为`website/theme/docsy`
  * `https://raw.githubusercontent.com/kubernetes-sigs/downloadkubernetes/master/dist/release_binaries.json`也可考虑下载到本地,修改`website/layouts/shortcodes/release-binaries.html`中的引用地址为本地即可
* 构建项目
```Dockerfile
FROM dongle/golang AS base
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
RUN apk add build-base libc6-compat git rsync npm
RUN go env -w GO111MODULE=on
RUN go env -w  GOPROXY=https://goproxy.cn,direct
WORKDIR /src

FROM base AS hugo
WORKDIR /tmp/hugo
COPY hugo_extended_0.139.3_linux-amd64.tar.gz hugo.tar.gz
RUN tar -xf "hugo.tar.gz" hugo

FROM base AS node
RUN npm config set registry https://registry.npmmirror.com
COPY website/package.json .
RUN npm install

FROM base AS build-base
COPY --from=hugo /tmp/hugo/hugo /bin/hugo
COPY --from=node /src/node_modules /src/node_modules
COPY website .

FROM build-base AS build
# 若已提前下载github资源,可省略次不住
RUN make module-init
RUN hugo --cleanDestinationDir --minify --environment development -b /

FROM nginx:alpine
COPY --from=build /src/public /usr/share/nginx/html
```

# angular.dev
* `git clone git@github.com:angular/angular.git`
* 构建项目: **由于网络限制过多，实际无法本地构建**
  * 可直接在`github`上下载官方构建`Build adev for preview deployment`结果`adev-preview.zip`
```Dockerfile
FROM dongle/node:20 AS build
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
RUN apk add git
RUN npm install -g @angular/cli
WORKDIR /src
COPY angular .
RUN yarn
RUN yarn docs:build

FROM nginx:alpine
COPY --from=build /src/ /usr/share/nginx/html
```
**注意**: 由于本项目依赖过多github.com资源,建议在可访问外网环境下构建
