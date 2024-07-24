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

# 13 Roman to Integer(中)
    Roman numerals are usually written largest to smallest from left to right.
    I can be placed before V (5) and X (10) to make 4 and 9. 
    X can be placed before L (50) and C (100) to make 40 and 90. 
    C can be placed before D (500) and M (1000) to make 400 and 900.
```py
# 45ms 86.56% 16.4MB 39.26%
class Solution:
    def romanToInt(self, s: str) -> int:
        sm = {"I":1,"V":5,"X":10,"L":50,"C":100,"D":500,"M":1000}
        sum = 0
        pre = ""
        num=0
        for i in s:
            if pre == "I" and (i == "V" or i=="X"):
                sum = sum - num
                num = sm[i] -1
            elif pre == "X" and (i == "L" or i=="C"):
                sum = sum - num
                num = sm[i] -10
            elif pre == "C" and (i == "D" or i=="M"):
                sum = sum - num
                num = sm[i] -100
            else:
                num = sm[i]
            sum = sum + num
            pre = i
        return sum
```
```py
# 52ms 55.28% 16.4MB 39.26%
class Solution:
    def romanToInt(self, s: str) -> int:
        romans = {'I':1,'V':5,'X':10,'L':50,'C':100,'D':500,'M':1000,'CD':400,'CM':900,'XC':90,'XL':40,'IX':9,'IV':4}
        roman = 0
        pre = 0
        for i in s:
            if pre == 0:
                pre = romans[i]
                continue
            if pre < romans[i]:
                roman += romans[i] - pre
                pre = 0
            else:
                roman += pre
                pre = romans[i]
            print( i,pre,roman)
        if pre != 0:
            roman += pre
        return roman
```
```py
# 58ms 25.26% 16.1MB 93.92%
class Solution:
    def romanToInt(self, s: str) -> int:
        romans = {'I':1,'V':5,'X':10,'L':50,'C':100,'D':500,'M':1000}
        roman = 0
        for i in range(len(s)):
            if i < len(s) -1 and romans[s[i]] < romans[s[i+1]]:
                roman -= romans[s[i]]
            else:
                roman += romans[s[i]]
        return roman
```

# 14 返回最长公共字符串前缀
    Write a function to find the longest common prefix string amongst an array of strings.
    If there is no common prefix, return an empty string "".
* 个人版
```java
public String longestCommonPrefix(String[] strs){
    if (strs == null || strs.length == 0) return "";
    if (strs.length == 1 && strs[0]!=null) return strs[0];
    StringBuilder sb = new StringBuilder();
    int len = 0;boolean cur=true;
    all: while(true){
        char c = 0;
        boolean cur_first=true;
        for (String str : strs) {
            if (str == null || len == str.length() || !cur)
                break all;
            if (cur_first) {
                c = str.charAt(len);
                cur_first = false;
            }
            cur = c == str.charAt(len);
        }
        if (cur){
            sb.append(c);
            len++;
        }else {
            break;
        }
    }
    return sb.toString();
}
```
* 官方最优解法
  * 先选一个前缀，用前缀与其它比较
  * 使用indexOf方法判断是否存在，不存在减一位继续
  * 不足：未判空，并且前缀缩减至空时应终止程序
```java
public String longestCommonPrefix(String[] strs){
    String prefix=strs[0];
    for (int i = 1; i < strs.length; i++) {
        while (strs[i].indexOf(prefix)!=0)
            prefix = prefix.substring(0,prefix.length()-1);
    }
    return prefix;
}
```
* 修订版
```java
public String longestCommonPrefix(String[] strs){
    // 预处理
    if (strs == null || strs.length == 0) return "";
    if (strs.length == 1 && strs[0]!=null) return strs[0];
    String prefix=strs[0];
    for (int i = 1; i < strs.length; i++) {
        if(strs[i]==null || strs[i].equals("")) return ""; // 判空
        while (strs[i].indexOf(prefix)!=0)
            prefix = prefix.substring(0,prefix.length()-1);
            if(prefix.equals("")) return "";//防止缩减至空
    }
    return prefix;
}
```

