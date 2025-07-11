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
static void insertSort(int[] arr){
    for(int i=1;i<arr.length;i++){
        for(int j = i;j>0 && less(arr[j],arr[j-1]);j--){
            exch(arr,j,j-1);
        }
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
    static void shellSort(int[] arr){
        int n = arr.length,h = 1;
        while(h<n/3) h = h*3+1;
        while(h>0){
            for(int i = h;i<arr.length;i++){
                for(int j = i;j>=h && less(arr[j], arr[j-h]);j-=h){
                    exch(arr,j,j-h);
                }
            }
            h=h/3;
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
    int j = partition(arr, lo, hi);
    quickSort(arr, lo, j - 1);
    quickSort(arr, j + 1, hi);
}

static int partition(int[] arr, int lo, int hi) {
    int i = lo, j = hi + 1, v = arr[lo];
    while (true) {
        while(less(arr[++i],v)) if(i==hi) break;
        while(less(v,arr[--j])) if(j == lo) break;
        if (i>=j) break;
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
堆排序实质是一个二叉树形式，分为最大堆和最小堆，即顶部节点是最大或最小值，
* 堆实质是一个二叉树形式
* 堆排序实质是不断调整堆结构，将最大值或最小值放到顶部，然后调整堆结构，将最大值或最小值放到末尾
* 重复此过程，直到堆结构为空，即排序完成
```java
// 堆排序（升序，原地排序）
public static void heapSort(int[] arr) {
    int n = arr.length;

    // 1. 构建大根堆（从最后一个非叶子节点开始调整）
    for (int i = n / 2 - 1; i >= 0; i--) {
        heapify(arr, n, i);
    }

    // 2. 依次取出堆顶元素（最大值），放到数组末尾
    for (int i = n - 1; i > 0; i--) {
        // 交换堆顶和当前末尾元素
        swap(arr, 0, i);

        // 调整剩余部分，使其重新满足大根堆性质
        heapify(arr, i, 0);
    }
}

// 堆化调整（大根堆）
private static void heapify(int[] arr, int n, int i) {
    int largest = i;      // 当前最大值索引
    int left = 2 * i + 1; // 左孩子
    int right = 2 * i + 2; // 右孩子

    // 找出当前节点、左孩子、右孩子中的最大值
    if (left < n && arr[left] > arr[largest]) {
        largest = left;
    }
    if (right < n && arr[right] > arr[largest]) {
        largest = right;
    }

    // 如果最大值不是当前节点，交换并继续调整
    if (largest != i) {
        swap(arr, i, largest);
        heapify(arr, n, largest);
    }
}

private static void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}
```


# 计数排序
创建以最大值+1为长度的辅助数组，将数组元素以索引方式堆辅助索引计数(+1)，最后遍历辅助数组，将元素按顺序放入原数组

**需要三次遍历+一次小内循环**
* 一次找出最大值
* 一次遍历数组，将元素以索引方式堆辅助索引计数(+1)
* 一次遍历辅助数组，将元素按顺序放入原数组
  * 因对计数值进行遍历

**提示**：
* 不适用于有负数的数组
* 不适用于元素间差值过大的数组
  * 如`{1,100,1000,99999}`，因为需要创建长度为`100000`的辅助数组
```java
public static void countSort(int[] arr){
    int index=0;
    for(int i=1;i<arr.length-1;i++){
        if(less(arr[index],arr[i])) index = i;
    }
    int max = arr[index]; // 获取最大值
    int[] aux = new int[max+1]; // 创建辅助数组，以最大值+1为长度
    for(int i = 0;i<arr.length;i++){
        aux[arr[i]]++; // 对数值计数+1
    }
    int sorted_index=0; // 已排序索引
    for(int i=0;i<aux.length;i++){
        while(aux[i]>0){ // 当计数大于0时，将数值放入原数组
            arr[sorted_index++] = aux[i]; // 按顺序放入原数组
            aux[i]--; // 将计数-1
        }
    }
}
```
# 桶排序
桶是一个二维数组，每个桶内放最多指定个数的元素(**桶大小**)，
* 找出最大最小值
* 根据最大最小值差异和指定桶大小，确定桶的个数
* 根据元素于最小值差和桶大小的比值，确定元素位于具体桶：**同等比值的代表元素位于同一个桶(不好过该桶的最大和最小值)**
* 对每个桶内元素进行排序：**桶内元素只是确定最值范围，仍需排序**
* 遍历每个桶，将桶内元素按顺序放入原数组
```java
public static void bucketSort(int[] arr,int bucketSize){
    // 找最值
    int max = arr[0],min=arr[0];
    for(int num:arr){
        if(num>max) max = num;
        if(num<min) min = num;
    }

    // 确定桶个数:由桶内能存储的数量决定
    int bucket_count = (max-min)/bucketSize+1;
    List<List<Integer>> buckets = new ArrayList<>(bucket_count);
    // 初始化桶
    for(int i = 0;i<bucket_count;i++){
        buckets.add(new ArrayList<>());
    }
    // 将元素放入桶中
    for(int i= 0;i<arr.length;i++){
        int index = (arr[i]-min)/bucketSize;
        buckets.get(index).add(arr[i]);
    }
    // 桶内排序
    for(List<Integer> bucket:buckets){
        Collections.sort(bucket);
    }
    // 将桶内元素放回原数组
    int sortIndex = 0;
    for(List<Integer> bucket:buckets){
        for(int num:bucket){
            arr[sortIndex++]=num;
        }
    }
}
```
# 基数排序
基数排序是根据每个元素的尾部进行排序，依次从尾到首进行排序，直到最大首位数排序完成
* 是对计数排序的改进
* 不仅仅适用于整数，也可以用于字符串
* 关键步骤
  * 根据最大值取最大位数
  * 根据每个元素每一位上的内容，定义指定桶个数，如`整数0-9就是10个桶`，`字符串a-z就是26个桶`
  * 需要多次遍历，具体由最大位数决定
  * 每次遍历，将内容除模取余，与桶序列绑定
  * 每次遍历完，模和余都要向前进一位，如`整数是10进制，则模10，余10`
```java
public static void radixSort(int[] arr){
    // 取最大值，并根据最大值获取最大位数
    int max = arr[0];
    for(int num:arr){
        if(max<num) max = num;
    }
    // 取最大位数
    int maxDigit = 0;
    while(max!=0){
        maxDigit++;
        max /=10;
    }
    
    int couterCount = 10; // 若考虑负数，则couterCount = 20
    List<List<Integer>> counter = new ArrayList<>(couterCount);
    int mod = 10,dev=1;
    for(int i = 0;i<maxDigit;i++){
        for(int j = 0;j<couterCount;j++){ // 必需，重置
            counter.add(new ArrayList<>());
        }
        for(int j = 0;j<arr.length;j++){
            // 若考虑负数情况时， int bucket = int((arr[j] % mod) / dev) + mod;
            int bucket = (arr[j] % mod)/dev;
            counter.get(bucket).add(arr[j]);
        }
        int pos = 0;
        for(int j =0;j<counter.size();j++){
            if(counter.get(j).size()==0) continue;
            for(int k = 0;k<counter.get(j).size();k++){
                arr[pos++] = counter.get(j).get(k);
            }
        }
        dev *=10;mod *=10;
    }
}
```
