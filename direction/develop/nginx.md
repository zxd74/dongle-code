# 基础

## 安装
* 源码安装
* 镜像安装
### 源码安装
```shell
# 1. 安装依赖
yum install -y gcc-c++ pcre-devel openssl-devel epel-release

# 解压缩
tar -zxvf nginx-x.x.x.tar.gz 
cd nginx-x.x.x
# 编译配置
./configure --prefix=/usr/local/nginx
# 编译安装
make && make install
```
### 系统镜像
```shell
# CentOS7环境
yum install epel-release

yum search nginx # 先搜索有没有，没有可以找个安装源
# sudo curl -o /etc/yum.repos.d/centos.repo http://mirrors.aliyun.com/repo/centos-7-centosplus.repo

yum install -y nginx
```
### docker 镜像
* 镜像`docker pull nginx:latest`
* 挂载点
  * `/etc/nginx/nginx.conf` 基本配置
  * `/etc/nginx/conf.d` 服务代理配置目录`server`列表
    * `default.conf` 默认配置文件，仅包含`server`内容
  * `/usr/share/nginx/html` 默认网站根目录
  * `/var/log/nginx` 日志目录
* 启动命令`docker run -d -p 80:80 nginx`

### 配置`configure`
```txt
  --help                             print this message

  --prefix=PATH                      set installation prefix
  --sbin-path=PATH                   set nginx binary pathname
  --modules-path=PATH                set modules path
  --conf-path=PATH                   set nginx.conf pathname
  --error-log-path=PATH              set error log pathname
  --pid-path=PATH                    set nginx.pid pathname
  --lock-path=PATH                   set nginx.lock pathname

  --user=USER                        set non-privileged user for
                                     worker processes
  --group=GROUP                      set non-privileged group for
                                     worker processes

  --build=NAME                       set build name
  --builddir=DIR                     set build directory

  --with-select_module               enable select module
  --without-select_module            disable select module
  --with-poll_module                 enable poll module
  --without-poll_module              disable poll module

  --with-threads                     enable thread pool support

  --with-file-aio                    enable file AIO support

  --with-http_ssl_module             enable ngx_http_ssl_module
  --with-http_v2_module              enable ngx_http_v2_module
  --with-http_realip_module          enable ngx_http_realip_module
  --with-http_addition_module        enable ngx_http_addition_module
  --with-http_xslt_module            enable ngx_http_xslt_module
  --with-http_xslt_module=dynamic    enable dynamic ngx_http_xslt_module
  --with-http_image_filter_module    enable ngx_http_image_filter_module
  --with-http_image_filter_module=dynamic
                                     enable dynamic ngx_http_image_filter_module
  --with-http_geoip_module           enable ngx_http_geoip_module
  --with-http_geoip_module=dynamic   enable dynamic ngx_http_geoip_module
  --with-http_sub_module             enable ngx_http_sub_module
  --with-http_dav_module             enable ngx_http_dav_module
  --with-http_flv_module             enable ngx_http_flv_module
  --with-http_mp4_module             enable ngx_http_mp4_module
  --with-http_gunzip_module          enable ngx_http_gunzip_module
  --with-http_gzip_static_module     enable ngx_http_gzip_static_module
  --with-http_auth_request_module    enable ngx_http_auth_request_module
  --with-http_random_index_module    enable ngx_http_random_index_module
  --with-http_secure_link_module     enable ngx_http_secure_link_module
  --with-http_degradation_module     enable ngx_http_degradation_module
  --with-http_slice_module           enable ngx_http_slice_module
  --with-http_stub_status_module     enable ngx_http_stub_status_module

  --without-http_charset_module      disable ngx_http_charset_module
  --without-http_gzip_module         disable ngx_http_gzip_module
  --without-http_ssi_module          disable ngx_http_ssi_module
  --without-http_userid_module       disable ngx_http_userid_module
  --without-http_access_module       disable ngx_http_access_module
  --without-http_auth_basic_module   disable ngx_http_auth_basic_module
  --without-http_mirror_module       disable ngx_http_mirror_module
  --without-http_autoindex_module    disable ngx_http_autoindex_module
  --without-http_geo_module          disable ngx_http_geo_module
  --without-http_map_module          disable ngx_http_map_module
  --without-http_split_clients_module disable ngx_http_split_clients_module
  --without-http_referer_module      disable ngx_http_referer_module
  --without-http_rewrite_module      disable ngx_http_rewrite_module
  --without-http_proxy_module        disable ngx_http_proxy_module
  --without-http_fastcgi_module      disable ngx_http_fastcgi_module
  --without-http_uwsgi_module        disable ngx_http_uwsgi_module
  --without-http_scgi_module         disable ngx_http_scgi_module
  --without-http_grpc_module         disable ngx_http_grpc_module
  --without-http_memcached_module    disable ngx_http_memcached_module
  --without-http_limit_conn_module   disable ngx_http_limit_conn_module
  --without-http_limit_req_module    disable ngx_http_limit_req_module
  --without-http_empty_gif_module    disable ngx_http_empty_gif_module
  --without-http_browser_module      disable ngx_http_browser_module
  --without-http_upstream_hash_module
                                     disable ngx_http_upstream_hash_module
  --without-http_upstream_ip_hash_module
                                     disable ngx_http_upstream_ip_hash_module
  --without-http_upstream_least_conn_module
                                     disable ngx_http_upstream_least_conn_module
  --without-http_upstream_random_module
                                     disable ngx_http_upstream_random_module
  --without-http_upstream_keepalive_module
                                     disable ngx_http_upstream_keepalive_module
  --without-http_upstream_zone_module
                                     disable ngx_http_upstream_zone_module

  --with-http_perl_module            enable ngx_http_perl_module
  --with-http_perl_module=dynamic    enable dynamic ngx_http_perl_module
  --with-perl_modules_path=PATH      set Perl modules path
  --with-perl=PATH                   set perl binary pathname

  --http-log-path=PATH               set http access log pathname
  --http-client-body-temp-path=PATH  set path to store
                                     http client request body temporary files
  --http-proxy-temp-path=PATH        set path to store
                                     http proxy temporary files
  --http-fastcgi-temp-path=PATH      set path to store
                                     http fastcgi temporary files
  --http-uwsgi-temp-path=PATH        set path to store
                                     http uwsgi temporary files
  --http-scgi-temp-path=PATH         set path to store
                                     http scgi temporary files

  --without-http                     disable HTTP server
  --without-http-cache               disable HTTP cache

  --with-mail                        enable POP3/IMAP4/SMTP proxy module
  --with-mail=dynamic                enable dynamic POP3/IMAP4/SMTP proxy module
  --with-mail_ssl_module             enable ngx_mail_ssl_module
  --without-mail_pop3_module         disable ngx_mail_pop3_module
  --without-mail_imap_module         disable ngx_mail_imap_module
  --without-mail_smtp_module         disable ngx_mail_smtp_module

  --with-stream                      enable TCP/UDP proxy module
  --with-stream=dynamic              enable dynamic TCP/UDP proxy module
  --with-stream_ssl_module           enable ngx_stream_ssl_module
  --with-stream_realip_module        enable ngx_stream_realip_module
  --with-stream_geoip_module         enable ngx_stream_geoip_module
  --with-stream_geoip_module=dynamic enable dynamic ngx_stream_geoip_module
  --with-stream_ssl_preread_module   enable ngx_stream_ssl_preread_module
  --without-stream_limit_conn_module disable ngx_stream_limit_conn_module
  --without-stream_access_module     disable ngx_stream_access_module
  --without-stream_geo_module        disable ngx_stream_geo_module
  --without-stream_map_module        disable ngx_stream_map_module
  --without-stream_split_clients_module
                                     disable ngx_stream_split_clients_module
  --without-stream_return_module     disable ngx_stream_return_module
  --without-stream_upstream_hash_module
                                     disable ngx_stream_upstream_hash_module
  --without-stream_upstream_least_conn_module
                                     disable ngx_stream_upstream_least_conn_module
  --without-stream_upstream_random_module
                                     disable ngx_stream_upstream_random_module
  --without-stream_upstream_zone_module
                                     disable ngx_stream_upstream_zone_module

  --with-google_perftools_module     enable ngx_google_perftools_module
  --with-cpp_test_module             enable ngx_cpp_test_module

  --add-module=PATH                  enable external module
  --add-dynamic-module=PATH          enable dynamic external module

  --with-compat                      dynamic modules compatibility

  --with-cc=PATH                     set C compiler pathname
  --with-cpp=PATH                    set C preprocessor pathname
  --with-cc-opt=OPTIONS              set additional C compiler options
  --with-ld-opt=OPTIONS              set additional linker options
  --with-cpu-opt=CPU                 build for the specified CPU, valid values:
                                     pentium, pentiumpro, pentium3, pentium4,
                                     athlon, opteron, sparc32, sparc64, ppc64

  --without-pcre                     disable PCRE library usage
  --with-pcre                        force PCRE library usage
  --with-pcre=DIR                    set path to PCRE library sources
  --with-pcre-opt=OPTIONS            set additional build options for PCRE
  --with-pcre-jit                    build PCRE with JIT compilation support

  --with-zlib=DIR                    set path to zlib library sources
  --with-zlib-opt=OPTIONS            set additional build options for zlib
  --with-zlib-asm=CPU                use zlib assembler sources optimized
                                     for the specified CPU, valid values:
                                     pentium, pentiumpro

  --with-libatomic                   force libatomic_ops library usage
  --with-libatomic=DIR               set path to libatomic_ops library sources

  --with-openssl=DIR                 set path to OpenSSL library sources
  --with-openssl-opt=OPTIONS         set additional build options for OpenSSL

  --with-debug                       enable debug logging
```
## 命令
* 默认`nginx`命令位于`/usr/local/nginx/sbin/nginx` 建议加入`PATH`中
```shell
Usage: nginx [-?hvVtTq] [-s signal] [-c filename] [-p prefix] [-g directives]

Options:
  -?,-h         : this help
  -v            : show version and exit
  -V            : show version and configure options then exit
  -t            : test configuration and exit
  -T            : test configuration, dump it and exit
  -q            : suppress non-error messages during configuration testing
  -s signal     : send signal to a master process: stop, quit, reopen, reload
  -p prefix     : set prefix path (default: /etc/nginx/)
  -c filename   : set configuration file (default: /etc/nginx/nginx.conf)
  -g directives : set global directives out of configuration file
```

