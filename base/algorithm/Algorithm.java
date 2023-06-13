package base.algorithm;

public class Algorithm {

//----------------排序算法---------------------
    /**
     * 冒泡排序
     * @param array
     */
    private void bubbleSort(int[] array){
        // 外层循环由从第二元素开始，当执行到外层最后一个时即结束内层循环(无意义)
        for (int i = 1; i < array.length; i++) {
            // 内层循环由第一元素开始，
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

    /**
     * 反转排序
     * @param array
     */
    private void reverseSort(int[] array){
        for (int i = 0; i < array.length / 2; i++) {
            int tmp = array[i];
            array[i] = array[array.length - i -1];
            array[array.length - i -1] = tmp;
        }
    }

    /**
     * 直接选择排序
     * @param array
     */
    private void selectSort(int[] array){
        // 一般先排最右侧结果
        // 因第一排序位置是比较值，无需从第一元素排序，外层代表需要排序的位置，
        for (int i = 1; i < array.length; i++) {
            int index = 0;
            // 因每次循环都是从第一元素作为比较，故从第二元素开始比较，且不超出已排好序的位置
            for (int j = 1; j <= array.length - i; j++) {
                if (array[j] > array[index]){
                    index = j;
                }
            }
            int tmp = array[array.length -i];
            array[array.length-i] = array[index];
            array[index] = tmp;
        }
    }

//-----------------查找算法------------------
    /**
     * 二分法查找基本数据类型查询，其他类型Object需要实现Comparable接口
    */
    private static int binarySearch0(int[] a, int key) {
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
}