# 20 括号开闭校验
    Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.

    An input string is valid if:
     Open brackets must be closed by the same type of brackets.
     Open brackets must be closed in the correct order.
     Every close bracket has a corresponding open bracket of the same type.

* 算法逻辑：
  * 开括号入栈，比括号同类型出栈
* 初版
  * 代码较多，但简单易懂
  * 使用了java源码的Stack类，对象比较大
```java
public static boolean isValid(String s){
    Stack<Integer> stack = new Stack<>();
    boolean flag = true;
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (c == '(') {
            stack.push(1);
        }
        if (c == ')') {
            if (stack.isEmpty() || stack.pop()!=1) {
                return false;
            }
        }
        if (c == '[') {
            stack.push(2);
        }
        if (c == ']') {
            if (stack.isEmpty() || stack.pop()!=2) {
                return false;
            }
        }
        if (c == '{') {
            stack.push(3);
        }
        if (c == '}') {
            if (stack.isEmpty() || stack.pop()!=3) {
                return false;
            }
        }
    }
    return flag && stack.isEmpty();
}
```
* 优解
  * 代码简洁，但可读性较差
  * 逻辑：开括号入栈，比括号同类型出栈
```java
public boolean isValid(String s) {
    int i=-1;
    char[] stack=new char[s.length()];

    for(char ch:s.toCharArray())
    {
        if(ch=='[' ||ch=='{' ||ch=='('){
            stack[++i]=ch;
        }
        else{
            if(i>=0 && ((stack[i]=='{' && ch=='}') ||
                        (stack[i]=='[' && ch==']') ||
                        (stack[i]=='(' && ch==')')))
                --i;
            else
                return false;
        }
    }
    return i==-1;
}
```
* 修订出版
```java
public boolean isValid(String s) {
    Stack<Character> stack = new Stack<>();
    Character pre;
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (c == '(' || c == '[' || c == '{') {
            stack.push(c);
        }
        else{
            if (stack.isEmpty()) {
                return false;
            }
            pre = stack.pop();
            if ((c==')' && pre!='(')||(c==']' && pre!='[')||(c=='}' && pre!='{')) {
                return false;
            }
        }
    }
    return stack.isEmpty();
}
```
# 21 合并两个有序列表
    You are given the heads of two sorted linked lists list1 and list2.

    Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.

    Return the head of the merged linked list.
```java
public static class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
```
* 个人实践
  * 基础实践
  * 代码繁琐，局部变量定义较多
```java
public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
    ListNode list=null,next = null,node;
    while(true){
        if(list1 == null || list2 == null) {
            if(list == null){
                list = list1 ==null?list2:list1;
            }else{
                next.next = list1 ==null?list2:list1;
            }
            break;
        }

        if(list2.val > list1.val){
            node = list1;
            list1 = list1.next;
        }else{
            node = list2;
            list2 = list2.next;
        }		
        node.next = null;
        if(list==null){
            list =next =  node;
        }else{
            next.next = node;
            next = node;
        }
    }
    return list;
}
```
```java
// 个人版修订，但不推荐
public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
    ListNode list=new ListNode(-101),next = list;
    while(true){
        if(list1 == null || list2 == null) {
            next.next = list1 ==null?list2:list1;
            break;
        }

        if(list2.val > list1.val){
            next.next = list1;
            list1 = list1.next;
        }else{
            next.next = list2;
            list2 = list2.next;
        }
        next = next.next;
    }
    return list.next;
}
```
* 最优解
  * 代码少，使用递归逻辑实现，无局部变量
```java
public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
    if (list1 == null) return list2;
    if (list2 == null) return list1;
    if (list1.val < list2.val) {
        list1.next = mergeTwoLists(list1.next, list2);
        return list1;
    }else {
        list2.next = mergeTwoLists(list1, list2.next);
        return list2;
    }
}
```

