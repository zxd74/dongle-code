面试题(代码部分)整理


# 给定一组IP列表，最大并发N，求最短RTT时间的IP
**思路**
* 基础：单线程，分别请求，对比RTT时间，找最短
* 优化1：多线程并发，分组请求，找最短，继续下一组，直到完成，对比最短
* 优化2：多线程并发，分组请求，最短返回后终止分组，继续下一组，直至完成，对比最短
* 优化3：多线程并发，分组请求，最短返回后终止分组，继续下一组时，增加超时为上一组最短RTT时间，直至完成
```java
public class FastestIpFinder {

    // 模拟RTT请求（可替换为真实逻辑）
    private long simulateRttRequest(String ip) throws InterruptedException {
        Thread.sleep((long) (Math.random() * 1000)); // 模拟延迟
        return (long) (Math.random() * 1000); // 模拟RTT
    }

    public String findFastestIp(List<String> ipList, int maxConcurrency) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        String fastestIp = null;
        long timeout = Long.MAX_VALUE; // 初始无超时

        // 按maxConcurrency分组处理
        for (int i = 0; i < ipList.size(); i += maxConcurrency) {
            List<String> group = ipList.subList(i, Math.min(i + maxConcurrency, ipList.size()));
            CompletionService<IpResult> completionService = new ExecutorCompletionService<>(executor);
            
            // 提交当前组所有任务
            for (String ip : group) {
                completionService.submit(() -> new IpResult(ip, simulateRttRequest(ip)));
            }

            // 获取当前组第一个完成的结果
            try {
                Future<IpResult> future = completionService.poll(timeout, TimeUnit.MILLISECONDS);
                if (future != null) {
                    // future.get(0, TimeUnit.MILLISECONDS); // 立即返回不阻塞
                    IpResult groupWinner = future.get();
                    fastestIp = groupWinner.ip;
                    timeout = groupWinner.rtt; // 更新下一组超时时间
                }
            } catch (ExecutionException ignored) {}

            // // 取消当前组剩余任务 
            // for (int j = 0; j < group.size() - 1; j++) {
            //     Future<IpResult> future = completionService.poll();
            //     if (future != null) future.cancel(true);
            // }
        }
        executor.shutdown();
        // 找出所有组中最快的IP
        return fastestIp;
    }

    // 辅助类，存储IP和RTT
    private static class IpResult {
        String ip;long rtt;
        IpResult(String ip, long rtt) {this.ip = ip;this.rtt = rtt;}
    }
}
```
```js
async function simulateRttRequest(ip) {
    // 模拟RTT请求，返回随机时间（毫秒）
    const delay = Math.random() * 1000;
    return new Promise(resolve => setTimeout(() => resolve({ ip, rtt: delay }), delay));
}

async function findFastestIp(ipList, maxConcurrency) {
    let fastestIp = null;
    let minRtt = Infinity;
    let processed = 0;
    let isFirstGroup = true;

    while (processed < ipList.length) {
        const group = ipList.slice(processed, processed + maxConcurrency);
        const promises = group.map(ip => simulateRttRequest(ip));

        try {
            // 第一组：必须等到至少一个结果
            const result = isFirstGroup 
                ? await Promise.race(promises)
                : await Promise.race([
                    ...promises,
                    new Promise((_, reject) => setTimeout(() => reject('timeout'), minRtt))
                ]);

            if (result && result.rtt < minRtt) {
                minRtt = result.rtt;
                fastestIp = result.ip;
            }
            isFirstGroup = false;
        } catch (e) {
            console.log(`Group timeout with minRtt: ${minRtt}`);
        }

        processed += group.length;
    }

    return fastestIp;
}
```
