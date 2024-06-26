```java
public class QueueDemo{
    
    /**
     * 依据单方向链，组成单向队列
     */
    interface Queue<T>{
        int size();
        DoubleLink<T> head();
        DoubleLink<T> end();
        void setRight(DoubleLink<T> right);
    }
    private static class SimpleQueue<T> implements Queue<T>{
        DoubleLink<T> head;
        DoubleLink<T> end;
        DoubleLink<T> link;
        int size;

        public SimpleQueue(DoubleLink<T> link) {
            if (link == null){
                return;
            }
            this.link = link;
            ++size;
            handlerLink(link);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public DoubleLink<T> head() {
            return head;
        }

        @Override
        public void setRight(DoubleLink<T> right) {
            if (right == null){
                return;
            }
            this.link.addNext(right);
            ++size;
            handlerLink(right);
        }

        @Override
        public DoubleLink<T> end() {
            return this.end;
        }

        private void handlerLink(DoubleLink<T> link){
            if (link == null){
                return;
            }
            DoubleLink<T> pre = link;
            while (pre.pre() != null){
                pre = pre.pre();
                ++size;
            }
            this.head = pre;
            Link<T> next = link;
            while (next.next() != null){
                next = next.next();
                ++size;
            }
            this.end = (DoubleLink<T>) next;
        }
    }

    /**
     * 依据双方向链，组成双向队列
     */
    interface DoubleQueue<T> extends Queue<T>{
        void setLeft(DoubleLink<T> left);
    }
    private static class SimpleDoubleQueue<T> implements DoubleQueue<T>{
        DoubleLink<T> head;
        DoubleLink<T> end;
        DoubleLink<T> link;
        int size;

        public SimpleDoubleQueue(DoubleLink<T> link) {
            if (link == null){
                return;
            }
            this.link = link;
            ++size;
            handlerLink(link);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public DoubleLink<T> head() {
            return head;
        }

        @Override
        public void setRight(DoubleLink<T> right) {
            if (right == null){
                return;
            }
            this.link.addNext(right);
            ++size;
            handlerLink(right);
        }

        @Override
        public DoubleLink<T> end() {
            return this.end;
        }

        @Override
        public void setLeft(DoubleLink<T> left) {
            if (left == null){
                return;
            }
            this.link.setPre(left);
            ++size;
            handlerLink(left);
        }

        private void handlerLink(DoubleLink<T> link){
            if (link == null){
                return;
            }
            DoubleLink<T> pre = link;
            while (pre.pre() != null){
                pre = pre.pre();
                ++size;
            }
            this.head = pre;
            Link<T> next = link;
            while (next.next() != null){
                next = next.next();
                ++size;
            }
            this.end = (DoubleLink<T>) next;
        }
    }
}
```