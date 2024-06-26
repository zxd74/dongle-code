```java
package language.java.practice.structure;

public class LinkMemo {
    
    /**
     * 单方向链
     */
    interface Link<T> {
        T get();
        void addNext(Link<T> t);
        Link<T> next();
    }
    public static class SimpleLink<T> implements Link<T>{
        T t;
        Link<T> next;

        public SimpleLink(T t) {
            this(t,null);
        }

        public SimpleLink(T t, Link<T> next) {
            this.t = t;
            this.next = next;
        }

        @Override
        public T get() {
            return t;
        }

        @Override
        public void addNext(Link<T> next) {
            this.next = next;
        }


        @Override
        public Link<T> next() {
            return next;
        }
    }

    /**
     * 双方向链
     */
    interface DoubleLink<T> extends Link<T>{

        void setPre(DoubleLink<T> t);
        DoubleLink<T> pre();
    }
    public static class SimpleDoubleLink<T> implements DoubleLink<T>{
        T t;
        DoubleLink<T> pre;
        Link<T> next;

        public SimpleDoubleLink() {
        }

        public SimpleDoubleLink(T t) {
            this(t,null,null);
        }

        public SimpleDoubleLink(T t, DoubleLink<T> pre, Link<T> next) {
            this.t = t;
            this.pre = pre;
            this.next = next;
        }

        @Override
        public void set(T t) {
            this.t = t;
        }
        @Override
        public T get() {
            return t;
        }

        @Override
        public void addNext(Link<T> next) {
            this.next = next;
        }

        @Override
        public Link<T> next() {
            return next;
        }

        @Override
        public void setPre(DoubleLink<T> pre) {
            this.pre = pre;
        }

        @Override
        public DoubleLink<T> pre() {
            return pre;
        }
    }


}

```