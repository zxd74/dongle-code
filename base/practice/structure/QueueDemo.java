package base.practice.structure;

import LineMemo.Line;
import LineMemo.DoubleLine;

public class QueueDemo{
    
    /**
     * 依据单方向链，组成单向队列
     */
    interface Queue<T>{
        int size();
        DoubleLine<T> head();
        DoubleLine<T> end();
        void setRight(DoubleLine<T> right);
    }
    private static class SimpleQueue<T> implements Queue<T>{
        DoubleLine<T> head;
        DoubleLine<T> end;
        DoubleLine<T> line;
        int size;

        public SimpleQueue(DoubleLine<T> line) {
            if (line == null){
                return;
            }
            this.line = line;
            ++size;
            handlerLine(line);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public DoubleLine<T> head() {
            return head;
        }

        @Override
        public void setRight(DoubleLine<T> right) {
            if (right == null){
                return;
            }
            this.line.addNext(right);
            ++size;
            handlerLine(right);
        }

        @Override
        public DoubleLine<T> end() {
            return this.end;
        }

        private void handlerLine(DoubleLine<T> line){
            if (line == null){
                return;
            }
            DoubleLine<T> pre = line;
            while (pre.pre() != null){
                pre = pre.pre();
                ++size;
            }
            this.head = pre;
            Line<T> next = line;
            while (next.next() != null){
                next = next.next();
                ++size;
            }
            this.end = (DoubleLine<T>) next;
        }
    }

    /**
     * 依据双方向链，组成双向队列
     */
    interface DoubleQueue<T> extends Queue<T>{
        void setLeft(DoubleLine<T> left);
    }
    private static class SimpleDoubleQueue<T> implements DoubleQueue<T>{
        DoubleLine<T> head;
        DoubleLine<T> end;
        DoubleLine<T> line;
        int size;

        public SimpleDoubleQueue(DoubleLine<T> line) {
            if (line == null){
                return;
            }
            this.line = line;
            ++size;
            handlerLine(line);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public DoubleLine<T> head() {
            return head;
        }

        @Override
        public void setRight(DoubleLine<T> right) {
            if (right == null){
                return;
            }
            this.line.addNext(right);
            ++size;
            handlerLine(right);
        }

        @Override
        public DoubleLine<T> end() {
            return this.end;
        }

        @Override
        public void setLeft(DoubleLine<T> left) {
            if (left == null){
                return;
            }
            this.line.setPre(left);
            ++size;
            handlerLine(left);
        }

        private void handlerLine(DoubleLine<T> line){
            if (line == null){
                return;
            }
            DoubleLine<T> pre = line;
            while (pre.pre() != null){
                pre = pre.pre();
                ++size;
            }
            this.head = pre;
            Line<T> next = line;
            while (next.next() != null){
                next = next.next();
                ++size;
            }
            this.end = (DoubleLine<T>) next;
        }
    }
}