# 11 最大水量(中)
    You are given an integer array height of length n. There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).

    Find two lines that together with the x-axis form a container, such that the container contains the most water.

* 约束：
  * `n == height.length`
  * `2 <= n <= 10^5`
  * `0 <= height[i] <= 10^4`
* 个人解法:逻辑通，但超时
```java
public static int maxArea(int[] height) {
    int min=0,max=Math.min(height[0], height[1]);
    for(int i=2;i<height.length;i++){
        for(int j = 0;j<i;j++){
            min = Math.min(height[j], height[i]) * (i-j);
            if (min>max) {
                max = min;
            }
        }
    }
    return max;
}
```
* 最优解
```java
class Solution {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;

        while (left < right) {
            int currentArea = Math.min(height[left], height[right]) * (right - left);
            maxArea = Math.max(maxArea, currentArea);

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }
}
```
* 疑问：为什么最优解中，left和right的值一定会小于最大值?
  * 答案：因为每次循环，都会将left和right的值向中间移动，所以left和right的值一定会小于最大值
* 最优解升级版:持续降低循环次数
```java
public int maxArea(int[] height) {
    int max = 0;
    int s = 0;
    int e = height.length-1;

    while(s<e){
        int h = Math.min(height[s],height[e]);
        int amt = h*(e-s);
        if(max<amt) max = amt;
        while(s<e && height[s]<=h){
            s++;
        }
        while(s<e && height[e]<=h){
            e--;
        }
    }
    return max;
}
```
# 12 Intege to Roman (中)
    Roman numerals are formed by appending the conversions of decimal place values from highest to lowest. Converting a decimal place value into a Roman numeral has the following rules:
* 逻辑
  * If the value does not start with 4 or 9, select the symbol of the maximal value that can be subtracted from the input, append that symbol to the result, subtract its value, and convert the remainder to a Roman numeral.
  * If the value starts with 4 or 9 use the subtractive form representing one symbol subtracted from the following symbol, for example, 4 is 1 (I) less than 5 (V): IV and 9 is 1 (I) less than 10 (X): IX. Only the following subtractive forms are used: 4 (IV), 9 (IX), 40 (XL), 90 (XC), 400 (CD) and 900 (CM).
  * Only powers of 10 (I, X, C, M) can be appended consecutively at most 3 times to represent multiples of 10. You cannot append 5 (V), 50 (L), or 500 (D) multiple times. If you need to append a symbol 4 times use the subtractive form.
* 约束 ：`1 <= num <= 3999`
* 基本解法：分段处理
  * 1000+
  * >=500：900，500<900
  * >=100: 400,100<400
  * >=50: 90,50<90
  * >=10: 40,10<40
  * >=5: 9,5<9
  * >0: 4,1<4
```java
public static String intToRoman(int num) {
        StringBuilder sb = new StringBuilder();
        while (num>=1000) {num -= 1000;sb.append("M");}
        while (num>=500) {
            if (num/100 == 9) {num -= 900;sb.append("CM");
            }else{num -= 500;sb.append("D");}
        }
        while (num>=100) {
            if (num/100 == 4) {num -= 400;sb.append("CD");
            }else{num -= 100;sb.append("C");}
        }
        while (num>50) {
            if (num/10 == 9) {num -= 90;sb.append("XC");
            }else{num -= 50;sb.append("L");}
        }
        while (num>10) {
            if (num/10 == 4) {num -= 40;sb.append("XL");
            }else{num -= 10;sb.append("X");}
        }
        while (num>5) {
            if (num == 9) {num -= 9;sb.append("IX");
            }else{num -= 5;sb.append("V");}
        }
        while (num>0) {
            if (num == 4) {num -= 4;sb.append("IV");
            }else{num -= 1;sb.append("I");}
        }
        return sb.toString();
    }
```
* 其它解法
  * 将所有可能一一对应罗列，通过匹配方式输出
