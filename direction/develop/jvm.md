# 调优
## 时机
* Heap内存（老年代）持续上涨达到设置的最大内存值；
* Full GC 次数频繁；
* GC 停顿时间过长（超过1秒）；
* 应用出现OutOfMemory 等内存异常；
* 应用中有使用本地缓存且占用大量内存空间；
* 系统吞吐量与响应性能不高或下降。

## 目标
* 延迟：GC低停顿和GC低频率；
* 低内存占用；
* 高吞吐量;

## 步骤
* 分析系统系统运行情况：分析GC日志及dump文件，判断是否需要优化，确定瓶颈问题点；
* 确定JVM调优量化目标；
* 确定JVM调优参数（根据历史JVM参数来调整）；
* 依次确定调优内存、延迟、吞吐量等指标；
* 对比观察调优前后的差异；
* 不断的分析和调整，直到找到合适的JVM参数配置；
* 找到最合适的参数，将这些参数应用到所有服务器，并进行后续跟踪。

### 了解
* 参数
  * -Xms：初始堆大小，默认为物理内存的1/64，最小为1M；
  * -Xmx：最大堆大小，默认为物理内存的1/4，最小为2M；
  * -Xmn：年轻代大小，整个堆大小=年轻代大小+年老代大小+持久代大小；
  * -Xss：每个线程的堆栈大小；
  * -XX:NewSize：设置年轻代大小；
  * -XX:MaxNewSize：年轻代最大值；
  * -XX:PermSize：设置持久代(perm gen)初始值；
  * -XX:MaxPermSize：设置持久代最大值；
  * -XX:NewRatio：年轻代与年老代的比例；
  * -XX:SurvivorRatio：年轻代中Eden区与Survivor区的比例；
  * -XX:MaxTenuringThreshold：年轻代对象转年老代年龄；
  * -XX:GCTimeRatio：GC时间占总时间的比率，默认值为99，即最大1%的GC时间；
  * -XX:MetaspaceSize：元空间初始值，默认为20.75MB，JDK8以前叫永久代；
  * -XX:MaxMetaspaceSize：元空间最大值，默认为无限制；
  * -XX:+UseParallelGC
  * -XX:+UseParNewGC
  * -XX:+UseConcMarkSweepGC
  * -XX:+UseG1GC
  * -XX:+PrintGC
  * -XX:+PrintGCDetails
* 工具
  * java自带
    * jps：虚拟机进程状况工具；
    * jstat：虚拟机统计信息监视工具；
    * jinfo：Java配置信息工具；
    * jmap：Java内存映像工具；
    * jhat：虚拟机堆转储快照分析工具；
    * jstack：Java堆栈跟踪工具；
    * jcmd：诊断命令工具；
    * jconsole:JVM监控图形化工具；
    * jvisualvm:JVM监控图形化工具；
  * Linux命令
    * top：实时显示进程和系统占用资源；
    * vmstat：显示虚拟内存状态；
    * iostat：监视系统的磁盘I/O和CPU使用情况；
    * netstat：显示网络状态信息；
    * free：查看内存使用情况；
    * ps：显示当前进程状态；
    * kill：终止进程；

### 调优策略
* 选择合适的垃圾回收器
  * CPU单核，那么毫无疑问Serial 垃圾收集器是你唯一的选择。
  * CPU多核，关注吞吐量 ，那么选择PS+PO组合。
  * CPU多核，关注用户停顿时间，JDK版本1.6或者1.7，那么选择CMS。
  * CPU多核，关注用户停顿时间，JDK1.8及以上，JVM可用内存6G以上，那么选择G1。
* 调整内存大小 `-Xms,-XX:InitialHeapSize,-XX:MaxHeapSize,-XX:MaxNewSize`
* 设置符合预期的停顿时间 `-XX:MaxGCPauseMillis`
* 调整内存区域大小比率  `-XX:SurvivorRatio,-XX:NewRatio`
* 调整对象升老年代的年龄 `-XX:InitialTenuringThreshol`
* 调整大对象的标准    `-XX:PretenureSizeThreshold`
* 调整GC的触发时机    `-XX:CMSInitiatingOccupancyFraction, -XX:G1MixedGCLiveThresholdPercent`
* 调整JVM本地内存大小 `XX:MaxDirectMemorySize`