## 启动
```shell
nginx -c /etc/nginx/nginx.conf
```

# 配置
* 全局配置：nginx.conf
* 代理配置：conf.d/xxx.conf
```shell
#user  nobody; # 进程用户
worker_rlimit_nofile 65535; # 最大打开文件数
worker_rlimit_sigpending 65535; # 最大信号队列长度
worker_processes  1; # 配置进程数量，一般根据CPU数量决定
worker_cpu_affinity 01 10; # 配置CPU标识

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;

events { # 事件配置
    worker_connections  1024; # 最大连接数
}
http{
    # ...
}
```
## 代理
* 正向代理：代理客户端
* 反向代理：代理服务端
* **负载策略**：
  1. `round-robin`轮询(默认)
  2. `least-connected`:最少连接数。将下一个请求分配到连接数最少的那台服务器上。
  3. `weight`权重
  4. `ip_hash`
  5. `fair`（第三方）
  6. `url_hash`
```shell
# nginx.conf
http{
    # ...
    server{
        ###other
        location / {
            root /usr/share/nginx/html;
            index  index.html index.htm;
        }
    }
}
```

### 转发
```shell
# nginx.conf
http{
    # ...
    upstream tomcats{ # 可以做负载，集合转发、负载等功能
        least-connected # 负载策略
        server ip:port;  #设置具体的服务器
        server ip:port;  #设置具体的服务器
    }
    server{
        location / {
            proxy_pass   http://tomcats;
        }
    }
}
```
* 请求头代理
```shell
http{
    # ...
    server{
        location / {
            proxy_redirect off;
            proxy_set_header Host $host; # 此处代表，将Host设置本server的server_name，次现象只适用于localhost，其它慎重
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            client_max_body_size 10m;
            client_body_buffer_size 128k;
            proxy_buffers           32 4k;
            proxy_connect_timeout   600;
            proxy_send_timeout      600;
            proxy_read_timeout      600;
        }
    }
}
```
### 反向代理(转发实践)
作用：整合多个服务向外提供一个地址

