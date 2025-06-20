堆是一个**二叉树形式**，但内部仍以数组形式存储数据(**堆是数组的包装**)
* 内部支持**父/左/右节点索引**计算
* 支持**插入/删除**
* 支持**向上/向下调整**
```java
public class Heap{
    private int[] heap;
    private int size;
    private int capacity;
    
    public Heap(int capacity){
        this.capacity = capacity;
        this.heap = new int[capacity];
        this.size = 0;
    }

    private int parent(int i){return (i-1)/2;}
    private int leftChild(int i ){return 2*i+1;}
    private int rightChild(int i){return 2*i+2;}
    private void swap(int i,int j){
        int tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }
    public void insert(int val){
        if(size == capacity) throw new IllegalStateException("Heap is full");
        heap[size]=val;
        heapifyUp(size);
        size++;
    }
    private void heapifyUp(int i){
        while(i>0 && heap[i]>heap[parent(i)]){
            swap(i,parent(i));
            i=parent(i);
        }
    }
    public int extractMax(){ // 删除堆顶，下沉操作
        if(size == 0) throw new IllegalStateException("Heap is empty");
        int max = heap[0];
        size--;
        heapifyDown(0);
        return max;
    }
    private void heapifyDown(int i){
        int maxIndex = i;
        int left = leftChild(i);
        int right = rightChild(i);
        if(left <size && heap[left]>heap[maxIndex]){
            maxIndex = left;
        }
        if(right<size && heap[right]>heap[maxIndex]){
            maxIndex = right;
        }
        if(i!=maxIndex){
            swap(i,maxIndex);
            heapifyDown(maxIndex);
        }
    }
    public void buildHeap(int[] arr){
        if(arr.length>capacity) throw new IllegalArgumentException("Array size exceeds heap capacity");
        System.arraycopy(arr,0,heap,0,arr.length);
        size = arr.length;
        // 从最后一个非叶子节点开始调整
        for(int i = size/2-1;i>=0;i--){
            heapifyDown(i);
        }
    }
}
```

# 堆排序
* 利用Heap结构进行排序
```java
// 堆排序（升序，使用大根堆）
public static void heapSort(int[] arr) {
    int n = arr.length;

    // 1. 构建大根堆
    MaxHeap maxHeap = new MaxHeap(n);
    maxHeap.buildHeap(arr);

    // 2. 依次取出堆顶元素（最大值），放到数组末尾
    for (int i = n - 1; i >= 0; i--) {
        arr[i] = maxHeap.extractMax();
    }
}
```
