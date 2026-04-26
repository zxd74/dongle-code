# 数组
* 数组大小
  * 内存大小 `sizeof(arr)`
  * 元素个数 `int len = sizeof(arr) / sizeof(arr[0])`
  * 动态数组：
  ```c
  int *arr=malloc(n * sizeof(int)) // malloc只需要总内存大小
  int *arr=calloc(n,sizeof(int)) // calloc 参数分开：个数+单元大小
  ```
    * 个数: 即为分配时的个数`n`
    * 内存：根据初始分配时指定的个数确定`n * sizeof(int)`
    * 扩容：`arr = realloc(arr,n*sizeof(int))`
    * **切记**：用完后使用`free`释放内存 `f
* 排序
  * 冒泡: 相邻元素互换
    ```c
    void selection_sort(int arr[], int len) {
        for (int i = 0; i < len-1; i++) {
            int max_idx = i;
            for (int j = i+1; j < len; j++) {
                if (arr[j] > arr[max_idx]) {
                    max_idx = j;
                }
            }
            // Swap arr[i] and arr[max_idx]
            int temp = arr[i];
            arr[i] = arr[max_idx];
            arr[max_idx] = temp;
        }
    }
    ```
  * 选择：确定位置互换
    ```c
    void selection_sort(int arr[], int len) {
        for (int i = 0; i < len-1; i++) {
            int min_idx = i;
            for (int j = i+1; j < len; j++) {
                if (arr[j] < arr[min_idx]) {
                    min_idx = j;
                }
            }
            // Swap arr[i] and arr[min_idx]
            int temp = arr[i];
            arr[i] = arr[min_idx];
            arr[min_idx] = temp;
        }
    }
    ```
* 反转
    ```c
    void reverse_array(int arr[], int n) {
        for (int i = 0; i < n / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[n - i - 1];
            arr[n - i - 1] = temp;
        }
    }
    ```