```java
// O(n)
public String intToRoman(int num) {
    int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
    String[] romans = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
    StringBuilder sb=new StringBuilder();
    for(int i=0;i<values.length;i++){
        while(num>=values[i]){
            num-=values[i];
            sb.append(romans[i]);
        }
    }
    return sb.toString();
}
```
```java
// O(1)
public String intToRoman(int num) {
    string ones[] = {"","I","II","III","IV","V","VI","VII","VIII","IX"};
    string tens[] = {"","X","XX","XXX","XL","L","LX","LXX","LXXX","XC"};
    string hrns[] = {"","C","CC","CCC","CD","D","DC","DCC","DCCC","CM"};
    string ths[]= {"","M","MM","MMM"};
        
    return ths[num/1000] + hrns[(num%1000)/100] + tens[(num%100)/10] + ones[num%10];
}
```
# 15 Three Sum(中)
    Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.

    Notice that the solution set must not contain duplicate triplets.

* 约束：
  * `3 <= nums.length <= 3000`
  * `-10^5 <= nums[i] <= 10^5`

* 个人版：超时，
  * 原因：三重循环，并且判重逻辑，补充额外大对象变量
```java
public List<List<Integer>> threeSum(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    Map<String, Integer> sets = new HashMap<>();
    for(int i = 0; i < nums.length - 2; i++){
        for(int j = i + 1; j < nums.length - 1; j++){
            for(int k = j + 1; k < nums.length; k++){
                if(nums[i] + nums[j] + nums[k] == 0){
                    List<Integer> list = Arrays.asList(nums[i], nums[j], nums[k]);
                    list.sort((l1,l2)->l1-l2);
                    String key = list.get(0) + "," + list.get(1) + "," + list.get(2);
                    if (!sets.containsKey(key)) {
                        result.add(list);
                        sets.put(key, null);
                    }
                }
            }
        }
    }
    return result;
}
```
* 最优解：首先对数组进行排序，根据排序结果再匹配
  * 为避免相邻元素重复：第一和第二个元素都要对相同值跳过
  * 第一和第二从左侧开始，第三从右侧开始匹配
    * 若和大于0，则第三过大，向左移动
    * 若和小于0，则第二过小，向右移动
    * 若和等于0，则加入结果集，同时第二向右移动
  * 两重循环；通过排序解决重复问题
```java
public List<List<Integer>> threeSum(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    Arrays.sort(nums); // 重点，先排序

    for(int i = 0; i < nums.length -2; i++){
        if (i > 0 && nums[i] == nums[i-1]) {continue;} // 避免重复，因已排序，故而相邻相同时，对应的结果相同
        
        int j = i + 1, k = nums.length - 1;
        if (nums[i] + nums[j] + nums[k]>0) {break;} // 修订：当初次匹配的和大于0，则无需继续，因已排序，和只会越来越大

        while (j<k) {
            int total = nums[i] + nums[j] + nums[k];
            if (total>0) {k--;}
            else if (total<0) {j++;}
            else{
                result.add(Arrays.asList(nums[i], nums[j], nums[k]));
                j++;
                while (j<k && nums[j] == nums[j-1]) { // 避免第二元素重复，同理
                    j++;
                }
            }
        }
    }
    return result;
}
```
# 16 最接近目标值的三数之和
    Given an integer array nums of length n and an integer target, find three integers in nums such that the sum is closest to target.

* 思路：和三数之和为0的问题类似，先排序，然后比较
```java
public int threeSumClosest(int[] nums, int target) {
    Arrays.sort(nums);
    int count = -1001;
    for (int i = 0; i < nums.length; i++) {
        int j = i + 1, k = nums.length - 1;
        while (j < k) {
            int sum = nums[i] + nums[j] + nums[k];
            if (sum == target) return target;

            if (count == -1001) count = sum;
            if(sum < target) j++;
            else if (sum> target) k--;

            if (Math.abs(sum - target)<Math.abs(count - target)) count = sum;
        }
    }
    return count;
}
```

# 17 电话号码的字母组合
    Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent. Return the answer in any order.

    A mapping of digits to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters.

    { "2":"abc", "3":"def", "4":"ghi", "5":"jkl", "6":"mno", "7":"pqrs", "8":"tuv", "9":"wxyz" }

