# 多线程&并发
1. 方式：thread，runnable,callable
2. 线程状态：new,runnable,running,blocked,waiting,timed_waiting
3. 线程同步：synchronized,lock
4. 线程通信：wait,notify,notifyAll
5. 线程池：Executor,ExecutorService,ThreadPoolExecutor
6. 线程安全：synchronized,lock,volatile,final
7. 线程调度：schedule,scheduleAtFixedRate,scheduleWithFixedDelay
8. 线程中断：interrupt,interrupted
9. 线程上下文：ThreadLocal

## Thread
    无返回值，以对象形式存在的线程任务
```java
new Thread(){
    @Override
    public void run() {
        System.out.println("Thread one");
    }
}.start();
```
## Runnable
    无返回值的线程任务
```java
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable one");
            }
        }).start(); // new Thread(()-> "Runnable one").start()
```
## Callable
    带返回值的线程任务
```java
String result = new Callable<String>(){
    @Override
    public String call() throws Exception {
        return "callable one";
    }
}.call(); // ((Callable<String>)() -> "callable one").call()
System.out.println(result);
```
## Future
```java
// 创建线程池
ExecutorService executorService = Executors.newFixedThreadPool(1);
// 创建任务
Callable<String> c = new Callable<String>(){
    @Override
    public String call() throws Exception {
        return "callable one";
    }
};
// 绑定任务
Future<String> future = executorService.submit(c);
// 等待结果；阻塞式
String result = future.get();
System.out.println(result);
```

## FutureTask&Callable
```java
    FutureTask<String> task = new FutureTask<>(()->Integer.valueof("123"));
    new Thread(task).start(); // 必需或run()，开启任务，否则就单纯是个任务，不会执行
    if (task.isDone()){
        ctx.writeAndFlush(task.get());
    }
```

## CountDownLatch
    多线程同步计数器，适用于指定数量的任务，比如商品秒杀
```java
public static void main(String[] args) {
    final int count = 3;
    final CountDownLatch latch = new CountDownLatch(count);

    for (int i = 0; i < count; i++) {
        new Thread(() -> {
            // 线程执行任务
            System.out.println(Thread.currentThread().getName() + " 执行任务...");
            // 任务执行完毕，计数器减1
            latch.countDown();
        }).start();
    }

    // 等待所有任务执行完毕
    latch.await();
    System.out.println("所有任务执行完毕...");
}
```

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
