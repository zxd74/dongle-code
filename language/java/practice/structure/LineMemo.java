package language.java.practice.structure;

public class LineMemo {
    

    /**
     * 单方向链
     */
    interface Line<T> {
        T get();
        void addNext(Line<T> t);
        Line<T> next();
    }
    public static class SimpleLine<T> implements Line<T>{
        T t;
        Line<T> next;

        public SimpleLine(T t) {
            this(t,null);
        }

        public SimpleLine(T t, Line<T> next) {
            this.t = t;
            this.next = next;
        }

        @Override
        public T get() {
            return t;
        }

        @Override
        public void addNext(Line<T> next) {
            this.next = next;
        }


        @Override
        public Line<T> next() {
            return next;
        }
    }

    /**
     * 双方向链
     */
    interface DoubleLine<T> extends Line<T>{

        void setPre(DoubleLine<T> t);
        DoubleLine<T> pre();
    }
    public static class SimpleDoubleLine<T> implements DoubleLine<T>{
        T t;
        DoubleLine<T> pre;
        Line<T> next;

        public SimpleDoubleLine() {
        }

        public SimpleDoubleLine(T t) {
            this(t,null,null);
        }

        public SimpleDoubleLine(T t, DoubleLine<T> pre, Line<T> next) {
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
        public void addNext(Line<T> next) {
            this.next = next;
        }

        @Override
        public Line<T> next() {
            return next;
        }

        @Override
        public void setPre(DoubleLine<T> pre) {
            this.pre = pre;
        }

        @Override
        public DoubleLine<T> pre() {
            return pre;
        }
    }


}