* 约束
  * `0 <= digits.length <= 4`
  * `digits[i] is a digit in the range ['2', '9'].`
* 个人版：字符串循环拼接
```java
public List<String> letterCombinations(String digits) {
    List<String> result = new ArrayList<>();

    String[] letters = { "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };

    for (int i = 0; i < digits.length(); i++) {
        String[] letter = letters[digits.charAt(i) - 50].split("");

        if (result.size() == 0) {
            result.addAll(Arrays.asList(letter));
            continue;
        }

        List<String> temp = new ArrayList<>();
        for (String str : letter) {
            for(String str2:result) temp.add(str2 + str);
        }
        result = temp;
    }

    return result;
}
```
* 升级版：
  * 使用StringBuilder加速字符串拼接
  * 使用回溯法取代循环，缩减代码
```java
public List<String> letterCombinations(String digits) {
    List<String> result = new ArrayList<>();

    String[] letters = { "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };

    combina(digits, letters, new StringBuilder(), 0, result);

    return result;
}

private void combina(String digits,String[] letters, StringBuilder sb,int start, List<String> result){
    if (start == digits.length()) {
        result.add(sb.toString());
        return;
    }

    String[] strs = letters[digits.charAt(start) - 50].split("");
    for (int i = 0; i < strs.length; i++) {
        sb.append(strs[i]);
        combina(digits, letters, sb, start + 1, result);
        sb.deleteCharAt(sb.length() - 1);
    }

}
```
# 18 四数之和
    Given an array nums of n integers, return an array of all the unique quadruplets [nums[a], nums[b], nums[c], nums[d]] such that:
    * 0 <= a, b, c, d < n
    * a, b, c, and d are distinct.
    * nums[a] + nums[b] + nums[c] + nums[d] == target

* 约束
  * `1 <= nums.length <= 200`
  * `-10^9 <= nums[i] <= 10^9`
  * `-10^9 <= target <= 10^9`
* 思考
  * 仿照三数之和，先排序，再对比
  * 由于数值元素限制，四数之和可能大于Integer，使用long类型转换，避免溢出
  * 固定前两个和，由后两个和与差值做对比，避免重复计算前两个和
  * 若循环者下一个值与前一个值相同，则跳过，进入下一次循环
* 个人版
```java
public List<List<Integer>> fourSum(int[] nums, int target) {
    List<List<Integer>> res = new ArrayList<>();
    Arrays.sort(nums);
    for (int i = 0; i < nums.length - 3; i++) {
        if(i>0 && nums[i]==nums[i-1]) continue; 
        for (int j = i+1; j < nums.length - 2 ; j++) {
            if (j>i+1 && nums[j] == nums[j-1]) continue;

            int k = j+1, l = nums.length - 1;
            while (k < l) {
                long sum = (long)nums[i] + nums[j] + nums[k] + nums[l];
                if (sum > Integer.MAX_VALUE) {
                    l--;
                    continue;
                }
                
                if (sum < target) k++;
                else if (sum>target) l--;
                else{
                    res.add(Arrays.asList(nums[i], nums[j], nums[k], nums[l]));
                    while (k<l && nums[k] == nums[k+1]) k++;
                    while (k<l && nums[l] == nums[l-1]) l--;
                    k++;
                }
            }
        }
    }

    return res;
}
```
* 优化版
  * 固定前两个和，避免重复计算
```java
public List<List<Integer>> fourSum(int[] nums, int target) {
    List<List<Integer>> res = new ArrayList<>();
    Arrays.sort(nums);
    for (int i = 0; i < nums.length - 3; i++) {
        if(i>0 && nums[i]==nums[i-1]) continue; 
        for (int j = i+1; j < nums.length - 2 ; j++) {
            if (j>i+1 && nums[j] == nums[j-1]) continue;
            long sum = (long)target - nums[i] - nums[j];
            int k = j+1, l = nums.length - 1;
            while (k < l) {
                long twosum = nums[k] + nums[l];
                if (sum < twosum)  l--;
                else if (sum > twosum) k++;
                else{
                    res.add(Arrays.asList(nums[i], nums[j], nums[k], nums[l]));
                    while (k<l && nums[k] == nums[k+1]) k++;
                    while (k<l && nums[l] == nums[l-1]) l--;
                    k++;
                    l--;
                }
            }
        }
    }

    return res;
}
```

