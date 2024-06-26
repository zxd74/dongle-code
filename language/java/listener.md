# 事件监听
## 元素
1. 事件源，EventContext：在事件执行时可能需要(即事件对象数据)
2. 事件监听器，EventListener：抽象类或接口类都可以
3. 事件监听实现类，如SystemEventListener：自定义事件监听器的实现类
4. 事件监听管理器，EventListenerManager：当事件监听器较多时，用于管理事件监听，支持监听增删及所有事件监听器的监听事件的执行

```java
import java.util.ArrayList;
import java.util.List;

public class JavaListener {

    public static void main(String[] args) {
        EventListenerManager manager = new EventListenerManager(new SystemEventListener());
        EventContext context = new EventContext();
        manager.starting(context);
        manager.started(context);
    }

    /**
     * 事件源
     */
    public static class EventContext{
        // 事件源属性
    }

    /**
     * 事件监听器
     */
    public interface EventListener {
        // 监听方法
        void stating(EventContext event);
        // 监听方法
        void started(EventContext event);
    }

    /**
     * 事件监听实现类
     */
    public static class SystemEventListener implements EventListener {
        @Override
        public void stating(EventContext event) {
            System.out.println("run starting listener");
        }

        @Override
        public void started(EventContext event) {
            System.out.println("run started listener");
        }
    }

    /**
     * 事件监听管理器
     */
    public static class EventListenerManager {

        private List<EventListener> listeners = new ArrayList<>();

        public EventListenerManager() {
        }

        public EventListenerManager(EventListener listener) {
            this.listeners.add(listener);
        }

        public EventListenerManager(List<EventListener> listeners) {
            this.listeners.addAll(listeners);
        }

        // 添加监听
        public void addListener(EventListener listener){
            listeners.add(listener);
        }

        // 删除监听
        public void delListener(EventListener listener){
            listeners.remove(listener);
        }

        // 执行监听方法
        public void starting(EventContext context){
            listeners.forEach(eventListener -> eventListener.stating(context));
        }
        // 执行监听方法
        public void started(EventContext context){
            listeners.forEach(eventListener -> eventListener.started(context));
        }

    }
}
```