**场景**：三个nginx服务，一台对外，两台对内
* 内uinginx配置
```shell
server{
	listen 80;
	server_name d2.dongle.com; # 注意 server_name和listen都相同代表一个server

	location /{
		root /usr/local/openresty/nginx/html;
		index index.html;
	}
}
server{
	listen 80;
	server_name d3.dongle.com;

	location /{
		root /usr/local/openresty/nginx/html;
		index index.html;
	}
}
```
* 对外主nginx配置
```shell
server{
	listen 80;
	server_name localhost;
	
	location /{
		rewrite ^$ /html last;
	}
	
	location /api{
		proxy_pass http://d2.dongle.com:80/;
        # proxy_set_header Host $host; # 此处要与d2.dongle.com配置server_name保持一致，否则请求转发失败
	}
	location /dongle{
		proxy_pass http://d3.dongle.com:80/;
	}
    #...可以如此扩展更多反向代理
}
```
**注意**：* 对于**转发请求头**`proxy_set_header Host`的设置要**慎重**
```txt
假设在主nginx中配置了proxy_set_header Host $host;，但对内两个nginx的server_name配置为d2.dongle.com和d3.dongle.com，那么当请求d2.dongle.com时，host配置与本配置的server_name一致，即localhost，但d2.dongle.com实际配置的时`d2.dongle.com`，就会导致最后转发不出去
```
* **扩展**：一台nginx代理多个web时，配置与上方一样，只不过需要将对应域名加入到本机`127.0.0.1`中
  * 另外万不可配置`proxy_set_header Host $host;`,否则会导致请求陷入死循环,无法连接
    * 如`localhost->d2.dongle.com->localhost->d2.dongle.com->...`
  * 当需要代理多个web时，需要频繁更改`default.conf`，如果域名解析满足，建议通过域名监听方式实现自适应配置。

