# 问题排查
## 排查占满CPU的进程代码
### Java项目
1. 查看高占用CPU的进程ID：`top`
2. 根据PID查看高占用CPU的线程ID ：`top -H -p <PID>`
3. 将线程ID转换为16进制：`printf "%0%x\n" <TID>`
4. 查看线程ID代码输出日志：`jstack <PID> | grep <十六进制TID> -A 30`
