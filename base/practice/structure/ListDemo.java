package base.practice.structure;

public class ListDemo{

    public static void main(String[] args) {
        List<String> list = new SingleList<>();
        list.add("Dongle");
        list.add("Kevin");
        list.addAll(new String[]{"11", "22", "33"});
        System.out.println("size:" + list.size() + ",list:" + list);

        System.out.println("index: " + list.find("11"));

        list.remove("11");
        System.out.println("size:" + list.size() + ",list:" + list);
        list.remove(1);
        System.out.println("size:" + list.size() + ",list:" + list);

        List<String> newList = list.getList(0,10);
        System.out.println("size:" + newList.size() + ",newList:" + newList);

        newList.claer();
        System.out.println("size:" + newList.size() + ",newList:" + newList);
    }

    /**
     * 自定有List接口
     */
    interface List<T extends Object> {

        int size();
    
        void set(int index,T t);
        void add(T t);
        void addAll(T[] t);
    
        void remove(T t);
        void remove(int index);
        void claer();
    
        T get(int index);
        int find(T t);
        List<T> getList(int size);
        List<T> getList(int fromIndex,int size);
    }

    /**
     * 自定义链表实现
     */
    static class SingleList<T extends Object> implements List<T> {
        // 默认容量
        private final int DEFAULT_SIZE = 10;
        // 初始元素数组
        private final Object[] DEFAULT_VALUES = {};
        // 最大容量
        private int maxSize;
        // 当前元素数量
        private int size;
        // 元素值
        private Object[] values;
    
        public SingleList() {
            changeCapacity(0);
            this.values = DEFAULT_VALUES;
        }
    
        public SingleList(T[] values) {
            if (values == null){
                return;
            }
            addAll(values);
            this.size = values.length;
        }
    
        public SingleList(int size){
            changeCapacity(size);
            this.size = size;
        }
    
        @Override
        public int size() {
            return size;
        }
    
        @Override
        public void set(int index, T t) {
            values[index] = t;
        }
    
        @Override
        public void add(T t) {
            changeCapacity(size + 1);
            values[size++] = t;
        }
    
        @Override
        public void addAll(T[] t) {
            if (t == null){
                return;
            }
            changeCapacity(size + t.length);
            for (T value : t) {
                values[size++] = value;
            }
        }
    
        @Override
        public void remove(T t) {
            int index = find(t);
            while ( index != -1){
                remove(index);
                index = find(t);
            }
        }
    
        @Override
        public void remove(int index) {
            for (int i = index; i < size - 1; i++) {
                this.values[i] = this.values[i+1];
            }
            this.values[--size] = null;
        }
    
        @Override
        public void claer() {
            changeCapacity(0);
            this.size = 0;
            this.values = DEFAULT_VALUES;
        }
    
        @Override
        public T get(int index) {
            return (T)values[index];
        }
    
        @Override
        public List<T> getList(int size) {
            return getList(0,size);
        }
    
        @Override
        public List<T> getList(int fromIndex, int num) {
            if ( 0> fromIndex || fromIndex > size){
                return new SingleList<>();
            }
            if (num <0){
                return new SingleList<>();
            }
    
            int minNum = Math.min((size - fromIndex),num);
            Object[] values = new Object[minNum];
            for (int i = 0; i < minNum; i++) {
                values[i] = this.values[fromIndex + i];
            }
            return new SingleList<>((T[])values);
        }
    
        @Override
        public int find(T t){
            int index = -1;
            for (int i=0;i<size;i++){
                if (values[i] == t){
                    return i;
                }
            }
            return index;
        }
    
        /**
         * 容量变更
         * @param size
         */
        private void changeCapacity(int size){
            if (size <= DEFAULT_SIZE){
                this.maxSize = DEFAULT_SIZE;
            }
    
            // 数组扩容
            while (maxSize < size){
                this.maxSize = this.maxSize >> 1;
            }
    
            // 若数组为空即未被初始化，则先初始化
            if (this.values == null){
                this.values = new Object[this.maxSize];
                return;
            }
    
            // 若不为空，则将原数据复制到新数组中
            Object[] values = new Object[this.maxSize];
            for (int i = 0; i < this.values.length; i++) {
                values[i] = this.values[i];
            }
            this.values = values;
        }
    
        @Override
        public String toString() {
            if (size == 0){
                return "[]";
            }
            String tmp = "[";
            for (int i = 0; i < size; i++) {
                tmp += this.values[i];
                if (i == size -1){
                     tmp += "]";
                }else{
                    tmp += ",";
                }
            }
            return tmp;
        }
    }
}

