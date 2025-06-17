# Git管理
## 全局配置
```shell
$ git config --global user.name “Your name” //配置用户名
$ git config --global user.email “Your Email address”  //配置用户邮箱地址
$ git config user.name   //查看用户名
$ git config user.email  //查看用户邮箱地址
$ git config -l          //查看所有配置
```
## 创建密钥
```shell
ssh-keygen -t rsa -C "your email address"
# 将结果的.pub内容作为公钥直接使用
```

## 多账号处理
```shell
# 在~/.ssh/下创建config文件，按一下格式配置
host github.com  #别名，随便定 后面配置地址有用
    Hostname github.com #要连接的服务器
    User ontheroadtomine #用户名
    IdentityFile ~/.ssh/id_rsa  #密钥文件的地址，注意是私钥
    PubkeyAcceptedKeyTypes +ssh-rsa # 新版配置，否则不再使用rsa算法
```

## 忽略文件
* 单项目配置：根项目下创建`.gitignore`文件，并添加需要忽略的文件或目录
* 全局配置：`~/.gitignore_global`文件，并添加需要忽略的文件或目录

# 源码管理
## 撤销
处理在`commit`之前的`add`失误操作
```shell
git add HEAD # 撤销上一次add提交的所有文件
git add HEAD filepath # 撤销上一次add提交的某文件
```

## 项目回滚
* `reset`模式：旨在删除原来commit，重新提交commit（一般的master分支是受保护的，不允许删除提交）
* `revert`模式：旨在回滚某一个版本的提交（推荐，安全回滚）

### `reset`模式
适用于本地commited但未推送至远程仓库的情况
`git reset --hard <commit id>`

### `revert`模式
* 普通回滚：`git revert -n <commit id>`
* 合并回滚：`git revert -n <commit id> -m 2 (m参数1是合并后的，2为合并前的)`
* **注意**：当前版本与需要回滚的版本之间如果存在代码合并，需要加上回滚合并，否则只能回滚之前版本提交的文件
  
注意：重新提交代码后，需要强制覆盖远程:`git push -f orgin master`


## 克隆指定分支
```shell
git clone -b branchA url

git checkout 版本号
## 后在该版本新建分支即可
```

## 管理指定目录或文件
* 完整管理
* `Sparse Checkout`模式：可拉取任意目录
* `Submodule`模式: 只适用于添加新目录到主项目，不适合拉取已有项目

### `Sparse Checkout`模式
1. 初始化本地项目仓库 
    ```shell
    git init
    ```
2. 关联远程仓库
    ```shell
    git remote add origin [远程仓库地址]
    ```
3. 配置`Sparse Checkout`模式 
    ```shell
    git config core.sparsecheckout true
    ```
4. 编辑`.git/info/sparse-checkout`文件，指定需要管理的目录
    ```shell
    vi .git/info/sparse-checkout
    # 如只管理`src`目录
    /src 
    # echo project-file-path >> .git/info/sparse-checkout
    ```
5. 拉取远程仓库代码
    ```shell
    git pull origin [分支名]
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

## 多源项目
1. 关联多个远程地址
```shell
## git remote add github git@gitee.com:code/demo.git # 源仓库
git remote add gitee git@gitee.com:repos/code/demo.git # 新仓库,指定仓库名为gitee
```
 * 可观察`.git/config`配置
    ```shell
    [core]
        repositoryformatversion = 0
        filemode = false
        bare = false
        logallrefupdates = true
        symlinks = false
        ignorecase = true
    [remote "origin"]
        url = git@github.com:repos/code/demo.git
        fetch = +refs/heads/*:refs/remotes/origin/*
    [branch "main"]  # github这里是main分支，gitee这里是master分支
        remote = origin
        merge = refs/heads/main
    [remote "gitee"] # 一般情况可以根据git remote时指定名称
        url = git@gitee.com:repos/code/demo.git
        fetch = +refs/heads/*:refs/remotes/gitee/*
    ```
2. 管理源码： 除默认源外，其它源同步需要明确源名称
```shell
git push  # 推送到默认origin仓库

git push gitee # 推送到指定仓库gitee,
```
