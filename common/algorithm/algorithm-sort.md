排序算法整理。

- [冒泡排序](#冒泡排序)
- [直接排序](#直接排序)
- [反转排序](#反转排序)
- [插入排序](#插入排序)
- [希尔排序](#希尔排序)
- [归并排序](#归并排序)
- [快速排序](#快速排序)
- [堆排序](#堆排序)
- [计数排序](#计数排序)
- [桶排序](#桶排序)
- [基数排序](#基数排序)

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

# 快速排序
```python
# 位置值交换
def swap(arr,i,j):
    arr[i],arr[j] = arr[j],arr[i]

# 定义基准数，比较位一左一右
def partition(arr, left, right):
    if left > right:
        return
    partition_index = left
    i = right
    while partition_index != i :
        while arr[i] >= arr[left] and i > partition_index:
            i -= 1
        while arr[partition_index] <= arr[left] and partition_index< i:
            partition_index += 1
        if i > partition_index:
            swap(arr,partition_index,i)
    swap(arr,left,partition_index)
    return partition_index

# 定义基准，比较位从左向右
def partition1(arr,left,right):
    if left > right:
        return
    index = left + 1
    i = index
    while i <= right:
        if arr[i] < arr[left]:
            swap(arr,i,index)
            index += 1
        i += 1
    partition_index = index - 1
    swap(arr,left,partition_index)
    return partition_index

# 实现快速排序：分治+递归方式
def sort(arr,left,right):
    if left is None:
        left = 0
    if right is None:
        right = len(arr) - 1

    if left < right:
        partition_index = partition1(arr, left, right)
        sort(arr,left,partition_index - 1)
        sort(arr,partition_index + 1,right)
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
