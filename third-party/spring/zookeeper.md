# Java
## Zookeeper API
1. 依赖
```xml
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.7</version>
</dependency>
```
2. 管理zk
```java
        // 创建zk连接
        ZooKeeper zk = new ZooKeeper(
                "192.168.1.3:2181,192.168.1.4:2181,192.168.1.5:2181",
                20000,
                watchedEvent -> {
                    // 发生变更的节点路径
                    String path = watchedEvent.getPath();
                    System.out.println("path:" + path);
 
                    // 通知状态
                    Watcher.Event.KeeperState state = watchedEvent.getState();
                    System.out.println("KeeperState:" + state);
 
                    // 事件类型
                    Watcher.Event.EventType type = watchedEvent.getType();
                    System.out.println("EventType:" + type);
                }
        );

        // 创建节点
        zk.create("/abc", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        // 获取节点数据
        byte[] data = zk.getData("/abc", false, stat);
        // 对节点添加watche事件
        zk.getData("/abc",
                watchedEvent -> {
                    System.out.println("path:" + watchedEvent.getPath());
                    System.out.println("KeeperState:" + watchedEvent.getState());
                    System.out.println("EventType:" + watchedEvent.getType());
                },
                null);
        // set数据
        zk.setData("/abc", "456".getBytes(), -1);
        // 关闭zk连接
        zk.close();
```
## zkClient API
1. 依赖
```xml
<dependency>
    <groupId>com.101tec</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.10</version>
</dependency>
```
2. 操作zk节点数据
```java
        ZkClient zkClient = new ZkClient(
                "192.168.1.3:2181,192.168.1.4:2181,192.168.1.5:2181",
                20000
        );
 
        // 创建节点
        zkClient.createPersistent("/abc", "hello");
        zkClient.createEphemeral("/xyz", "world");
        zkClient.create("/opq", "world", CreateMode.EPHEMERAL_SEQUENTIAL);
 
        String data = zkClient.readData("/abc");
        System.out.println(data);
 
        // 监听状态变化
        zkClient.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                System.out.println("state:" + keeperState);
            }
 
            @Override
            public void handleNewSession() throws Exception {
                System.out.println("new session");
            }
 
            @Override
            public void handleSessionEstablishmentError(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
 
        // 监听子节点发生变化
        zkClient.subscribeChildChanges("/", new IZkChildListener() {
            @Override
            public void handleChildChange(String path, List<String> list) throws Exception {
                System.out.println("watch path:" + path);
                // 输出所有子节点
                list.forEach(str -> {
                    System.out.println(str);
                });
            }
        });
 
```