# 19 从List结尾移除第N项节点
    Given the head of a linked list, remove the nth node from the end of the list and return its head.
* **约束**
  * 1 <= sz <= 30
  * 0 <= Node.val <= 100
  * 1 <= n <= sz
```java
public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
```
* 普通版：
  * 时间复杂度为O(n)
  * 易理解
```java
public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode next = head;int total = 0;
    while (next != null) {
        total++;
        next = next.next;
    }
    if (total == n) {
        return head.next;
    }
    total = total - n;
    next = head;
    while (total>1) {
        total--;
        next = next.next;
    }
    next.next = next.next.next;
    return head;
}
``
* 递归版
    * 时间复杂度高于O(n),低于O(n^2)
    * 使用了递归思想，简化了代码
```java
public ListNode removeNthFromEnd(ListNode head, int n) {
    return removeNthFromEnd(head,head,n);
}
private ListNode removeNthFromEnd(ListNode head,ListNode node,int n){
    int size = ListNodeSize(node);
    if (size-1 > n ) return removeNthFromEnd(head,node.next,n);
    if (size  == n) return head.next;
    node.next = node.next.next;
    return head;
}
private int ListNodeSize(ListNode node){
    if (node == null) return 0;
    return ListNodeSize(node.next)+1;
}
```
# 20 括号的可能组合
    Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
* 约束
  * `1 <= n <= 8`
  * 结果需要**排序**，否则和LeetCode结果顺序不一致就会错误
* 个人版
  * 初次想法：比前一个，将括号添加到每个元素头部，尾部，外部，有缺漏(无法在中间插入，导致缺失部分可能性)
  * 最终想法：对一个单独括号匹配，给这个单括号在外部和相邻位置分别再加一个单括号，然后越过该单括号位置后，再继续下一次循环，直至找不到为止
    * 由于其它情形存在，最终合并后就包含所有
    * **时间复杂度O(n^2)**
```java
public List<String> generateParenthesis(int n){
    if(n == 1) return Collections.singletonList("()");
    Set<String> set = new HashSet<String>();
    generateParenthesis(n-1).forEach(sub->{
        int start = 0;
        while ((start = sub.indexOf("()", start)) != -1) {
            set.add(sub.substring(0, start) + "()()" + sub.substring(start + 2));
            set.add(sub.substring(0, start) + "(())" + sub.substring(start + 2));
            start += 2;
        }
    });
    return set.stream().sorted().collect(Collectors.toList());
}
```
* 官方较优解：分别从左右匹配,左侧每个位置加`(`,右侧每个位置加`)`，直至左右对等
  * **时间复杂度 O(n)**
```java
public List<String> generateParenthesis(int n){
    List<String> result = new ArrayList<String>();
    StringBuilder sb = new StringBuilder();
    generateParenthesis(result, sb, 0, 0, n);
    return result;
}
public void generateParenthesis(List<String> result, StringBuilder sb, int leftCount,int rifghtCount,int n) {
    if (leftCount == n && rifghtCount == n) {
        result.add(sb.toString());
        return;
    }
    if (leftCount<n) {
        sb.append("(");
        generateParenthesis(result, sb, leftCount + 1, rifghtCount, n);
        sb.deleteCharAt(sb.length()-1); // 减去多增加的 ( 字符
    }
    if (rifghtCount<n && rightCount<leftCount) {
        sb.append(")");
        generateParenthesis(result, sb, leftCount, rifghtCount + 1, n);
        sb.deleteCharAt(sb.length()-1); // 减去多增加的 ) 字符
    }
}
```