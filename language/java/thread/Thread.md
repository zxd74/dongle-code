# 线程池

## Executors创建线程池
1. 可缓存无界限：newCachedThreadPool
2. 指定大小，可控最高并发：newFixedThreadPool
3. 指定大小、核心线程数、定时周期：newScheduledThreadPool
4. 单线程化：newSingleThreadExecutor

## 自定义线程池
参考Executors.newFixedThreadPool
```java

// int corePoolSize  核心线程数
// int maximumPoolSize  最大线程数
// long keepAliveTime 活跃时间，超过这个时间不活跃会自动关闭线程
// TimeUnit unit    活跃时间单位，与keepAliveTime关联
// BlockingQueue<Runnable> workQueue 线程队列
// faThreadFactory threadFactory 线程工厂，指定线程属性
// RejectedExecutionHandler handler 线程数量到达边界时的处理程序


    public static void main(String[] args) {
        int corePoolSize = 10;
        int maximumPoolSize = 60;
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.DAYS;
        BlockingQueue workQueue = new LinkedBlockingQueue<Runnable>();
        ThreadFactory threadFactory = r -> {
            Thread t = new Thread(r,"dongle thread");
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        };
        RejectedExecutionHandler handler = (r, executor) -> {
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    executor.toString());
        };

        ExecutorService dongle1 = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        // 绑定factory
        ExecutorService dongle2 = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory);
        // 绑定超出处理程序
        ExecutorService dongle3 = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory,handler);
    }
    
```
