package base.listener;
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