# 浅克隆 --depth
```bash
git clone --depth 1 git@github.com:xxx/xxx.git
```

# 克隆指定commit
* 完全克隆方式：直接通过`git checkout <commit_id>`切换到指定commit
* 浅克隆方式: 先`fetch <commit_id>` 信息，再切换
```bash
git fetch origin <commit_id> # 完整commit_id
git checkout <commit_id>
```