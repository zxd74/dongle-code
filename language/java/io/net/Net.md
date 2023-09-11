# IP 获取
默认网卡IP地址获取
```java
InetAddress.getLocalHost().getHostAddress();
```
多网卡IP地址获取
```java
    Enumeration allNetInterfaces=NetworkInterface.getNetworkInterfaces();
    InetAddress ip=null;
    while(allNetInterfaces.hasMoreElements()){
        NetworkInterface netInterface=(NetworkInterface) allNetInterfaces.nextElement();
        //System.out.println(netInterface.getName());
        Enumeration addresses=netInterface.getInetAddresses();
        while(addresses.hasMoreElements()){
            // 当前网卡IP信息
            InetAddress ip=(InetAddress) addresses.nextElement();
        }
    }
```