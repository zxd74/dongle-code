- [二分法查找](#二分法查找)
- [顺序查找](#顺序查找)

# 二分法查找
```java
    /**
     * 基本数据类型查询,其他类型Object需要实现Comparable接口
    */
    private static int binarySearch0(int[] a, int fromIndex, int toIndex,
                                     int key) {
        int low = 0;
        int high = a.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }
```
# 顺序查找
