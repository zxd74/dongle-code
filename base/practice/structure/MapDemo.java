package base.practice.structure;

public class MapDemo {
    
    
    /**
     * Map接口
     * @param <K>
     * @param <V>
     */
    public interface Map<K,V>{
        void put(K k,V v);
        void putAll(Map<K,V> map);
        void clear();
        void remove(K k);
        boolean contain(K k);
        V get(K k);
        V getOrDefault(K k,V v);
        int size();
    }

    /**
     * 节点接口
     * @param <K>
     * @param <V>
     */
    public interface Entity<K,V>{
        void setValue(V v);
    }
    public static class SimpleMap<K,V> implements Map<K,V>{

        static final int DEFAULT_CAPTION = 16;
        static final float MAX_BALANCER = 0.75f;
        static final float MIN_BALANCER = 0.5f;
        // 最大容量
        int caption;
        // 实际容量
        int size;
        Node<K,V>[] nodes;
        // 平衡率,当size超过最大容量的max_balancer比率时触发扩容，当低于最大容量的min_balancer比例时触发缩容
        float max_balancer;
        float min_balancer;

        public SimpleMap(int caption) {
            this(caption,MAX_BALANCER, MIN_BALANCER);
        }

        public SimpleMap(int caption, float max_balancer, float min_balancer) {
            this(new Node[0],caption,max_balancer, min_balancer);
        }


        public SimpleMap(Node<K, V>[] nodes,int caption, float max_balancer, float min_balancer) {
            this.nodes = nodes;
            this.caption = caption;
            this.max_balancer = max_balancer;
            this.min_balancer = min_balancer;
        }

        private void changeCaption(int size){
            caption = size < DEFAULT_CAPTION ? DEFAULT_CAPTION : caption>>>1;
        }

        @Override
        public void put(K k, V v) {
            int hash = hash(k);
            Node<K,V> node;
            int index = (this.caption - 1) & hash;
            node = new Node<>(hash,k,v,null);
            if (nodes[index] == null){
                nodes[index] = node;
            } else{
                nodes[index].next = node;
            }
            size++;
        }

        @Override
        public void putAll(Map<K, V> map) {
            //changeCaption(size + map.size());
            //changeNodes();
            // TODO 无法循环读取node，需要有一个方法读取

        }

        @Override
        public void clear() {
            changeCaption(0);
            nodes = (Node<K, V>[]) new Node[DEFAULT_CAPTION];
            this.size = 0;
        }

        @Override
        public void remove(K k) {
            // TODO 如何高效删除
            int hash = hash(k);
            int index = (nodes.length-1) & hash;
            Node<K,V> node,lnode;
            if ((node = nodes[index]) == null){
                return;
            }
            if (node.key == k){
                node = node.next;
            }else{
                lnode = node;
                while ((node = node.next)!=null){
                    if (node.key == k){
                        lnode.next = node.next;
                        node = lnode;
                        break;
                    }
                    lnode = node;
                }
            }
            nodes[index] = node;
        }

        @Override
        public boolean contain(K k) {
            return getNode(k) != null;
        }

        @Override
        public V get(K k) {
            return getOrDefault(k,null);
        }

        @Override
        public V getOrDefault(K k, V v) {
            Node<K,V> node;
            return (node=getNode(k)) == null ? v:node.value;
        }

        private Node<K,V> getNode(K k){
            // TODO 目前涉及全量搜索，待优化
            Node<K,V> node = nodes[0];
            Node<K,V> v = null;
            do {
                if (node.key == k){
                    v = node;
                    break;
                }
            }while ((node = node.next) != null);
            return v;
        }

        @Override
        public int size() {
            return size;
        }

        final int hash(K k){
            return k.hashCode();
        }
    }

    /**
     * 节点
     * @param <K>
     * @param <V>
     */
    public static class Node<K,V> implements Entity<K,V>{
        int hash; // 代表Map中table索引
        K key;
        V value;
        Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
        @Override
        public void setValue(V value) {
            this.value = value;
        }
    }
}