## 跨域处理
* `add_header Access-Control-Allow-Origin` : # 允许的来源
* `add_header Access-Control-Allow-Credentials` : 设置为true，允许ajax异步请求带cookie信息
* `add_header Access-Control-Allow-Headers`
* `add_header Access-Control-Allow-Methods`;
```shell
# nginx.conf
server{
    # ...
    add_header Access-Control-Allow-Origin *; 
    add_header Access-Control-Allow-Credentials true;
    add_header Access-Control-Allow-Headers X-Requested-With;
    add_header Access-Control-Allow-Methods GET,POST,OPTIONS;
}
```
## 监控链接
需要配置nginx启动stub-status模块
```shell
# nginx.conf
server{
    location=/nginx_status{
        stub_status on;
        access_log off;
        allow 127.0.0.1;
        deny all;
    }
    # ...
}
```
## 配置长链接
```shell
# nginx.conf
http{

    # ...
    keepalive_timeout 10; # 长连接超时
    keepalive_requests 100; # 单个长连接最大请求数
    upstream tomcats{
        # ...
        keepalive 100; # 长连接数量
    }
}
```
## 日志配置
* 支持全局日志配置：位于`http`模块外，或`http`内`server`外
* 支持局部日志配置：位于`server`模块内
```shell
# nginx.conf
error_log  logs/error.log;  # 错误日志 位置 级别（notice，info，error，warn）
access_log  logs/access.log  main; # 访问日志

# 格式： 级别 格式
log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';
```
## 压缩
* `gzip`: 开启压缩
* `gzip_min_length` 设置允许压缩的页面最小字节数，页面字节数从header头中的Content-Length中获取。默认值是0，不管页面多大都压缩
  * 建议设置成大于1k的字节数，小于1k可能会越压越大。
* `gzip_buffers`:设置系统获取几个单位的缓存用于存储gzip的压缩结果数据流。4 16k 表示以16k为单位，安装原始数据大小的4倍申请内存。
* `gzip_http_version` : 压缩版本（默认1.1，前端如果是squid2.5请使用1.0）
* `gzip_comp_level`:压缩等级
* `gzip_types`: text/plain application/x-javascript text/css application/xml;
* `gzip_vary`: 是否添加 "Vary: Accept-Encoding" 响应头
* `gzip_static` : 是否对静态文件进行压缩（默认on）
* `gzip_disable`: "MSIE [1-6]\."; # IE6对Gzip不友好，禁止对这类浏览器做压缩处理
* `gzip_proxied`: expired no-cache no-store private no_last_modified no_etag auth any;

