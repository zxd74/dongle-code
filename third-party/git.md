参数：
* `-verbose` 输出详细信息

# 克隆或拉取
## 浅克隆 --depth
```bash
git clone --depth 1 git@github.com:xxx/xxx.git
```

## 克隆指定commit
* 完全克隆方式：直接通过`git checkout <commit_id>`切换到指定commit
* 浅克隆方式: 先`fetch <commit_id>` 信息，再切换
```bash
git fetch origin <commit_id> # 完整commit_id
git checkout <commit_id>
```

## 超大文件LSF
* 普通用户每月有一定额度(若超出可以等待下月初恢复)
* 可跳过LSF拉取`GIT_LFS_SKIP_SMUDGE=1 git clone <repo address>`


# 推送
## 显式进度
增加`--progress`参数
```bash
git push -progress
```