# 排序算法
* [冒泡排序](algorithm-sort.md#冒泡排序)
* [直接排序](algorithm-sort.md#直接排序)
* [反转排序](algorithm-sort.md#反转排序)
* [插入排序](algorithm-sort.md#插入排序)
* [希尔排序](algorithm-sort.md#希尔排序)
* [归并排序](algorithm-sort.md#归并排序)
* [快速排序](algorithm-sort.md#快速排序)
* [堆排序](algorithm-sort.md#堆排序)
* [计数排序](algorithm-sort.md#计数排序)
* [桶排序](algorithm-sort.md#桶排序)
* [基数排序](algorithm-sort.md#基数排序)

# 查找算法
- [二分法查找](algorithm-find.md#二分法查找)
- [顺序查找](algorithm-find.md#顺序查找)

# 负载均衡算法
* 任务平分类(轮询算法):各服务依次轮询使用
* 负载均衡类: 服务负载最低优先，如连接数、http请求数、cpu负载等
  * 最小连接数
  * 加权随机数
  * 加权轮询
* 性能最优类: 优先分配给响应随时间最快的服务
* Hash分类:根据任务中的某些关键信息进行Hash运算，将相同Hash请求分配到相同服务上
* 随机分类:根据随机数，随机提供服务

## nginx负载
1. 轮询(默认)
2. weight权重
3. ip_hash
4. fair（第三方）
5. url_hash