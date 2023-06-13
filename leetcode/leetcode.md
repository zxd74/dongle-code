# 对一个整数m删除n位数后，顺序不变，输出最大值
* 题目说明：给定任意一个数字 m，然后给出数字 n，则需在 m 中去掉 n 位数，保持各位顺序不变的情况下，得到最大数。
* 输入描述：输入整数m,n.(1<=m<=1e100,1<=n<=100)
* 输出描述：输出删除后的最大数。
* 示例：输入1234 2，输出34

```java
import java.util.ArrayList;
import java.util.Scanner;

class Main {
    // 本实例支持正负整数处理
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String str_0 = scan.nextLine();
        String[] line_list_0 = str_0.trim().split(" ");
        ArrayList<Integer> arr = new ArrayList<>();
        for (String s : line_list_0) {
            arr.add(Integer.parseInt(s));
        }
        scan.close();
        int result = solution(arr);
        System.out.println(result);
    }

    public static int solution(ArrayList<Integer> arr) {
        int result = 0;

        // TODO: 请在此编写代码
        // 获取m，n
        if (arr == null || arr.size() == 0) {
            return result;
        }
        Integer m = arr.get(0);
        Integer n = arr.size() > 1 ? arr.get(1) : null;
        // 验证m，n有效性
        if (m == null || n == null || m == 0) {
            return result;
        }
        if (n <= 0) {
            return m;
        }
        // 判断正负
        boolean flag = true;
        if (m < 0) {
            flag = false;
            m = -m;
        }
        // 比较最大位数
        int max_num = (int) Math.pow(10, (n - 1));
        if (m <= max_num) {
            return result;
        }
        // 整数m转数组
        String ms = String.valueOf(m);
        Integer[] mss = new Integer[ms.length()];
        for (int i = 0; i < ms.length(); i++) {
            mss[i] = Integer.parseInt(Character.toString(ms.charAt(i)));
        }

        // 循环n次，每次处理一个最大或最小值
        solution(mss,n,flag);

        // 合并非null内容为整数
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mss.length; i++) {
            if (mss[i] != null) {
                sb.append(mss[i]);
            }
        }
        result = Integer.parseInt(sb.toString());
        // 根据正负处理最终结果
        if (!flag) {
            result = -result;
        }
        return result;
    }
    // 
    public static void solution(Integer[] mss,int n,boolean flag){
        for (int i = 0; i < n; i++) {
            int index = 0;
            Integer tmp = mss[0];
            for (int j = 1; j < mss.length; j++) {
                // 过滤无效对比
                if (tmp == null) {
                    tmp = mss[j];
                    index = j;
                    continue;
                }
                if (mss[j] == null) {
                    continue;
                }
                if (flag) { // 正数筛选最小值
                    if (tmp > mss[j]) {
                        tmp = mss[j];
                        index = j;
                    }
                } else { // 负数筛选理最大值
                    if (tmp < mss[j]) {
                        tmp = mss[j];
                        index = j;
                    }
                }
            }
            // 处理最值为null
            if (tmp != null) {
                mss[index] = null;
            }
        }
    }
}
```

# 8 字符串转整数
    * 读入字符串并丢弃无用的前导空格
    * 检查下一个字符（假设还未到字符末尾）为正还是负号，读取该字符（如果有）。 确定最终结果是负数还是正数。 如果两者都不存在，则假定结果为正。
    * 读入下一个字符，直到到达下一个非数字字符或到达输入的结尾。字符串的其余部分将被忽略。
    * 将前面步骤读入的这些数字转换为整数（即，"123" -> 123， "0032" -> 32）。如果没有读入数字，则整数为 0 。必要时更改符号（从步骤 2 开始）。
    * 如果整数数超过 32 位有符号整数范围 [−2^31,  2^31 − 1] ，需要截断这个整数，使其保持在这个范围内。具体来说，小于 −2^31 的整数* 应该被固定为 −2^31 ，大于 2^31 − 1 的整数应该被固定为 2^31 − 1 。
    * 返回整数作为最终结果。

注意：
* 本题中的空白字符只包括空格字符 ' ' 。
* 除前导空格或数字后的其余字符串外，请勿忽略 任何其他字符。

示例：
* "42" => 42
* "   -42" => -42
* "-54654685465" => -27483648
* "564654564654" => 27483647
* "a 987" => 0
* "" => 0
* "-5-" => -5
* "+-5" => 0
```java
class Solution {
    public int myAtoi(String str) {
        // if(str == null) return 0;
        str = str.trim();
        // 去空后为空输出0
        if (str.length() == 0) return 0;
        int num = 0,i = 0,digit = 0;boolean isF = false;
        char ch = str.charAt(i);
        // 判断首位正负
        if (ch == 43){ // ch == '+'
            i++;
        }else if (ch==45){ // ch == '-'
            isF = true;
            i++;
        }
        
        for (; i < str.length(); i++) {
            ch = str.charAt(i);
            if (47 < ch && ch < 58){ // if(Character.isDigit(ch))
                digit = Character.getNumericValue(ch);
                if (num > (Integer.MAX_VALUE - digit) / 10){
                    return isF ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                }
                num = num * 10 + digit;
            }else{
                break;
            }
        }
        return isF ? -num:num;
    }
}
```

# 9 数字是回环数字
* int整数数字对称
* 负数非回环
```java
// 初版 10ms
class Solution {
    public boolean isPalindrome(int x) {
        if (x == 0) return true;
        if (x<0) return false;
        
        String str = String.valueOf(x);

        for (int i = 0; i < str.length() / 2; i++) {
            if (str.charAt(i) != str.charAt(str.length() - 1 - i)){
                return false;
            }
        }
        return true;
    }
}

// 学习版
class Solution {
    public boolean isPalindrome(int x) {
        if (x<0 || (x!=0 && x%10==0)) return false;
        int rev = 0;
        while (x>rev){
            rev = rev*10 + x%10; // 相当于复制左半部分
            x = x/10;
        }
        return (x==rev || x==rev/10);
    }
}
```

# 10 正则表达式匹配