```shell
# nginx.conf
server{
    # ...
    gzip on; # 开启压缩
    #...
}
```
## 详解
```shell
####################nginx config start#######################
#定义Nginx运行的用户和用户组
#user www www;

#nginx进程数，建议设置为等于CPU总核心数。
worker_processes 4;
 
#全局错误日志定义类型，[ debug | info | notice | warn | error | crit ]
#error_log /var/log/nginx/error.log info;
error_log  logs/error.log  warn; 

#进程文件
#pid /var/run/nginx.pid;
pid   logs/nginx.pid;
 
#一个nginx进程打开的最多文件描述符数目，理论值应该是最多打开文件数（系统的值ulimit -n）与nginx进程数相除，但是nginx分配请求并不均匀，所以建议与ulimit -n的值保持一致。
worker_rlimit_nofile 65535;
 
#工作模式与连接数上限
events {
	#参考事件模型，use [ kqueue | rtsig | epoll | /dev/poll | select | poll ]; #epoll模型是Linux 2.6以上版本内核中的高性能网络I/O模型，如果跑在FreeBSD上面，就用kqueue模型。
	#use epoll; #只支持Linux 2.6以上版本
	#单个进程最大连接数（最大连接数=连接数*进程数）
	worker_connections 65535;
}
 
#设定http服务器
http {
	include mime.types; #文件扩展名与文件类型映射表,多个需要换行导入
	default_type application/octet-stream; #默认文件类型
	charset utf-8; #默认编码
	server_names_hash_bucket_size 128; #服务器名字的hash表大小
	client_header_buffer_size 512k; #上传文件大小限制
	large_client_header_buffers 4 64k; #设定请求缓
	client_max_body_size 8m; #设定请求缓
	sendfile on; #开启高效文件传输模式，sendfile指令指定nginx是否调用sendfile函数来输出文件，对于普通应用设为 on，如果用来进行下载等应用磁盘IO重负载应用，可设置为off，以平衡磁盘与网络I/O处理速度，降低系统的负载。注意：如果图片显示不正常把这个改成off。
	autoindex off; #开启目录列表访问，合适下载服务器，默认关闭。
	tcp_nopush on; #防止网络阻塞
	tcp_nodelay on; #防止网络阻塞
	keepalive_timeout 120; #长连接超时时间，单位是秒
	 
	#FastCGI相关参数是为了改善网站的性能：减少资源占用，提高访问速度。下面参数看字面意思都能理解。
	fastcgi_connect_timeout 300;
	fastcgi_send_timeout 300;
	fastcgi_read_timeout 300;
	fastcgi_buffer_size 64k;
	fastcgi_buffers 4 64k;
	fastcgi_busy_buffers_size 128k;
	fastcgi_temp_file_write_size 128k;
	 
	#gzip模块设置
	gzip on; #开启gzip压缩输出
	gzip_min_length 1k; #最小压缩文件大小
	gzip_buffers 4 16k; #压缩缓冲区
	gzip_http_version 1.0; #压缩版本（默认1.1，前端如果是squid2.5请使用1.0）
	gzip_comp_level 2; #压缩等级
	gzip_types text/plain application/x-javascript text/css application/xml;
	#压缩类型，默认就已经包含text/html，所以下面就不用再写了，写上去也不会有问题，但是会有一个warn。
	gzip_vary on;
	#limit_zone crawler $binary_remote_addr 10m; #开启限制IP连接数的时候需要使用
	 
	upstream blance{
		#upstream的负载均衡，weight是权重，可以根据机器配置定义权重。weigth参数表示权值，权值越高被分配到的几率越大。
		ip_hash; #每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题。
		#server 192.168.1.10:8080  weight=1;
		server localhost:8080  weight=3;
		server 127.0.0.1:8080  weight=3;

	}
	 
	#虚拟主机的配置
	server {
		#监听端口
		listen 80;

		#域名可以有多个，用空格隔开
		server_name localhost  127.0.0.1;

		index index.html index.htm index.php;

		#root /data/www/webapp;
		root html;

		#charset utf-8;

		#日志格式设定
		#log_format access '$remote_addr - $remote_user [$time_local] "$request" '
		#'$status $body_bytes_sent "$http_referer" '
		#'"$http_user_agent" $http_x_forwarded_for';

		#定义本虚拟主机的访问日志
		#access_log /var/log/nginx/ha97access.log access;
		#access_log logs/access.log access;

		#对 "/" 启用反向代理
		location / {
			#root   /apps/oaapp;
			#index  index.jsp index.html index.htm;
			proxy_pass http://blance;
			proxy_redirect off;
			proxy_set_header X-Real-IP $remote_addr;
			#后端的Web服务器可以通过X-Forwarded-For获取用户真实IP
			proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
			#以下是一些反向代理的配置，可选。
            #注: proxy_set_header如果对外非80端口,这里需要写上端口,例如:$host:8888,
            #否则在response.sendRedirect()时，客户端可能无法获得正确的重定向URL
			proxy_set_header Host $host;
			client_max_body_size 10m; #允许客户端请求的最大单文件字节数
			client_body_buffer_size 128k; #缓冲区代理缓冲用户端请求的最大字节数，
			proxy_connect_timeout 1; #nginx跟后端服务器连接超时时间(代理连接超时),设置为1某台服务器失效后可快速切换
			proxy_send_timeout 1; #后端服务器数据回传时间(代理发送超时),设置为1某台服务器失效后可快速切换
			proxy_read_timeout 1; #连接成功后，后端服务器响应时间(代理接收超时),设置为1某台服务器失效后可快速切换
			proxy_buffer_size 4k; #设置代理服务器（nginx）保存用户头信息的缓冲区大小
			proxy_buffers 4 32k; #proxy_buffers缓冲区，网页平均在32k以下的设置
			proxy_busy_buffers_size 64k; #高负荷下缓冲大小（proxy_buffers*2）
			proxy_temp_file_write_size 64k;
			#设定缓存文件夹大小，大于这个值，将从upstream服务器传
		}
		 
		#设定查看Nginx状态的地址
		location /nginx_status {
			stub_status on;
			access_log on;
			#auth_basic "nginx_status";
			#allow 192.168.10.0/24;
			#deny all;
			#auth_basic_user_file conf/htpasswd; #htpasswd文件的内容可以用apache提供的htpasswd工具来产生。
		}

        #所有静态文件由nginx直接读取不经过tomcat,测试有效
		#图片缓存时间设置
        # root设置注意1.必须是绝对路径2.必须将文件放到路径内,否则将找不到报错;3.在windows电脑上注意路径分隔符为/,写反了找不到文件,例如:D:/tools/nginx/webapps;
		location ~ .*.(htm|html|gif|jpg|jpeg|png|bmp|swf|ioc|rar|zip|txt|flv|mid|doc|ppt|pdf|xls|mp3|wma)$ { 
			root /webapps; 
			#expires 15d;
		}

		#JS和CSS缓存时间设置,
        #实测如果开启将会造成未知错误,例如403Forbidden,设置iphash一台机器宕机后无法自动切换等问题.
		#location ~ .*.(js|css)?$ {
		#	root /webapps; #1.必须是绝对路径;2.在windows电脑上注意路径分隔符为/,写反了找不到文件,例如:D:/tools/nginx/webapps;
		#	#expires 1h; 
		#}

		#本地动静分离反向代理配置
		#所有jsp的页面均交由tomcat处理
		#location ~ .(jsp|do|action)?$ {
		#	proxy_set_header Host $host;
		#	proxy_set_header X-Real-IP $remote_addr;
		#	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		#	proxy_pass http://blance;
		#}
		
        
		#禁止访问WEB-INF目录
		#location ~ ^/(WEB-INF)/ {  
		#	deny all;  
		#}
		#location  ~* /download/ { 
		#   root /apps/oa/fs; 
		#}
		#error_page  404              /404.html;
		# redirect server error pages to the static page /50x.html
		error_page   500 502 503 504  /50x.html;
		location = /50x.html {
			root   html;
		}
	}
}
####################nginx config end#######################
#nginx的upstream目前支持4种方式的分配
#1、轮询（默认）
#每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器down掉，能自动剔除。
#2、weight
#指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况。
#例如：
#upstream bakend {
#    server 192.168.0.14 weight=10;
#    server 192.168.0.15 weight=10;
#}
#2、ip_hash
#每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题。
#例如：
#upstream bakend {
#    ip_hash;
#    server 192.168.0.14:88;
#    server 192.168.0.15:80;
#}
#3、fair（第三方）
#按后端服务器的响应时间来分配请求，响应时间短的优先分配。
#upstream backend {
#    server server1;
#    server server2;
#    fair;
#}
#4、url_hash（第三方）
#按访问url的hash结果来分配请求，使每个url定向到同一个后端服务器，后端服务器为缓存时比较有效。
#例：在upstream中加入hash语句，server语句中不能写入weight等其他的参数，hash_method是使用的hash算法
#upstream backend {
#    server squid1:3128;
#    server squid2:3128;
#    hash $request_uri;a
#    hash_method crc32;
#}

#tips:
#upstream bakend{#定义负载均衡设备的Ip及设备状态}{
#    ip_hash;
#    server 127.0.0.1:9090 down;
#    server 127.0.0.1:8080 weight=2;
#    server 127.0.0.1:6060 max_fails=2 fail_timeout=30s;
#    server 127.0.0.1:7070 backup;
#}
#在需要使用负载均衡的server中增加 proxy_pass http://bakend/;

#每个设备的状态设置为:
#1.down表示单前的server暂时不参与负载
#2.weight为weight越大，负载的权重就越大。
#3.max_fails：允许请求失败的次数默认为1.当超过最大次数时，返回proxy_next_upstream模块定义的错误
#4.fail_timeout:max_fails次失败后，暂停的时间。
#5.backup： 其它所有的非backup机器down或者忙的时候，请求backup机器。所以这台机器压力会最轻。

#nginx支持同时设置多组的负载均衡，用来给不用的server来使用。
#client_body_in_file_only设置为On 可以讲client post过来的数据记录到文件中用来做debug
#client_body_temp_path设置记录文件的目录 可以设置最多3层目录
#location对URL进行匹配.可以进行重定向或者进行新的代理 负载均衡
```

