# 基础
## 安装
在[Git](https://git-scm.com/)官网下载指定系统的版本安装包，安装完成后，在命令行输入`git --version`查看是否安装成功。
```shell
# 系统源安装
yum install -y git

# 源码安装
yum -y remove git
wget https://github.com/git/git/archive/refs/tags/v2.25.4.tar.gz
tar -zxvf v2.25.4.tar.gz
```

# 常用命令
```shell
git clone <repository_url> # 克隆远程仓库
git pull <remote_name> <branch_name> # 拉去远程分支
git push <remote_name> <branch_name> # 推送本地分支
git branch <branch_name> # 创建本地分支
git checkout <branch_name> # 切换分支
git checkout -b <branch_name> # 创建并切换分支
git log # 查看提交记录
git reset --hard HEAD # 撤销本地提交修改
git reset --hard <commit_id> # 撤销到指定版本
git revert <commit_id> # 撤销到指定版本，并生成新的提交
git status # 查看当前状态
git diff # 查看文件差异
git add . # 添加所有文件到暂存区
git commit -m "commit message" # 提交暂存区文件
git remote -v # 查看远程地址
git remote add <remote_name> <remote_url> # 添加远程地址
```

# 全局配置
```shell
git config --global user.name "xxx"
git config --global user.email "xxx@xxx.com"

git config --list

git config --global --unset user.name ## 删除配置
```

## 多用户登陆
主要用于同一台电脑，处理不同GIT仓库的登陆问题。(如，GitHub，GitLab，Gitee，GitCode等)
1. 清除原用户信息
```shell
git config --global --unset user.name
git config --global --unset user.email
```
2. 添加单用户信息
```shell
git config --global user.name "yourusername"
git config --global user.email "youremail@email.com"
```
3. 生成多个密钥
```shell
ssh-keygen -t rsa -C "youremail@email.com" # 根据提示，默认为id_rsa，位于~/.ssh/目录下
```
4.  ssh-agent 信任 ssh-key (默认可省略)
```shell
ssh-agent bash
ssh-add ~/.ssh/id_rsa
```
5. 添加公钥到远程仓库(各平台大致相似，设置-安全设置-SSH公钥-添加)
```shell
cat ~/.ssh/id_rsa.pub # 复制公钥内容
```
6. 在 config 文件配置多个 ssh-key
```ini
#Default gitHub user Self
Host github.com
    HostName github.com
    User git #用户名
    IdentityFile ~/.ssh/github_rsa
    #PubkeyAcceptedKeyTypes +ssh-rsa # 指定密钥类型
	
# gitee的配置
host gitee.com  # 别名,最好别改
	Hostname gitee.com #要连接的服务器
	User yourusername #用户名
	#密钥文件的地址，注意是私钥
	IdentityFile ~/.ssh/gitee_rsa
    #PubkeyAcceptedKeyTypes +ssh-rsa # 指定密钥类型
```
* **注意**：
  * 以上配置方式，使用于 `ssh:` 协议连接
  * 该方式**同样适用于单用户单机单公钥**，但是**不推荐**

# 克隆管理
## 浅克隆
* 限定深度：`git clone --depth 1`
* 全局限定深度：`git config --global clone.depth 1`
```shell
git clone --depth 1 https://github.com/xxx/xxx.git
```
## 导出指定文件
```shell
git archive --format=zip --output=xxx.zip HEAD:xxx
```

## 管理指定目录或文件
* 完整管理
* `Sparse Checkout`模式：可拉取任意目录
* `Submodule`模式: 只适用于添加新目录到主项目，不适合拉取已有项目

### `Sparse Checkout`模式(检出指定文件或目录)
```shell
# 创建仓库本地目录，并初始化
git init

# 设置浅克隆
git config core.sparseCheckout true

# 创建.git/info/sparse-checkout
touch .git/info/sparse-checkout

# 将指定文件添加到 .git/info/sparse-checkout
echo "path/to/file" >> .git/info/sparse-checkout
echo "path/to/directory/" >> .git/info/sparse-checkout

# 后续git pull或git fetch只会下载.git/info/sparse-checkout配置的内容
git remote add origin <repository_url> # 关联远程地址
git pull origin master
```
### `Submodule`模式
允许将一个仓库作为另一个仓库的子项目。
1. 将子项目仓库添加到主项目
```shell
git submodule add [子项目仓库地址] [子项目路径]
```
2. 子模块命令初始化子项目
```shell
git submodule init
```
3. 使用子模块命令更新子项目代码
```shell
git submodule update
```

## 关联多个远程地址
```shell
git remote add <remote_name> <remote_url> # 区分好remote_name即可
git remote -v # 查看远程地址
git remote rm <remote_name> # 删除远程地址

#  当然拉去/随送时也需要指定remote_name
git pull <remote_name> <branch_name>
git push <remote_name> <branch_name>
```

## SVN同步
* Git同步SVN项目初始
```shell
## 首先使用git svn clone 拉取svn项目 （）
git svn clone svn://host/path/fftmob --no-minimize-url --no-metadata -r 25923:HEAD  # 从哪个版本开始到哪个哪个版本（不过经过测试，后面的版本必须时最新版本，否则不会拉取大妈）
## 进入项目目录中
cd fftmob
## 关联git项目
git remote add origin git@gitee.com:url/test.git
## 关联远端分支（这里使用主支）
git branch -u origin/master
## 更新本地代码
git pull --allow-unrelated-histories # 由于本地和远端属于不同的两个分支，所以需要使用--allow-unrelated-histories强行合并
## 提交svn代码到git
git push origin --all
```
* SVN同步到Git
```shell
## 拉取svn最新代码
git svn fetch -rHEAD  # 结果会显示版本序列号及git本地分支

## 将git-svn代码合并到master
git merge remotes/git-svn

## 更新代码到git仓库
git push
```
* Git关联SVN
```shell
## 在git项目目录中编辑.git/config文件
[svn-remote "svn"]
        noMetadata = 1
        url = svn://test # svn项目地址
        fetch = :refs/remotes/git-svn  # svn项目关联远程分支（git-svn默认时svn master）
        
## 后续执行以下语句保持与svn项目关联
git svn fetch -r HEAD
```
* Git同步到SVN
```shell
##获取svn更新
git svn fetch # 得到svn版本号及svn分支的git版本号

## 添加待提交内容
git add .
git commit -m "" # git需要先进入提交状态，才可以执行下方语句，向svn同步

## 合并代码
git svn rebase # 版本差距较大时代码内容不同才会提示冲突，需要解决后才能提交，或者跳过

##提交git代码到svn
git svn dcommit 
## 上面存在异常，待处理
```

## 忽略文件
* 单项目配置：根项目下创建`.gitignore`文件，并添加需要忽略的文件或目录
* 全局配置：`~/.gitignore_global`文件，并添加需要忽略的文件或目录

## 差异对比
* `git diff`
* 文件差异：
  * 工作区与暂存区(默认)
  * 工作区和版本库
  * 暂存区和版本库
  * 不同版本之间
  * 比较非git的两个文件差异
```shell
git diff a.txt # 工作区与暂存区文件差异
git diff --cached a.txt # 暂存区与版本库文件差异
git diff HEAD|versionid # 工作区与版本库所有文件差异
git diff HEAD|versionid a.txt # 工作区与版本库单个文件差异
git diff V1 V2 # 不同版本之间差异
git diff a.txt b.txt # 两个文件之间差异(可以是非git文件)
``` 

## 撤销&回滚
### 撤销添加
处理在`commit`之前的`add`失误操作
```shell
git add HEAD # 撤销上一次add提交的所有文件
git add HEAD filepath # 撤销上一次add提交的某文件
```

### 项目回滚
* `git reset`: 回滚到指定版本,**适用于本地提交未推送时**
```shell
git log # 查看提交记录
git reset --hard HEAD^ # 回滚到上一个版本
git reset --hard HEAD~1 # 回滚到上一个版本
git reset --hard <commit_id> # 回滚到指定版本
```
* `git revert`: 回滚到指定版本，并生成新的提交，需要强行推送至远程(**慎重**)
  * **适用于本地提交已推送时**
  * 适用于线上紧急修复处理，否则还是不要用到
```shell
git log # 查看提交记录
git revert <commit_id> # 回滚到指定版本
git push origin <branch_name> --force # 强行推送至远程
```

# 命令
```shell
usage: git [-v | --version] [-h | --help] [-C <path>] [-c <name>=<value>]
           [--exec-path[=<path>]] [--html-path] [--man-path] [--info-path]
           [-p | --paginate | -P | --no-pager] [--no-replace-objects] [--bare]
           [--git-dir=<path>] [--work-tree=<path>] [--namespace=<name>]
           [--super-prefix=<path>] [--config-env=<name>=<envvar>]
           <command> [<args>]

These are common Git commands used in various situations:

start a working area (see also: git help tutorial)
   clone     Clone a repository into a new directory
   init      Create an empty Git repository or reinitialize an existing one

work on the current change (see also: git help everyday)
   add       Add file contents to the index
   mv        Move or rename a file, a directory, or a symlink
   restore   Restore working tree files
   rm        Remove files from the working tree and from the index

examine the history and state (see also: git help revisions)
   bisect    Use binary search to find the commit that introduced a bug
   diff      Show changes between commits, commit and working tree, etc
   grep      Print lines matching a pattern
   log       Show commit logs
   show      Show various types of objects
   status    Show the working tree status

grow, mark and tweak your common history
   branch    List, create, or delete branches
   commit    Record changes to the repository
   merge     Join two or more development histories together
   rebase    Reapply commits on top of another base tip
   reset     Reset current HEAD to the specified state
   switch    Switch branches
   tag       Create, list, delete or verify a tag object signed with GPG

collaborate (see also: git help workflows)
   fetch     Download objects and refs from another repository
   pull      Fetch from and integrate with another repository or a local branch
   push      Update remote refs along with associated objects

'git help -a' and 'git help -g' list available subcommands and some
concept guides. See 'git help <command>' or 'git help <concept>'
to read about a specific subcommand or concept.
See 'git help git' for an overview of the system.
```
