排序算法整理。

- [冒泡排序](#冒泡排序)
- [直接排序](#直接排序)
- [反转排序](#反转排序)
- [插入排序](#插入排序)
- [希尔排序](#希尔排序)
- [归并排序](#归并排序)
- [快速排序](#快速排序)
  - [三切分快速排序](#三切分快速排序)
- [堆排序](#堆排序)
- [计数排序](#计数排序)
- [桶排序](#桶排序)
- [基数排序](#基数排序)

```java
static void exch(int[] arr, int i, int j){
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}

// 判断两个数的大小关系
static boolean less(int i,int j){
    return i-j<0;
}
```

# 冒泡排序
```java
    public static void sort(int[] array){
        // 外层循环由从第二元素开始
        for (int i = 1; i < array.length; i++) {
            // 内层循环由第一元素开始
            for (int j = 0; j < array.length - i; j++) {
                // 条件：判断相邻元素大小
                if (array[j] > array[j+1]){
                    // 从小到大排序将小的向前移动，大的向后移动
                    // 反之，小的向后移动，大的向前移动
                    // 因为两值交换，需要有临时变量转换
                    int tmp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = tmp;
                }
            }
        }
    }
```
# 直接排序
```java
public static void selectSort(int[] array){
    // 一般先排最右侧结果
    // 因第一排序位置是比较值，无需从第一元素排序，外层代表需要排序的位置，
    for (int i = 1; i < array.length; i++) {
        int index = 0;
        // 因每次循环都是从第一元素作为比较，故从第二元素开始比较，且不超出已排好序的位置
        for (int j = 1; j <= array.length - i; j++) {
            // 该判断由排序要求决定
            if (array[j] > array[index]){
                index = j;
            }
        }
        // 定位最后侧位置值，定义临时变量，交换右侧位置与index位置值
        int tmp = array[array.length -i];
        array[array.length-i] = array[index];
        array[index] = tmp;
    }
}
```
* 定位最右侧：**通常使用**，相对逻辑易理解，如从小到大排序，先找大
* 定位最左侧：相对逻辑较多，如从小到大排序，先找小
```java
static void selectSort(int[] arr){
    int index;
    // 方法一：定位最右侧(如从小到大排序，先找大)
    for(int i = 0;i<arr.length-1;i++){ 
        index = 0;
        for(int j = 1;j<arr.length-i;j++){ // 减i代表已经排序好的元素个数
            if(less(arr[index],arr[j])) index = j; // 找最大
        }
        exch(arr,index,arr.length-i-1);
    }

    // 方法二： 定位最左侧(如从小到大排序，先找小)
    for(int i = 0;i<arr.length-1;i++){ // 最后一位无需外层排序，已无可比
        index = i;
        for(int j = i+1;j<arr.length;j++){  // 从i+1开始，因为i是初次比较位（估计这也是为啥通常不是先找最小值，先找最大值的原因）
            if(less(arr[j],arr[index])) index = j; //找最小
        }
        if(index > i) exch(arr,i,index);
    }
}
```

# 反转排序
```java
    public static void reverseSort(int[] array){
        for (int i = 0; i < array.length / 2; i++) {
            int tmp = array[i];
            array[i] = array[array.length - i -1];
            array[array.length - i -1] = tmp;
        }
    }
```

# 插入排序
```java
    public void insertSort(int[] arrays){
        for (int i = 1; i < arrays.length; i++) {
            int j = i -1;
            // 取当前值，
            int tmp = arrays[i];
            // 与前面有序数组做比较，并将加大元素后移
            for (; j>0 && arrays[j] > tmp ; j--) {
                arrays[j+1] = arrays[j];
            }
            // 找到合适位置赋值
            arrays[j] = tmp;
        }
    }
```

# 希尔排序
```java
    // 步长为N/2
    public void shellSortBy2(int[] arrays){
        // 每次步长除2
        for (int step = arrays.length/2; step > 0; step/=2) {
            for (int i = step; i < arrays.length; i++) {
                int j = i - step;
                int tmp = arrays[i];
                // 向前校验间隔步长值对比并互换
                for (; j >= 0 && arrays[j] > tmp; j -= step)
                    arrays[j + step] = arrays[j];
                arrays[j + step] = tmp;
            }
        }
    }

    // 步长为N/3+1
    public void shellSortBy3(int[] arrays){
        int grap=1,i,j,len = arrays.length,tmp;
        // 确定最合适的开始步长
        while (grap<len/3)
            grap = grap * 3 +1;

        for (;grap > 0;grap /= 3)
            for (i=grap;i<len;i++){
                tmp=arrays[i];
                for (j = i - grap; j >= 0 && arrays[j] > tmp; j -= grap)
                    arrays[j + grap] = arrays[j];
                arrays[j + grap] = tmp;
            }
    }
```

# 归并排序
```python
def merge(left_arr,right_arr):
    result = [] # 新数组返回，排序结果需要重新接收
    while left_arr and right_arr:
        if left_arr[0] < right_arr[0]:
            result.append(left_arr.pop(0))
        else:
            result.append(right_arr.pop(0))
    while left_arr:
        result.append(left_arr.pop(0))
    while right_arr:
        result.append(right_arr.pop(0))
    return result

def merge_sort(arr:list):
    size = len(arr)
    if size < 2:
        return arr
    mid = int(size/2)
    left, right= arr[0:mid],arr[mid:]
    return merge(merge_sort(left), merge_sort(right))
```
```java
static void mergeSort(int[] arr) {
    int[] aux = new int[arr.length];
    mergeSortByTopToDown(arr, aux, 0, arr.length - 1);
}

// 自顶向下的归并排序
static void mergeSortByTopToDown(int[] arr, int[] aux, int lo, int hi) {
    if (hi >= lo)
        return;
    int mid = lo + (hi - lo) / 2;
    mergeSortByTopToDown(arr, aux, lo, mid);
    mergeSortByTopToDown(arr, aux, mid + 1, hi);
    // TODO 以下逻辑会重复，由内层向万层重复，内层会重复多次，可需要优化
    merge(arr, aux, lo, mid, hi);
}

// 合并两个有序的子数组
static void merge(int[] arr, int[] aux, int lo, int mid, int hi) {
    // i指向左子数组的第一个元素，j指向右子数组的第一个元素
    int i = lo, j = mid + 1;
    // 将原数组复制到辅助数组中
    for (int k = lo; k <= hi; k++)  
        aux[k] = arr[k];
    // 合并两个有序的子数组
    for (int k = lo; k <= hi; k++) {
        // 如果左子数组已经遍历完，则将右子数组的元素复制到原数组中
        if (i > mid) // 当左半有序数组已经遍历完，则将右半部分剩余的元素复制到原数组中
            arr[k] = aux[j++];
        else if (j > hi) // 当右半有序数组已经遍历完，则将左半部分剩余的元素复制到原数组中
            arr[k] = aux[i++];
        else if (less(arr[j], arr[i])) // 当右半有序数组的元素小于左半有序数组的元素时，将右半有序数组的元素复制到原数组中
            arr[k] = aux[j++];
        else // 当左半有序数组的元素小于等于右半有序数组的元素时，将左半有序数组的元素复制到原数组中
            arr[k] = aux[i++];
    }
}

// 自底向上的归并排序：简化自顶向下的归并排序中的归并重复逻辑
static void mergeSortByBottomToUp(int[] arr) {
    int N = arr.length;
    int[] aux = new int[N];
    for (int sz = 1; sz < N; sz += sz) { // sz子数组的大小
        for (int lo = 0; lo < N - sz; lo += 2 * sz) { // lo子数组的起始位置
            merge(arr, aux, lo, lo + sz - 1, Math.min(lo + 2 * sz - 1, N - 1));
        }
    }
}
```

# 快速排序
```java
static void quickSort(int[] arr){
    quickSort(arr,0,arr.length-1);
}

static void quickSort(int[] arr, int lo, int hi) {
    if (hi <= lo)
        return;
    int j = partation(arr, lo, hi);
    quickSort(arr, lo, j - 1);
    quickSort(arr, j + 1, hi);
}

static int partation(int[] arr, int lo, int hi) {
    int i = lo, j = hi + 1, v = arr[lo];
    while (true) {
        while (less(arr[++i], v))
            if (i == hi)
                break;
        while (less(v, arr[--j]))
            if (j == lo)
                break;
        if (i >= j)
            break;
        exch(arr, i, j); // 代表存在左序列大于v，有序列小于v，即arr[j]<v<arr[i]
    }
    exch(arr, lo, j);
    return j;
}
```
## 三切分快速排序
* 循环时对**初始索引lo的数值**进行定位，(**实际一直以首元素数值对比**`v=arr[lo]`)
  * 当后面元素小于初始值，则索引对应数值交换，并都后移继续对比
  * 当后面元素大于初始值，与最后值`gt`交换，`gt`前移，继续对比(可以理解为只要大于初始值，就默认为最大值)
```java
static void quick3waySort(int[] arr){
    quick3waySort(arr,0,arr.length-1);
}
static void quick3waySort(int[] arr,int lo,int hi){
    if(hi<=lo) return;
    int lt=lo,i=lo+1,gt=hi,v=arr[lo];
    while(i<=gt){
        int cmp = arr[i]-v;
        if(cmp<0) exch(arr,lt++,i++); // lt无论如何变化，始终数值为v
        else if(cmp>0) exch(arr,i,gt--); //若i大于lt，则默认i为最大，与原最大值调换，继续比较i，lt
        else i++; // i == lt
    }
    // 此时 arr[lo~i-1]<arr[i]<=arr[lt..gt]<arr[i+1~hi]
    quick3waySort(arr,lo,lt-1);
    quick3waySort(arr,gt+1,hi);
}
```

# 堆排序
```python
# 位置值交换
def swap(arr,i,j):
    arr[i],arr[j] = arr[j],arr[i]

# 堆调整(平衡)
def heapify(arr,i):
    left,right,largest = 2 * i+1,2 * i+2,i
    #
    if left < size and arr[left] > arr[largest]:
        largest = left
    if right < size and arr[right] > arr[largest]:
        largest = right
    # 若最大值索引变更，则交换值，并重新处理堆平衡
    if largest != i:
        swap(arr,i,largest)
        mid = int(size/2)
        # 过半索引对比无效，浪费循环
        if largest > mid:
            largest = mid
        heapify(arr, largest)

# 构建初始堆
def build_heap(arr):
    for i in range(int(size / 2), -1, -1):
        heapify(arr, i)
def sort(arr):
    global size  # 定义全局变量
    size = len(arr)
    build_heap(arr)
    for i in range(int(len(arr)-1),-1,-1):
        swap(arr, 0, i)
        size -= 1
        heapify(arr, 0)
```
# 计数排序
```python
def count_sort(arr,max):
    bucket_len = max + 1
    bucket = [0] * bucket_len
    print(bucket)
    sorted_index = 0
    arr_len = len(arr)
    for i in range(arr_len):
        if not bucket[arr[i]]:
            bucket[arr[i]] = 0
        bucket[arr[i]] += 1
    for i in range(bucket_len):
        while bucket[i] > 0:
            arr[sorted_index] = i
            sorted_index += 1
            bucket[i] -= 1
```
# 桶排序
```python
def max_min_num(arr):
    max,min = arr[0],arr[0]
    for i in range(len(arr)):
        if arr[i] < min:
            min = arr[i]
        if arr[i] > max:
            max = arr[i]
    return max,min

def insert_sort(arr):
    for i in range(len(arr)):
        j = i -1
        tmp = arr[i]
        while j>0 and arr[j] > tmp:
            arr[j + 1] = arr[j]
            j -= 1
        arr[j] = tmp

# 桶排序，bucket_size为理想状态桶内元素数量，非实际数量
def bucket_sort(arr:list,bucket_size):
    if len(arr) == 0 :
        return
    # 获取最大最小值
    max,min = max_min_num(arr)
    # 根据最大最小值和桶大小确定桶数量
    bucket_count = int((max-min)/bucket_size) + 1
    # 定义指定数量的桶并赋初值
    bucket = [[]]*bucket_count
    for i in range(bucket_count):
        bucket[i] = []
    # 将数组元素根据比例填充到桶中
    for i in range(len(arr)):
        index = int((arr[i]-min)/bucket_size)
        bucket[index].append(arr[i])

    # 清空原数组
    arr.clear()
    # 将每个桶桶排序并补充给空数组
    for i in range(bucket_count):
        insert_sort(bucket[i])
        for j in range(len(bucket[i])):
            arr.append(bucket[i][j])
```
# 基数排序
```python
# 取最大数
def max_num(arr):
    # 取绝对值 math.fabs(arr[i])
    max = arr[0]
    for i in range(len(arr)):
        if max < arr[i]:
            max = arr[i]
    return max
# 取数值位数
def num_digit(num):
    digit = 0
    while num != 0:
        digit += 1
        num /= 10
    return digit

def radix_sort(arr):
    mod,dev,i=10,1,0
    # 取最大数的位数，暂不考虑位数，否则需要取绝对值 math.fabs(val)
    max_digit = num_digit(max_num(arr))

    # 若考虑负数情况是，counter请扩充至20，0-9代表负数，10-19代表正数 counter_count = 20
    counter_count = 10
    for i in range(max_digit):
        counter = [[]]*counter_count
        # 需要给counter赋初值
        for j in range(counter_count):
            counter[j] = []
        for j in range(len(arr)):
            # 若考虑负数情况时， bucket = int((arr[j] % mod) / dev) + mod;
            bucket = int((arr[j] % mod) / dev)
            if not counter[bucket]:
                counter[bucket] = []
            counter[bucket].append(arr[j])
        pos = 0
        for j in range(len(counter)):
            if counter[j]:
                for k in range(len(counter[j])):
                    arr[pos] = counter[j][k]
                    pos += 1
        # 继续比较十位，百位，等等
        dev *= 10
        mod *= 10
```