# 扩展

## Lua模块
可以直接通过`openresty`作为nginx即可(内部包含lua模块)

[以下步骤来源](https://blog.csdn.net/qq_27156945/article/details/104019069)
1. 需要安装的模块
   * `LuaJIT`: [官网](https://www.luajit.org/) [直接下载](http://luajit.org/download/LuaJIT-2.0.4.tar.gz)
```shell
cd /usr/local
wget -c http://luajit.org/download/LuaJIT-2.0.4.tar.gz
tar xzvf LuaJIT-2.0.4.tar.gz
cd LuaJIT-2.0.4
make install PREFIX=/usr/local/luajit

#添加环境变量!
export LUAJIT_LIB=/usr/local/luajit/lib
export LUAJIT_INC=/usr/local/luajit/include/luajit-2.0
```
   * `ngx_devel_kit`
```shell
wget https://github.com/simpl/ngx_devel_kit/archive/v0.3.0.tar.gz

tar -xzvf v0.3.0.tar.gz
```
   * `lua-nginx-module`
```shell
wget https://github.com/openresty/lua-nginx-module/archive/v0.10.9rc7.tar.gz

tar -xzvf v0.10.9rc7.tar.gz
```
2. 重新编译nginx,加入以上三个模块
```shell
./configure --prefix=/usr/local/nginx \
--with-http_ssl_module \
--with-http_flv_module \
--with-http_stub_status_module \
--with-http_gzip_static_module \
--with-http_realip_module \
--with-pcre \
--add-module=/data/soft/nginx/lua-nginx-module-0.10.9rc7 \
--add-module=/data/soft/nginx/ngx_devel_kit-0.3.0 \
--with-stream

make && make install
```
