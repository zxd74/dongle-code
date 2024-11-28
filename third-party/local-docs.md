# 本地环境部署

## React.dev
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
