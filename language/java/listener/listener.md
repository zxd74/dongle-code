# 事件监听
## 元素
1. 事件源，EventContext：在事件执行时可能需要(即事件对象数据)
2. 事件监听器，EventListener：抽象类或接口类都可以
3. 事件监听实现类，如SystemEventListener：自定义事件监听器的实现类
4. 事件监听管理器，EventListenerManager：当事件监听器较多时，用于管理事件监听，支持监听增删及所有事件监听器的监听事件的执行