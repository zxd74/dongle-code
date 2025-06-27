- [对一个整数m删除n位数后，顺序不变，输出最大值](#对一个整数m删除n位数后顺序不变输出最大值)
- [8 字符串转整数](#8-字符串转整数)
- [9 数字是回环数字](#9-数字是回环数字)
- [10 正则表达式匹配](#10-正则表达式匹配)
- [13 Roman to Integer(中)](#13-roman-to-integer中)
- [14 返回最长公共字符串前缀](#14-返回最长公共字符串前缀)
- [20 括号开闭校验](#20-括号开闭校验)
- [21 合并两个有序列表](#21-合并两个有序列表)
- [11 最大水量(中)](#11-最大水量中)
- [12 Intege to Roman (中)](#12-intege-to-roman-中)
- [15 Three Sum(中)](#15-three-sum中)
- [16 最接近目标值的三数之和](#16-最接近目标值的三数之和)
- [17 电话号码的字母组合](#17-电话号码的字母组合)
- [18 四数之和](#18-四数之和)
- [19 从List结尾移除第N项节点](#19-从list结尾移除第n项节点)
- [20 括号的可能组合](#20-括号的可能组合)
- [23 合并多个有序链表](#23-合并多个有序链表)
- [24 两两交换链表中的节点](#24-两两交换链表中的节点)
- [25 K 个一组翻转链表](#25-k-个一组翻转链表)
- [26 删除有序数组中的重复项](#26-删除有序数组中的重复项)
- [27 移除指定值元素](#27-移除指定值元素)
- [28 Find the Index of the First Occurrence in a String](#28-find-the-index-of-the-first-occurrence-in-a-string)
- [29 Divide Two Integers](#29-divide-two-integers)
- [30 Substring with Concatenation of All Words](#30-substring-with-concatenation-of-all-words)
- [31. 下一个排列](#31-下一个排列)
- [32 最长有效圆括号](#32-最长有效圆括号)
- [33 搜索旋转排序数组](#33-搜索旋转排序数组)
- [34 在排序数组中查找元素的第一个和最后一个位置](#34-在排序数组中查找元素的第一个和最后一个位置)
- [35 在排序数组中查找插入位置(Easy)](#35-在排序数组中查找插入位置easy)
- [36 有效的数独(中)](#36-有效的数独中)
- [37 解数独(难)](#37-解数独难)
- [38 报数序列(中)](#38-报数序列中)
- [39 组合总和(中)](#39-组合总和中)
- [40 组合总和 II(中)](#40-组合总和-ii中)
- [41 缺失的第一个正数(难)](#41-缺失的第一个正数难)
- [58. Length of Last Word(简单)](#58-length-of-last-word简单)
- [66. Plus One(简单)](#66-plus-one简单)
- [67. Add Binary(简单)](#67-add-binary简单)
- [69. Sqrt(x)(简单)](#69-sqrtx简单)
- [70. Climbing Stairs(简单)](#70-climbing-stairs简单)
- [83. Remove Duplicates from Sorted List(简单)](#83-remove-duplicates-from-sorted-list简单)
- [88. Merge Sorted Array(简单)](#88-merge-sorted-array简单)
- [94. Binary Tree Inorder Traversal(简单)](#94-binary-tree-inorder-traversal简单)
- [100. Same Tree(简单)](#100-same-tree简单)
- [101. Symmetric Tree(简单)](#101-symmetric-tree简单)
- [104. Maximum Depth of Binary Tree(简单)](#104-maximum-depth-of-binary-tree简单)
- [108. Convert Sorted Array to Binary Search Tree(简单)](#108-convert-sorted-array-to-binary-search-tree简单)
- [110. Balanced Binary Tree(简单)](#110-balanced-binary-tree简单)
- [111. Minimum Depth of Binary Tree(简单)](#111-minimum-depth-of-binary-tree简单)
- [112. Path Sum(简单)](#112-path-sum简单)
- [118. Pascal's Triangle(简单,帕斯卡三角形)](#118-pascals-triangle简单帕斯卡三角形)
- [119. Pascal's Triangle II(简单,帕斯卡三角形)](#119-pascals-triangle-ii简单帕斯卡三角形)
- [121. Best Time to Buy and Sell Stock(简单)](#121-best-time-to-buy-and-sell-stock简单)
- [125. Valid Palindrome(简单)](#125-valid-palindrome简单)
- [136. Single Number(简单)](#136-single-number简单)
- [141. Linked List Cycle(简单)](#141-linked-list-cycle简单)
- [144. Binary Tree Preorder Traversal(简单)](#144-binary-tree-preorder-traversal简单)
- [145. Binary Tree Postorder Traversal(简单)](#145-binary-tree-postorder-traversal简单)
- [157. Read N Characters Given Read4(中等)](#157-read-n-characters-given-read4中等)
- [160. Intersection of Two Linked Lists(简单)](#160-intersection-of-two-linked-lists简单)
- [163. Missing Ranges(中等)](#163-missing-ranges中等)
- [168. Excel Sheet Column Title(简单)](#168-excel-sheet-column-title简单)
- [169. Majority Element(简单)](#169-majority-element简单)
- [170. Two Sum III - Data Structure Design(简单)](#170-two-sum-iii---data-structure-design简单)
- [171. Excel Sheet Column Number(简单)](#171-excel-sheet-column-number简单)
- [175. Combine Two Tables(简单)](#175-combine-two-tables简单)
- [181. Employees Earning More Than Their Managers(简单)](#181-employees-earning-more-than-their-managers简单)
- [182. Duplicate Emails(简单)](#182-duplicate-emails简单)
- [183. Customers Who Never Order(简单)](#183-customers-who-never-order简单)



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
// 初版：通过字符串索引前后对比实现
class Solution {
    public boolean isPalindrome(int x) {
        if (x == 0) return true;
        if (x < 0 || x%10==0) return false;
        
        String str = String.valueOf(x);
        int lo = 0, hi = str.length()-1;
        while (lo < hi) {
            if (str.charAt(lo++) != str.charAt(hi--))
                return false;
        }
        return true;
    }
}

// 学习版：通过余数反转对比实现
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
# 23 合并多个有序链表
    You are given an array of k linked-lists lists, each linked-list is sorted in ascending order.
* 约束：
  * `k == lists.length`
  * `0 <= k <= 10^4`
  * `0 <= lists[i].length <= 500`
  * `-10^4 <= lists[i][j] <= 10^4`
  * `lists[i] is sorted in ascending order.`
  * `The sum of lists[i].length will not exceed 10^4.`
```java
public static class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
```
* 个人版：
  * 针对链表操作，头部初始化自定义，后续节点在next处绑定，避免判断头部是否为空
  * 遍历链表，按顺序插入到新链表中
  * 时间复杂度O(n^2)
```java
public static ListNode mergeKLists(ListNode[] lists) {
    if (lists.length == 0) return null;
    if(lists.length == 1) return lists[0];
    ListNode head = new ListNode(),list,pre,node ;
    for(int i = 0;i<lists.length;i++){
        if((node = lists[i]) == null) continue;
        if (head.next == null) {
            head.next = node;
            continue;
        }
        pre = head;
        list = head.next;
        while(list!=null && node != null){
            if(list.val<=node.val){
                pre = pre.next = list;
                list = list.next;
                continue;
            }
            
            pre = pre.next = node;
            node = node.next;
            
        }
        ListNode tmp = list == null ? node : list;
        while (tmp!=null) {
            pre = pre.next = tmp;
            tmp = tmp.next;
        }
    }
    return head.next;
}	
```
* 官方优解
  * 采用递归算法，最优解为单独一个ListNode
  * 采用二分法排序，直至最终只有一个元素结果
  * 时间复杂度O(n)
```java
public ListNode mergeKLists(ListNode[] lists) {
    if (lists.length == 0)  return null;
    if (lists.length == 1) return lists[0];
    return mergeSorted(0,lists.length-1,lists);
}

public ListNode mergeSorted(int start,int end,ListNode[] lists){
    if (start == end) {
        return lists[start];
    }
    int mid = (start + end) >>> 1;
    ListNode left = mergeSorted(start,mid,lists),right = mergeSorted(mid+1,end,lists);
    return mergeTwoLists(left, right);
}
public ListNode mergeTwoLists(ListNode list1,ListNode list2){
    ListNode head = new ListNode();
    ListNode next = head;
    while(list1!=null && list2!=null){
        if(list1.val<=list2.val){
            next = next.next = list1;
            list1 = list1.next;
            continue;
        }
        
        next = next.next = list2;
        list2 = list2.next;
    }
    ListNode tmp = list1!=null ? list1 : list2;
    while (tmp !=null) {
        next = next.next = tmp;
        tmp = tmp.next;
    }
    return head.next;
}
```
# 24 两两交换链表中的节点
    Given a linked list, swap every two adjacent nodes and return its head. You must solve the problem without modifying the values in the list's nodes (i.e., only nodes themselves may be changed.)

* 约束：
  * `The number of nodes in the list is in the range [0, 100].`
  * `0 <= Node.val <= 100`
* 个人版
  * 采用递归算法，最优解为节点为空或下一个节点为空
  * 每个次递归时，校验两个节点(当前节点和下一个节点)
    * 子递归，已当前节点的下下各节点为开头
    * 当前递归交换两个节点，然后下下各节点为下次递归结果
```java
public ListNode swapPairs(ListNode head) {
    if(head == null) return null;
    if(head.next == null) return head;
    
    ListNode next = head.next;
    ListNode nh = swapPairs(next.next);
    next.next = head;
    head.next = nh;
    head = next;
    return head;
}
```

# 25 K 个一组翻转链表
    Given the head of a linked list, reverse the nodes of the list k at a time, and return the modified list.

    k is a positive integer and is less than or equal to the length of the linked list. If the number of nodes is not a multiple of k then left-out nodes, in the end, should remain as it is.

    You may not alter the values in the list's nodes, only nodes themselves may be changed.

* 约束：
  * `The number of nodes in the list is n.`
  * `1 <= k <= n <= 5000`
  * `0 <= Node.val <= 1000`
* 思路
  * 先统计k长度的不为空的节点ListNode
    * 长度不够时直接保持原样返回
  * 将k个节点做**反转**(注意是反转，不是兑换第k个节点)
  * 若后续还有节点，则采用递归形式继续，直至结束
```java
public static ListNode reverseKGroup1(ListNode head, int k) {
    if(head == null) return head;
    ListNode curr = head,dummy = null,prev = null;
    int count = 0;
    while(curr != null && count < k){
        curr = curr.next;
        count++;
    }
    if (count<k) return head;
    count = 0;curr = head;
    while(curr != null && count < k){
        dummy = curr.next;
        curr.next = prev;
        prev = curr;
        curr = dummy;
        count++;
    }
    if(dummy != null) head.next = reverseKGroup(dummy, k);
    return prev;
}
```
# 26 删除有序数组中的重复项
    Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place such that each unique element appears only once. The relative order of the elements should be kept the same. Then return the number of unique elements in nums.

    Consider the number of unique elements of nums to be k, to get accepted, you need to do the following things:

    Change the array nums such that the first k elements of nums contain the unique elements in the order they were present in nums initially. The remaining elements of nums are not important as well as the size of nums.
    
    Return k.

* 约束
  * `1 <= nums.length <= 3 * 10^4`
  * `-100 <= nums[i] <= 100`
  * `nums is sorted in non-decreasing order.`
* 思路：
  * 无需考虑第一个元素，遍历直接从第二个开始
  * 记录结果有效元素索引
  * 以遍历值和有效索引对应值比较
    * 相等过滤
    * 不等，在有效索引下一位填充该值
  * 结果返回有效索引值+1
```java
public int removeDuplicates(int[] nums) {
    int idx = 0;
    for(int i = 1;i<nums.length;i++){
        if(nums[idx] == nums[i]) continue;
        nums[++idx] = nums[i];
    }
    return idx+1;
}
```

# 27 移除指定值元素
    Given an integer array nums and an integer val, remove all occurrences of val in nums in-place. The order of the elements may be changed. Then return the number of elements in nums which are not equal to val.

    Consider the number of elements in nums which are not equal to val be k, to get accepted, you need to do the following things:

    Change the array nums such that the first k elements of nums contain the elements which are not equal to val. The remaining elements of nums are not important as well as the size of nums.
    Return k.

* 约束
  * `0 <= nums.length <= 100`
  * `0 <= nums[i] <= 50`
  * `0 <= val <= 100`
* 思路：同上
```java
public int removeElement(int[] nums, int val) {
    int idx = 0;
    for(int i = 0;i<nums.length;i++){
        if(nums[i] == val) continue;
        nums[idx++] = nums[i];
    }
    return idx;
}
```

# 28 Find the Index of the First Occurrence in a String
    Given two strings needle and haystack, return the index of the first occurrence of needle in haystack, or -1 if needle is not part of haystack.

* 约束；
  * `1 <= haystack.length, needle.length <= 10^4`
  * `haystack and needle consist of only lowercase English characters.`
* 思路：
  * 可以使用各语言字符串原生操作`indexOf`
* 思路：
  * 比较长度，若匹配内容长，直接返回-1
  * 遍历被匹配内容，判断第一个字符是否相等，不等下次循环
  * 相等继续匹配下一个字符是否相等
  * 若结束都未匹配完或者其它字符不等则返回-1，
  * 否则返回当前被匹配索引值
```java
// haystack.indexOf(needle);
public int strStr(String haystack, String needle) {
    if(haystack.length()<needle.length()) return -1;
    A:for(int i=0;i<haystack.length();i++){
        if(haystack.charAt(i) != needle.charAt(0)) continue A;
        if(needle.length() ==1 ) return i;
        boolean flag = false;
        for(int j=1;j<needle.length() && i+j<haystack.length();j++){
            if(i+j==haystack.length()-1 && j!=needle.length()-1) return -1;
            if(!(flag = haystack.charAt(i+j) == needle.charAt(j))) continue A;
        }
        if (flag) return i;
    }
    return -1;
}
```
* **优化版**
  * 被匹配内容索引**截止**(总长-匹配内容长度)
  * 若匹配结果索引值+1等于匹配内容长度，则返回当前被匹配索引值
  * 否则返回-1
```java
public int strStr(String haystack, String needle) {
    int hl = haystack.length(),nl = needle.length();
    if(hl<nl) return -1;
    for(int i=0; i<=hl-nl; i++){
        int j=0;
        while(j<nl && haystack.charAt(i+j) == needle.charAt(j)) j++;
        if(j==nl) return i;
    }
    return -1;
}
```

# 29 Divide Two Integers
    Given two integers dividend and divisor, divide two integers without using multiplication, division, and mod operator.

    The integer division should truncate toward zero, which means losing its fractional part. For example, 8.345 would be truncated to 8, and -2.7335 would be truncated to -2.

    Return the quotient after dividing dividend by divisor.

* 约束：
  * `-2^31 <= dividend, divisor <= 2^31 - 1`
  * `divisor != 0`
* 个人版
  * 将所有值转为long类型正数
  * 判断最终结果正负
  * 处理除数为1的特殊情况，根据结果正负和被除数的限制，返回结果
```java
// 如果允许使用乘除，模式运算，可以考虑 
/**
 * if(dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;
 * return dividend/divisor
 */
public int divide(int dividend, int divisor) {
    if(dividend == 0) return 0;
    int count = 0;
    boolean df,rf,rsf;
    long dl = (df=dividend>0)?dividend:-((long)dividend);
    long d2 = (rf = divisor>0)?divisor:-((long)divisor);
    rsf = df == rf;
    if(d2 == 1) return rsf ? dividend==Integer.MIN_VALUE?Integer.MAX_VALUE:df?dividend:-dividend:df?-dividend:dividend;
    if(dl<d2) return 0;
    if(dl == d2) return rsf?1:-1;
    while(dl>=d2){
        dl -= d2;
        count++;
    }
    return rsf?count:-count;
}
```
* 优化版
  * 将dividend为最小Integer和divisor为-1的情况单独处理
  * 将Integer类型转Long类型，避免溢出
  * 加快逻辑：将被除数与除数的倍数比较(使用**位运算**)，结果直接加倍数，并将被除数减去除数倍数倍，然后继续重复，直至被除数小于除数
```java
public int divide(int dividend, int divisor) {
    if(dividend == 0) return 0;
    if(dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;
    int result = 0;
    boolean df,rf,rsf;
    long d1 = (df=dividend>0)?dividend:-((long)dividend);
    long d2 = (rf = divisor>0)?divisor:-((long)divisor);
    rsf = df == rf;
    if(d2 == 1) return rsf ? df?dividend:-dividend:df?-dividend:dividend;
    if(d1<d2) return 0;
    while(d1>=d2){
        int count = 0;
        while(d1>=(d2<<(count+1))) count++;
        result += 1<<count;
        d1-= d2 << count;
    }
    return rsf?result:-result;
}
```
# 30 Substring with Concatenation of All Words
    You are given a string s and an array of strings words. All the strings of words are of the same length.

    A concatenated string is a string that exactly contains all the strings of any permutation of words concatenated.

    For example, if words = ["ab","cd","ef"], then "abcdef", "abefcd", "cdabef", "cdefab", "efabcd", and "efcdab" are all concatenated strings. "acdbef" is not a concatenated string because it is not the concatenation of any permutation of words.
    Return an array of the starting indices of all the concatenated substrings in s. You can return the answer in any order.

约束：
* `1 <= s.length <= 10^4`
* `1 <= words.length <= 5000`
* `1 <= words[i].length <= 30`
* `s and words[i] consist of lowercase English letters.`
* 个人版
  * 自定义一个节点，存储字符串和索引值
    * 对于重复值，先排序，再获取索引值
  * 检查索引有效值
    * 按索引值排序
    * 判断两两索引值之间差是否为单个字符串长度(数组中的字符串)
    * 直至循环完都相等，则记录第一个索引值(之前已排序)
    * 不相等，则对第一个字符串重新获取索引值
      * 需要排除重复值的索引值，即以重复值的最大索引+单个字符串长度为起始开始检索索引
    * 重新检查索引有效值
  * **问题**：部分用例和leetcode执行结果不一致
    * `s = "barfoofoobarthefoobarman", words = ["bar","foo","the"],result = [13]`,本地结果为`[13]`,leetcode输出`[]`导致错误无法提交
    * 不确定问题在哪里，代码感觉没有问题, 在其它平台执行结果都是正确的(只能说可能和leetcode执行逻辑不匹配)
    * 代码确实繁重了，可以优化
    * 借助了自定义类
```java
public List<Integer> findSubstring(String s, String[] words) {
    List<Integer> list = new ArrayList<>();
    int subLength = words[0].length(),subAllLength = words.length * subLength;
    // 1. 验证长度是否够总长限制
    if(s.length()<subAllLength ) return list; // 长度限制
    
    Node[] nodes = new Node[words.length];
    Arrays.sort(words);
    int preIndex = -1;
    // 2. 获取每个word对应索引值
    for(int i=0;i<words.length;i++){
        int subIndex = i != 0 && words[i] == words[i-1]?subIndex = s.indexOf(words[i],preIndex + subLength):s.indexOf(words[i]);
        if(subIndex == -1) return list; // 2.1 存在一个字符串未匹配的，则代表全部无效
        nodes[i] = new Node(words[i],subIndex);
        preIndex = subIndex;
    }
    // 3. 其它情况的索引校验
    checkIndex(s, nodes, list, subLength,subAllLength);
    return list;
}
public void checkIndex(String s,Node[] nodes,List<Integer> list,int subLength,int subAllLength){
    Arrays.sort(nodes,(l1,l2)-> l1.idx-l2.idx); // 先按索引值排序
    Node preNode = nodes[0];
    int idx = -1 ;	// 标记无效的索引，!= -1 代表无效
    // 4. 验证索引值是否连续
    for(int i = 1;i<nodes.length;i++){
        Node node = nodes[i];
        if(node.idx-preNode.idx != subLength){
            idx = i;
            break; // 中断循环
        }
        preNode = node;
    }
    if(idx == -1) list.add(nodes[0].idx); // 有效索引值记录
    if (s.length() - (nodes[0].idx+subLength) <subAllLength) return; //当剩余长度不够时，直接退出
    int start = nodes[0].idx;
    for(int i = 1;i<nodes.length;i++){
        if (nodes[i].sub.equals(nodes[0].sub)) {
            start = nodes[i].idx;
        }
    }
    // 重新匹配第一个节点索引值，重新校验：实际结果排序后可能以下一个节点为开头
    int subIndex = s.indexOf(nodes[0].sub,start + subLength); //  将从无效位置的前一个+字符串长度为索引开始重新匹配前面所有无效的下一个索引值
    if(subIndex == -1) return; // 无法获取下一个索引值时，程序中断
    nodes[0].idx = subIndex; // 更新第一个索引值
    // 继续校验
    checkIndex(s,nodes,list,subLength,subAllLength);
}

public class Node{
    private String sub;
    private int idx;
    public Node(String sub,int idx){
        this.sub = sub;
        this.idx = idx;
    }
}
```
* **优解版**
  * **以Map存储word出现次数**
  * 遍历单词数组
    * 遍历不超过字符串长度，每次**步长为一个单词长**
      * 将截取的字符串放入临时Map中，并将统计次数(默认0)+1
      * 如果截取的字段数和单词组数相当时，判断两个Map是否相等，
        * 相等则记录索引值
      * 临时Map将开头单词的次数-1，
      * 截取的单词数同理-1；
      * 将判断符合条件的索引值 k 同理加一个单词长度
```java
public List<Integer> findSubstring(String s, String[] words) {
    List<Integer> list = new ArrayList<>();
    int n = s.length(), m = words.length, w = words[0].length(),t = m * w;
    if (n < t) return list;

    Map<String,Integer> map = Arrays.stream(words).collect(Collectors.toMap(i->i, i->1, Integer::sum));
    for (int i = 0; i < w; i++) {
        Map<String,Integer> temp = new HashMap<>();
        int count = 0;
        for (int j = i,k=i; j + w<= n; j += w) {
            String word = s.substring(j, j + w);
            temp.put(word,temp.getOrDefault(word,0)+1);
            count++;

            if (count == m) { // 每次循环都判断是否满足条件
                if (map.equals(temp)) list.add(k); // 满足条件，添加索引值
                temp.computeIfPresent(s.substring(k,k+w), (a,b) -> b > 1 ? b - 1 : null);
                count--; // 移除第一个索引值，count减1
                k =k+w;
            }
        }
    }
    return list;
}
```
* 总结：
  * 以Map存储字符串及出现的次数
  * 每次单词组内字符串验证一批次之后，移除第一个字符串次数，重新遍历
  * 每次单词遍历以一个单词长为步长匹配单词

# 31. 下一个排列
    A permutation of an array of integers is an arrangement of its members into a sequence or linear order.

    Given an array of integers nums, find the next permutation of nums.

    The next permutation of an array of integers is the next lexicographically greater permutation of its integer. More formally, if all the permutations of the array are sorted in one container according to their lexicographical order, then the next permutation of that array is the permutation that follows it in the sorted container. If such arrangement is not possible, the array must be rearranged as the lowest possible order (i.e., sorted in ascending order).

* For example, for arr = [1,2,3], the following are all the permutations of arr: [1,2,3], [1,3,2], [2, 1, 3], [2, 3, 1], [3,1,2], [3,2,1].
* For example, the next permutation of arr = [1,2,3] is [1,3,2].
* Similarly, the next permutation of arr = [2,3,1] is [3,1,2].
* While the next permutation of arr = [3,2,1] is [1,2,3] because [3,2,1] does not have a lexicographical larger rearrangement.

约束：
* `1 <= nums.length <= 100`
* `0 <= nums[i] <= 100`

思路：
* 从后向前遍历，找到前者比后者大的索引
* 不存在时，证明为最大值，进行反转(即最小排序)，结束
* 存在时，重新从后向向前到刚才的索引处遍历，找到比索引对应大的值，进行交换
* 对索引+1到结尾进行排序(反转，因必定是最大排序，故转最小值排序)
* 因使用了第三方Arrays对数据进行反转排序，存在额外操作，导致速度相对较慢
```java
public void nextPermutation(int[] nums) {
    if(nums.length <2) return;
    int i = nums.length-2 ;
    while(i>=0 && nums[i]>=nums[i+1]){ // 过滤前者比后者大的位置
        i--;
    }
    if(i<0){ // 即最大值排序。相对应最小排序
        Arrays.sort(nums);
        return; // 结束
    }
    // 存在前一个比后一个小的现象，nums[i] < nums[i+1]
    int j = nums.length-1;
    while(i<j){
        if (nums[j] > nums[i]) {
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
            break;
        }
        j--;
    }
    // 再对 i+1 到结尾进行排序
    int[] tmps = Arrays.copyOfRange(nums,i+1,nums.length);
    Arrays.sort(tmps);
    i++;
    for (int k = 0; k < tmps.length; k++) {
        nums[i+k] = tmps[k];
    }
}
```
优化版：
* 提供自定义交换和反转操作，比Arrays操作快
```java
public void nextPermutation(int[] nums){
    if(nums.length <2) return;
    int i = nums.length-2 ;
    while(i>=0 && nums[i]>=nums[i+1]) i--; // 过滤前者比后者大的位置
    if(i<0){ // 即最大值排序。相对应最小排序
        reverse(nums, 0);
        return; // 结束
    }
    // 存在前一个比后一个小的现象，nums[i] < nums[i+1]
    for(int j = nums.length - 1;j>i;j--){
        if (nums[j] > nums[i]) {
            swap(nums,i,j);
            break;
        }
    }
    // 再对 i+1 到结尾进行排序
    reverse(nums, i+1);
}

public void swap(int [] nums ,int i,int j){
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
public void reverse(int [] nums ,int start){
    int i=start,j=nums.length-1;
    while(i<j){
        swap(nums,i,j);
        i++;
        j--;
    }
}
```

# 32 最长有效圆括号
    Given a string containing just the characters '(' and ')', return the length of the longest valid (well-formed) parentheses substring.

* 约束：
  * `0 <= s.length <= 3 * 10^4`
  * `s[i] is '(', or ')'.`
* 思路：
  * 使用栈，遇到左括号，入栈，遇到右括号，出栈，当栈为空时，记录当前长度，当栈不为空时，清空栈，重新开始
  * 遍历完成后，再次记录当前长度，返回最大值
```java
public int longestValidParentheses(String s) {
    Stack<Integer> stack = new Stack<>();
    stack.push(-1);
    int max_len = 0,n=s.length();
    for (int i = 0; i < n; i++) {
        if (s.charAt(i) == '(') {
            stack.push(i);
        } else {
            stack.pop();
            if (stack.isEmpty()) {
                stack.push(i);
            } else {
                max_len = Math.max(max_len, i - stack.peek());
            }
        }
    }

    return max_len;        
}
```

# 33 搜索旋转排序数组
    There is an integer array nums sorted in ascending order (with distinct values).

    Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].

    Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.

    You must write an algorithm with O(log n) runtime complexity.

* 约束：
  * `1 <= nums.length <= 5000`
  * `-10^4 <= nums[i] <= 10^4`
  * All values of nums are unique.
  * `nums` is an ascending array that is possibly rotated.
  * `-10^4 <= target <= 10^4`
* 思路：
  * 使用二分查找，找到中间值，判断中间值是否为目标值，若不是，则判断中间值与目标值的大小，从而确定目标值在左半部分还是右半部分，然后递归查找
* 个人版：
  * 先定位 旋转位点k，找到最大值索引，在根据target范围查找
  * 优化：可边定位边匹配
```java
public int search(int[] nums, int target) {
    if(nums.length ==1){
        if(nums[0] == target) return 0;
        else return -1;
    }
    int maxIndex = nums.length-1;
    if (nums[maxIndex]<nums[0]) { // 被旋转了
        maxIndex--;
        while (maxIndex>0) {
            if (nums[maxIndex]>nums[maxIndex+1]) {
                break;
            }
            maxIndex--;
        }
    } // else 未被旋转 maxIndex = nums.length-1;
    System.out.println(maxIndex);
    if (nums[0]<=target) {
        while (maxIndex>-1) {
            if (nums[maxIndex] == target) {
                return maxIndex;
            }
            maxIndex--;
        }
    }else{
        maxIndex++;
        while (maxIndex<nums.length) {
            if (nums[maxIndex] == target) {
                return maxIndex;
            }
            maxIndex++;
        }
    }
    return -1;
}
```
* 优化版：
  * 采用二分法，每一边存在某一方必需两个条件都满足时才能处理，否则就是另一边进行处理
```java
public int search(int[] nums,int target){
    int low = 0,high = nums.length-1;
    while(low<=high){
        int mid = (high-low)/2;
        if(nums[mid]==target) return mid;
        if (nums[low]<=nums[mid]) {
            if (nums[low]<=target && nums[mid]>target) {
                high= mid-1;
            }else{
                low = mid +1;
            }
        }else{
            if (nums[mid]<target && target<=nums[high]) {
                low = mid+1;
            }else{
                high = mid-1;
            }
        }
    }
    return -1;
}
```


# 34 在排序数组中查找元素的第一个和最后一个位置
    Given an array of integers nums sorted in non-decreasing order, find the starting and ending position of a given target value.

    If target is not found in the array, return [-1, -1].

    You must write an algorithm with O(log n) runtime complexity.

* 约束：
  * `0 <= nums.length <= 10^5`
  * `-10^9 <= nums[i] <= 10^9`
  * nums is a non-decreasing array.
  * `-10^9 <= target <= 10^9`
* 思路
* 初版
```java
public int[] searchRange(int[] nums, int target) {
    int[] result = {-1,-1};
    int low=0,high = nums.length-1;
    while(low<=high && (result[0]==-1 || result[1]==-1)){
        if(nums[low] == target ){
            result[0] = low;
        }else{
            low++;
        }
        if(nums[high] == target){
            result[1] = high;
        }else{
            high--;
        }
    }
    return result;
}
```
# 35 在排序数组中查找插入位置(Easy)
    Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return the index where it would be if it were inserted in order.

    You must write an algorithm with O(log n) runtime complexity.

* 约束：
  * `1 <= nums.length <= 10^4`
  * `-10^4 <= nums[i] <= 10^4`
  * `nums` is sorted in non-decreasing order.
  * `-10^4 <= target <= 10^4`
* 思路：
  * 匹配大于等于项，对应索引即位置
  * 若未匹配，则结尾插入，索引对应数组长度
* 代码
```java
public int searchInsert(int[] nums, int target) {
    for(int i =0;i<nums.length;i++){
        if(nums[i]>=target){
            return i;
        }
    }
    return nums.length;
}
```
# 36 有效的数独(中)
    Determine if a 9 x 9 Sudoku board is valid. Only the filled cells need to be validated according to the following rules:
    Each row must contain the digits 1-9 without repetition.
    Each column must contain the digits 1-9 without repetition.
    Each of the nine 3 x 3 sub-boxes of the grid must contain the digits 1-9 without repetition.
    Note:
    A Sudoku board (partially filled) could be valid but is not necessarily solvable.
    Only the filled cells need to be validated according to the mentioned rules.
* 约束：
  * `board.length == 9`
  * `board[i].length == 9`
  * `board[i][j] is a digit 1-9 or '.'.`
* 思路：重点关注**九宫格**规则
  * 遍历，判断行、列、九宫格是否满足条件
  * 九宫格规则：
    * 行：`board[i][j]`
    * 列：`board[j][i]`
    * **九宫格**：`board[3*(i/3)+j/3][3*(i%3)+j%3]`
* 代码
```java
public boolean isValidSudoku(char[][] board) {
    int[] rows,cols,squares;
    for(int i = 0;i<9;i++){
        rows = new int[9];cols=new int[9];squares = new int[9];
        for(int j=0;j<9; j++){
            // 同行校验 [i][j]
            if(!isValidArrays(board[i][j], rows)) return false;
            // 同列校验 [j][i]
            if(!isValidArrays(board[j][i], cols)) return false;
            // 3x3格校验 [3*(i/3)+j/3][3*(i%3)+j%3]
            if(!isValidArrays(board[3*(i/3)+j/3][3*(i%3)+j%3], squares)) return false;
        }
    }
    return true;
}

public boolean isValidArrays(char ch,int[] nums){
    if (ch == '.')  return true;
    if (nums[ch-49] == ch)  return false;
    nums[ch-49] = ch;
    return true;
}
```

# 37 解数独(难)
    Write a program to solve a Sudoku puzzle by filling the empty cells.
    A sudoku solution must satisfy all of the following rules:
    Each of the digits 1-9 must occur exactly once in each row.
    Each of the digits 1-9 must occur exactly once in each column.
    Each of the digits 1-9 must occur exactly once in each of the 9 3x3 sub-boxes of the grid.
    The '.' character indicates empty cells.

* 约束：
  * `board.length == 9`
  * `board[i].length == 9`
  * `board[i][j] is a digit or '.'.`
  * `It is guaranteed that the input board has only one solution.`
* 代码（初版）
  * 思路：当某个位置不存在时，从1-9一次填充，
    * 填充一次检查是否有效
    * 有效，继续下一个位置
    * 无效，则换下一个填充
  * 问题：重复无效填充，导致时间耗时较高
```java
public void solveSudoku(char[][] board) {
    solve(board);
}
public boolean solve(char[][] board) {
    char[] nums = {'1','2','3','4','5','6','7','8','9'};
    for(int i = 0;i<9;i++){
        for(int j = 0;j<9;j++){
            if(board[i][j] != '.') continue;
            for(int k = 0;k<9;k++){
                board[i][j] = nums[k];
                if(isValidSudoku(board) && // 本次填充是否有效
                solve(board)) { // 下次填充是否有效，无效，则同步本次也无效，需要继续更换本次填充
                    return true;
                }else{
                    board[i][j] = '.' ;
                }
                
            }
            if (board[i][j]  == '.') {
                // 代表没有可选值了
                return false;
            }
        }
    }
    return true;
}
public boolean isValidSudoku(char[][] board) {
    int[] rows,cols,squares;
    for(int i = 0;i<9;i++){
        rows = new int[9];cols=new int[9];squares = new int[9];
        for(int j=0;j<9; j++){
            // 同行校验 [i][j]
            if(!isValidArrays(board[i][j], rows)) return false;
            // 同列校验 [j][i]
            if(!isValidArrays(board[j][i], cols)) return false;
            // 3x3格校验 [3*(i/3)+j/3][3*(i%3)+j%3]
            if(!isValidArrays(board[3*(i/3)+j/3][3*(i%3)+j%3], squares)) return false;
        }
    }
    return true;
}

public boolean isValidArrays(char ch,int[] nums){
    if (ch == '.')  return true;
    if (nums[ch-49] == ch)  return false;
    nums[ch-49] = ch;
    return true;
}
```

* 改进版：
  * 每次填充过滤行、列、格已存在的，避免重复填充无效值
```java
public void solveSudoku(char[][] board) {
    char[] nums = {'1','2','3','4','5','6','7','8','9'};
    solveSudoku(board,nums,0,0);
}
public boolean solveSudoku(char[][] board,char[] nums,int i,int j){
    if (j == 9) { // 当列索引j等于9时，代表列溢出，换到下一行开头
        i++;j = 0;
    }
    if (i == 9) return true; // 填充完毕
    if (board[i][j]!='.') return solveSudoku(board, nums, i, j+1);
    Set<String> exist = new HashSet<>();  // 统计其它位置已经存在的值
    for (int k = 0; k < 9; k++) {
        if (board[i][k] != '.') exist.add(String.valueOf(board[i][k])); // 同行
        if (board[k][j]!='.') exist.add(String.valueOf(board[k][j])); // 同列
        
        int tmpi = 3*(i/3)+(k)/3,tmpj = (k%3) + 3*(j/3); // 同格范围计算
        if (board[tmpi][tmpj] != '.') exist.add(String.valueOf(board[tmpi][tmpj])); // 因按行顺序填充，故九宫格的范围应以列为主
    }
    for(int k = 0;k<9;k++){
        if (exist.contains(String.valueOf(nums[k]))){
            continue;
        }
        board[i][j] = nums[k];
        if(solveSudoku(board,nums,i,j + 1)) {
            return true; // 只要没有false返回即代表一致填充到最后，直至成功
        }else{
            board[i][j] = '.' ;
        }
    }
    return false; // 默认返回false，避免未考虑的异常情况
}
```
* 官方(升级版)：
  * 按位判断是否互斥
```java
// TODO 待理解后补充
```

# 38 报数序列(中)
    The count-and-say sequence is a sequence of digit strings defined by the recursive formula:
    
    Run-length encoding (RLE) is a string compression method that works by replacing consecutive identical characters (repeated 2 or more times) with the concatenation of the character and the number marking the count of the characters (length of the run). For example, to compress the string "3322251" we replace "33" with "23", replace "222" with "32", replace "5" with "15" and replace "1" with "11". Thus the compressed string becomes "23321511".
    
    Given a positive integer n, return the nth element of the count-and-say sequence.

* 约束
  * `1 <= n <= 30`
* 思路
  * 使用递归先获取前一个的结果，再通过结果进行压缩
```java
public String countAndSay(int n) {
    if(n == 1) return "1";
    if(n == 2) return "11";
    String content = countAndSay(n-1);
    char pre = '0';int count = 0;
    StringBuilder sb = new StringBuilder();
    for(int i = 0;i<content.length();i++){
        if(content.charAt(i) != pre){
            if(pre != '0') sb.append(count).append(pre);
            count = 1;pre = content.charAt(i);
        }else{
            count +=1;
        }
    }
    if (count>0) {
        sb.append(count).append(pre);
    }
    return sb.toString();
}
```
# 39 组合总和(中)
    Given an array of distinct integers candidates and a target integer target, return a list of all unique combinations of candidates where the chosen numbers sum to target. You may return the combinations in any order.

    The same number may be chosen from candidates an unlimited number of times. Two combinations are unique if the frequency of at each candidate number is different.

    The test cases are generated such that the number of unique combinations that sum to target is less than 150 combinations for the given input.

* 约束
  * `1 <= candidates.length <= 30`
  * `1 <= candidates[i] <= 200`
  * `candidates` contains distinct elements.
  * `1 <= target <= 500`

* 思路
  * 对数组排序
  * 当当前值大于目标值，则终止
  * 使用回溯算法，将余数当作新目标值，递归获取结果
  * 对结果进行去重
```java
public List<List<Integer>> combinationSum(int[] candidates, int target) {
    Arrays.sort(candidates); // 对candidates排序，便于递归逻辑中终止
    Set<Integer> set = Arrays.stream(candidates).boxed().collect(Collectors.toSet());
    Map<Integer,List<List<Integer>>> map = new HashMap<>();
    return combinationSum(candidates,set,map,target);
}

public List<List<Integer>> combinationSum(int[] candidates,Set<Integer> set,Map<Integer,List<List<Integer>>> map, int target) {
    Set<List<Integer>> result = new HashSet<>(); // 结果去重，避免重复
    for (int i = 0; i < candidates.length; i++) {
        int cur = candidates[i];
        if (cur>target) { // 因以排序，所以后续的数肯定大于target
            break;
        }
        int quote = target/cur,remain = target % cur;
        while (quote > 0) {
            if (remain == 0) { // 代表整除
                List<Integer> tmp = new ArrayList<>();
                for (int j = 0; j < quote; j++) tmp.add(cur);
                result.add(tmp);
            }else { // 代表非整除,对余数进行递归
                List<List<Integer>> remainResult;
                if (map.containsKey(remain)) { // map避免同值重复递归
                    remainResult = map.get(remain);
                }else{
                    remainResult = combinationSum(candidates,set,map,remain); // 结果一定不为空，因result已经初始化
                    map.put(remain,remainResult);
                }
                for (List<Integer> list : remainResult) {
                    List<Integer> tmp = new ArrayList<>(list);
                    for (int j = 0; j < quote; j++) tmp.add(cur);
                    tmp.sort((t1,t2)->t1-t2);
                    result.add(tmp);
                }
            }
            // 对商减一，对余数追加，进行下一轮循环
            quote--;remain+=cur;
        }
    }
    return result.stream().collect(Collectors.toList());
}
```
* **改进版**(官方)
  * 直接使用目标值减去当前值，做递归循环，直到
    * 目标值为0，代表可以被整除
    * 起始索引不小于数组长度，代表遍历结束
```java
public List<List<Integer>> combinationSum(int[] candidates, int target) {
    List<List<Integer>> result = new ArrayList<>();
    Arrays.sort(candidates); // 对candidates排序，便于递归逻辑中终止
    combinationSum(candidates,result,new ArrayList<>(),0,target);
    return result;
}

public void combinationSum(int[] candidates,List<List<Integer>> result,List<Integer> curr,int idx, int target) {
    if (target == 0) { // 当余数为0，代表可以被整除，返回上一层
        result.add(new ArrayList<>(curr)); // 代表数组有效，可将数组加入结果集中
        return;
    }
    for (int i = idx; i < candidates.length; i++) { // 当起始位置溢出时，代表已经遍历完，返回上一层
        int cur = candidates[i];
        if (cur>target) break; // 因以排序，所以后续的数肯定大于target
        curr.add(cur); // 将当前值加入当前数组中
        combinationSum(candidates,result,curr,i,target-cur);// 递归余数
        curr.remove(curr.size()-1); // 移除当前值,继续下一轮循环
    }
}
```
* **官方最优版**
  * 因为是返回一个抽象数组，初始化时不会执行逻辑
  * 仅在对结果内容获取时，才会执行逻辑，故而执行逻辑不会耗时(**耗时逻辑在结果调用时**)
```java
public List<List<Integer>> combinationSum(int[] candidates, int target) {
    return new AbstractList<List<Integer>>() {
        List<List<Integer>> ret;
        ArrayList<Integer> curr;
        void init() {
            if (ret != null) {
                return;
            }
            ret = new LinkedList<>();
            curr = new ArrayList<>();
            Arrays.sort(candidates);
            rec(candidates, 0, target);
        }
        @Override
        public List<Integer> get(int index) {
            init();
            return ret.get(index);
        }
        @Override
        public int size() {
            init();
            return ret.size();
        }
        void rec(int[] candidates, int idx, int target) {
            if (target == 0) {
                ret.add(new ArrayList<>(curr));
                return;
            }
            for (int i = idx; i < candidates.length; i++) {
                int num = candidates[i];
                if (num > target) {
                    break;
                }
                curr.add(num);
                rec(candidates, i, target - num);
                curr.remove(curr.size() - 1);
            }
        }
    };
    }
```

# 40 组合总和 II(中)
    Given a collection of candidate numbers (candidates) and a target number (target), find all unique combinations in candidates where the candidate numbers sum to target. 
    
    Each number in candidates may only be used once in the combination.

    Note: The solution set must not contain duplicate combinations.

* 约束
  * `1 <= candidates.length <= 100`
  * `1 <= candidates[i] <= 50`
  * `1 <= target <= 30`

* 思路
```java

```
# 41 缺失的第一个正数(难)
    Given an unsorted integer array nums. Return the smallest positive integer that is not present in nums.

    You must implement an algorithm that runs in O(n) time and uses O(1) auxiliary space.

* 约束
  * `1 <= nums.length <= 5 * 10^5`
  * `-2^31 <= nums[i] <= 2^31 - 1`
* 思路
  * 先排序，后处理
  * 非正整数统统过滤
  * 从1开始对比，向等继续+1对比，
    * 至大于对比数或至数组结束终止
```java
public int firstMissingPositive(int[] nums) {
    Arrays.sort(nums);
    int pos = 1;
    for(int i=0;i<nums.length;i++){
        if(nums[i]<=0) continue;
        if(nums[i]>pos) break;
        if(nums[i] == pos) pos++;
    }

    return pos;
}
```
* 官方最优解
  * 过滤非正整数
  * 理论情况应为连续正整数，将小于等于数组长度count的值置为负数，最后找到第一个正整数，返回其下标+1
  * 注意：不能直接将值置为负数，因为负数可能已经置为负数，所以需要取绝对值
```java
public int firstMissingPositive(int[] nums) {
    int size = nums.length;
    int count= 0;
    for(int i=0;i<size;i++){ // 过滤非正整数，有效正整数个数为count
        if(nums[i]>0){
            nums[count] = nums[i];
            count++;
        }
    }
    for(int i=0;i<count;i++){ // 将正整数小于count的值置为负数，理论情况下，count个数都转为负
        int val = Math.abs(nums[i]);
        if(val - 1 < count && nums[val - 1]>0){ 
            nums[val - 1] = -nums[val-1];
        }
    }
    for(int i=0;i<count;i++){
        if(nums[i]>0){ // 找到第一个正整数，返回其下标+1
            return i+1;
        }
    }
    return count + 1; 
}
```

# 58. Length of Last Word(简单)
    Given a string s consists of some words separated by spaces, return the length of the last word in the string. If the last word does not exist, return 0.

    A word is a maximal substring consisting of non-space characters only.

* 约束
  * `1 <= s.length <= 10^4`
```java
public int lengthOfLastWord(String s) {
    int end = s.length()-1;
    while (end >= 0 && s.charAt(end) == ' ') end--;
    if (end < 0) return 0;
    return end - s.lastIndexOf(' ', end);
}
```
# 66. Plus One(简单)
    You are given a large integer represented as an integer array digits, where each digits[i] is the ith digit of the integer. The digits are ordered from most significant to least significant in left-to-right order. The large integer does not contain any leading 0's.

    Increment the large integer by one and return the resulting array of digits.

* 约束
  * `1 <= digits.length <= 100`
  * `0 <= digits[i] <= 9`
  * `digits does not contain any leading 0's`
* **思路**
  * 从后向前遍历，结果大于10，前进1位，当前位取余，
  * 若首位结果大于10，扩展新数组
* **改进**
  * 由于值为`0-9`，只有为`9`时才需要进位，故可简化为非9时直接+1退出
  * 等于9时，+1最终当前为结果为0
  * 由于**int默认值为0**，故**新数组扩展只需首位为1**即可
```java
// 初版
public int[] plusOne(int[] digits) {
    int tmp,n=digits.length,i=n-1;
    int[] result = digits;
    do {
        tmp = digits[i] + 1;
        digits[i--] = tmp % 10;
    } while (tmp / 10 == 1 && i >=0);
    if (i < 0 && tmp / 10 == 1) {
        // 扩展新数组
        result = new int[n + 1];
        result[0] = 1;
        for (int j = 0; j < n; j++)
            result[j + 1] = digits[j];
    }
    return result;
}

// 参考官方改进
public int[] plusOne(int[] digits) {
    int n = digits.length,i=n-1;
    do{
        if(digits[i]<9){
            digits[i]++;
            return digits; 
        }
        digits[i--] = 0;
    }while(i>=0);
    // 代表第一位结果=10，后续位置都为0，由于int默认0，故只需新增int[]首位1即可
    int[] ans = new int[digits.length+1]; 
    ans[0] = 1;
    return ans;
}
```
# 67. Add Binary(简单)
    Given two binary strings a and b, return their sum as a binary string.

* 约束
  * `1 <= a.length, b.length <= 10^4`
  * `a` and `b` consist only of '0' or '1' characters.
  * Each string does not contain leading zeros except for the zero itself.

* **思路**
  * **从后向前遍历**
  * **结果大于2，前进1位，当前位取余**
  * **若遍历完仍需进1，则新增首位为1**
  * `char`和`int`计算会被提权至`int`
  * 由于`char`中`0`转`int`非`0(48)`，故两个`char`相加相当于多加一次，需要减去一个
* **改进**
  * 以`char[]`接收结果，并定义最大可能长度的数组
  * 将各位运算都转为`int`类型计算
  * 通过**位并运算**计算当前位结果`(char)((sum & 1) + '0') or sum % 2`

```java
// 初版，通过StringBuilder.insert(offset, char)方法，从后向前插入
public static String addBinary(String a, String b) {
    StringBuilder sb = new StringBuilder();
    int i = a.length()-1,j= b.length()-1;
    char cur;int tmp = 0; // tmp 最大是1
    while(i>=0 || j>=0) {
        if(i<0) cur = b.charAt(j--); 
        else if(j<0) cur = a.charAt(i--);
        // 在ascii中，'0'的值为48, 双位相加相当于多加一次'0'，故需要减去一个
        else cur = (char)(a.charAt(i--) + b.charAt(j--) - '0');
        cur = (char)(cur + tmp);
        if (cur == '3') { // cur临时最大为3，即双位都为1，并且低位上进1
            cur = '1';
            tmp = 1;
        }else if(cur == '2') {
            cur = '0';
            tmp = 1;
        }else { //不足2时，cur不变，并且不足进1，tmp重置为0
            tmp = 0;
        }
        sb.insert(0,cur);

    }
    if (tmp == 1) sb.insert(0,'1');
    String result = sb.toString();
    return result;
}

// 改进版
public String addBinary(String a, String b) {
    if(a.length()<b.length()) return addBinary(b, a); // 统一 a为最长的字符串
    int i = a.length()-1, j = b.length()-1,carry = 0;
    char[] res = new char[a.length() + 1]; // 结果最大可能比最长长1位
    int k = res.length-1;
    while(i>=0){
        int digist = a.charAt(i--) - '0';
        int digistB = j>=0 ? b.charAt(j--) - '0' : 0;
        int sum = digist + digistB + carry;
        carry = sum>>1;
        res[k--] = (char)((sum & 1) + '0'); // == sum % 2
    }
    if (carry == 1) {
        res[k--] = '1';
    }
    return new String(res,k+1,res.length-k-1);
}
```
# 69. Sqrt(x)(简单)
    Given a non-negative integer x, return the square root of x rounded down to the nearest integer. The returned integer should be non-negative as well.

    You must not use any built-in exponent function or operator.

* 约束
  * `0 <= x <= 2^31 - 1`
* **思路**
  * 4之前的数字，直接返回
  * 从中间值向下匹配：超过4的值，一般不会有比中间值大的开方值
  * **注意数据长度限制**，若为最大值Integer，则中间值会溢出
* 改进： 减少循环次数
  * **二分查找**
```java
// 思路： N*2 == num；最次一半内，一半后肯定没有符合条件的，开放要从2开始
public static int sqrt2(int num){
    if(num==0) return 0;
    if(num<4) return 1;
    int hk = num/2;
    long tmp=0;
    while(hk>=2){ // 如何跳步 缩短步骤
        tmp =(long) hk * hk;
        if(tmp == num){return hk;}
        if(tmp<num){break;}
        hk--;
    }
    return hk; // 向下取整
}

// 改进版(二分查找)
public static int sqrt(int num){
    if(num == 0) return 0;
    if(num < 4) return 1;
    return sqrt(num,2,num/2);
}

public static int sqrt(int num,int lo,int hi){
    if(hi<=lo || hi-1==lo) return lo;
    int mid =lo + ( hi -lo) /2;
    long tmp = (long)mid *mid ;
    if(tmp<num) return sqrt(num,mid,hi); 
    if(tmp>num) return sqrt(num,lo,mid);
    return mid;
}

// 官方改进版(二分查找)，不使用递归，从两侧向中间查找
public int mySqrt(int x) {
	if(x==0||x==1) return x;
	int start = 1,end=x,mid=-1;
	while(start<=end){
		mid = start + (end-start)/2;
		if((long)mid*mid>(long)x) end=mid-1;
		else if(mid*mid==x) return mid;
		else start = mid+1;
	}

	return end;
}
```

# 70. Climbing Stairs(简单)
    You are climbing a staircase. It takes n steps to reach the top.

    Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?

* 约束
  * `1 <= n <= 45`
* **思路**： 从第三步开始，每一步都是前两步之和，即[**斐波那契数列**](/common/algorithm/algorithm.md#斐波那契数列)
  * **动态规划**: 大于2的使用**递归**方式，2之前的使用固定值
* **改进**
  * **保存前两个值**，每次计算后更新
```java
public static int climbStairs(int n ){
    if(n == 0 || n == 1) return 1;
    return climbStairs(n-1) + climbStairs(n-2); // 递归超时了
}

public static int climbStairs2(int n ){
    if(n <4) return n;
    int p1 = 2;
    int p2 = 3;
    int cur =0;
    for (int i = 4; i < n +1; i++) { //=> for (=> int i = 3; i < n; i++)
        cur = p1 + p2;
        p1 = p2;
        p2 = cur;
    }
    return cur;
}
```

# 83. Remove Duplicates from Sorted List(简单)
    Given the head of a sorted linked list, delete all duplicates such that each element appears only once. Return the linked list sorted as well.

* 约束
  * The number of nodes in the list is in the range [0, 300].
  * -100 <= Node.val <= 100
  * The list is guaranteed to be **sorted** in ascending order.
* **思路**
  * 通过当前和next对比，
    * 等值，则将`next.next`指向`next.next.next`
    * 否则将`next`设置为`next.next`
* **反思**
  * 开始的以为给的是随意的链表，需要对去重加排序呢
```java
public static  ListNode deleteDuplicates(ListNode head){
    if(head == null) return head;
    ListNode next = head;
    while(next!=null && next.next!=null){
        if(next.val == next.next.val){
            next.next = next.next.next;
        }else{
            next = next.next;
        }
    }
    return head;
}

// 以下是随机链表：去重+排序
public static  LineNode deleteDuplicates(LineNode head){
    if(head == null) return head; // 头节点为空，则直接返回
    // 定义first头节点，tail已排序的尾节点，next已排序尾节点的下一个节点
    LineNode first = new LineNode(-101,head),tail = first.next,next;
    
    while((next=tail.next)!=null){
        LineNode pre = first,cur = pre .next; // 定义前一个节点pre初始为first，当前比较节点，初始为first的下一个
        tail.next = next.next; // 无论next定位与否，都先将next.next交给tail.next

        while(true){  // pre<cur<tail !=null,next!=null
            if(cur.val <next.val){ //  cur.val<next.val	
                if (cur != tail) { // cur != tail，则pre绑定cur，cur转cur.next 继续
                    pre = cur;
                    cur = cur.next;
                    continue;
                }
                tail.next = next; // 还原，将next还原会tail.next 并将next设置为tail，最后退出
                tail = next;
                // break;
            }
            else if(cur.val>next.val){ // cur.val>next.val，cur大，则由pre绑定next，next绑定cur，退出
                next.next = cur;
                pre.next = next;
                // break;
            }
            // else (cur.val == next.val) break; // 等值忽略退出内循环
            break;
        }
    }
    return first.next;
}

// 将双层循环变为两个外层循环，利用map存储每个值对应的节点
public static ListNode deleteDuplicates2(ListNode head){
    if(head == null) return head;
    Map<Integer,ListNode> maps = new HashMap<>();
    ListNode first = new ListNode(-101,head),cur = first;
    while ((cur=cur.next)!=null) maps.put(cur.val,cur);
    first.next = null;cur = first;
    for(Integer key:maps.keySet().stream().sorted().toArray(Integer[]::new)){
        cur.next = maps.get(key);
        cur = cur.next;
    }
    cur.next = null; // 避免最大值有next
    return first.next;
}
```

# 88. Merge Sorted Array(简单)
    You are given two integer arrays nums1 and nums2, sorted in **non-decreasing order**, and two integers m and n, representing the number of elements in nums1 and nums2 respectively.

    Merge nums1 and nums2 into a single array sorted in **non-decreasing order**.
    The final sorted array should not be returned by the function, but instead be **stored inside the array nums1**. To accommodate this, nums1 has a length of m + n, where the first m elements denote the elements that should be merged, and the last n elements are set to 0 and should be ignored. nums2 has a length of n.

* **约束**
  * `nums1.length == m + n`
  * `nums2.length == n`
  * `0 <= m, n <= 200`
  * `1 <= m + n <= 200`
  * `-10^9 <= nums1[i], nums2[j] <= 10^9`
* **思路**：归并排序的归并函数
```java
public void merge(int[] nums1, int m, int[] nums2, int n) { // 归并排序的合并方法
    if(n == 0) return;
    int[] tmp = new int[m]; 
    for(int i = 0;i<m;i++){
        tmp[i] = nums1[i];
    }
    int i = 0,j=0;
    for(int k = 0;k<nums1.length;k++){
        if(i==m) nums1[k] = nums2[j++];
        else if(j==n) nums1[k] = tmp[i++];
        else if(nums2[j]-tmp[i]<0) nums1[k] = nums2[j++];
        else nums1[k] = tmp[i++];
    }
}
```

# 94. Binary Tree Inorder Traversal(简单)
    Given the root of a binary tree, return the **inorder** traversal of its nodes' values.

* **约束**
  * The number of nodes in the tree is in the range `[0, 100]`.
  * `-100 <= Node.val <= 100`
* **思路**: 深度优先(左中右)
  * **递归**
```java
public List<Integer> inorderTraversal(TreeNode root) {
    List<Integer> list = new ArrayList<>();
    if(root == null ) return list;
    if(root.left !=null){
        list.addAll(inorderTraversal(root.left));
    }
    list.add(root.val);
    if(root.right !=null){
        list.addAll(inorderTraversal(root.right));
    }
    return list;
}
```

# 100. Same Tree(简单)
    Given the roots of two binary trees `p` and `q`, write a function to check if they are the same or not.

    Two binary trees are considered the same if they are structurally identical, and the nodes have the same value.

* **约束**
  * The number of nodes in both trees is in the range `[0, 100]`.
  * `-104 <= Node.val <= 104`
  * `p` and `q` are both binary trees.
* **思路**: 递归
```java
public boolean isSameTree(TreeNode p, TreeNode q) {
    if(p == null && q == null) return true;
    if ((p==null && q !=null)||(p!=null && q ==null)) return false;
    if(p.val != q.val) return false;
    return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
}
```

# 101. Symmetric Tree(简单)
    Given the `root` of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).

* **约束**
  * The number of nodes in the tree is in the range `[1, 1000]`.
  * `-100 <= Node.val <= 100`
* **思路**: 递归,分治法
```java
public boolean isSymmetric(TreeNode root) {
    if(root==null) return true;
    return isSymmetric(root.left,root.right);
}
public boolean isSymmetric(TreeNode left,TreeNode right) {
    if(left==null && right==null) return true;
    if(left==null || right==null) return false;
    if(left.val!=right.val) return false;
    return isSymmetric(left.left,right.right) && isSymmetric(left.right,right.left);
}
```

# 104. Maximum Depth of Binary Tree(简单)
    Given the root of a binary tree, return its maximum depth.

    A binary tree's maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.

* **约束**
  * The number of nodes in the tree is in the range `[0, 104]`.
  * `-100 <= Node.val <= 100`
```java
public int maxDepth(TreeNode root) {
    if(root == null) return 0;
    if(root.left == null && root.right == null) return 1;
    return Math.max(maxDepth(root.left), maxDepth(root.right)) +1;
}
```

# 108. Convert Sorted Array to Binary Search Tree(简单)
    Given an integer array `nums` where the elements are sorted in **non-decreasing order**, convert `it` into a **height-balanced** binary search tree.

    A **height-balanced** binary tree is a binary tree in which the depth of the two subtrees of every node **never differs by more than one**.

* **约束**
  * `1 <= nums.length <= 10^4`
  * `-10^4 <= nums[i] <= 10^4`
* **思路**: 递归,分治
  * 对半分,左半部分为左子树,右半部分为右子树
  * 即中间节点为父节点`lo+(hi-lo)/2`
  * **注意**：要判断`lo,hi`是否越界和有效
```java
public TreeNode sortedArrayToBST(int[] nums) {
    if(nums.length==0) return null;
    if(nums.length==1) return new TreeNode(nums[0]);
    return buildTree(nums,0,nums.length-1);
}

public TreeNode buildTree(int[] nums,int lo,int hi) {
    if(hi<lo) return null;
    int mid = lo+(hi-lo)/2;
    TreeNode root = new TreeNode(nums[mid]);
    root.left = buildTree(nums,lo,mid-1);
    root.right = buildTree(nums,mid+1,hi);
    return root;
}
```

# 110. Balanced Binary Tree(简单)
    Given a binary tree, determine if it is height-balanced.

    A binary tree is **height-balanced** if the depth of the two subtrees of every node never differs by more than one.

* **约束**
  * The number of nodes in the tree is in the range `[0, 5000]`.
  * `-10^4 <= Node.val <= 10^4`
* **思路**：递归，分治法
  * 平衡二叉树：左右子树高度差不超过1
  * 既判断当前节点是否平衡，也要判断左右子树是否平衡
* **改进**：由于存在height重复计算问题，可以使用动态规划
  * 在计算左右子树高度时，判断左右子树是否平衡
```java
// 平衡二叉树：左右子树高度差不超过1
public boolean isBalanced(TreeNode root) {
    if(root == null) return true;
    return Math.abs(height(root.left) - height(root.right)) <= 1 && isBalanced(root.left) && isBalanced(root.right);
}
public int height(TreeNode node){
    if(node == null) return 0;
    return Math.max(height(node.left),height(node.right))+1;
}


// 改进版：使用动态规划，在计算左右子树高度时，判断左右子树是否平衡
public boolean isBalanced(TreeNode root) {
    return checkHeight(root) != -1; // -1表示不平衡
}
private int checkHeight(TreeNode node) {
    if (node == null) return 0;
    
    int leftHeight = checkHeight(node.left);
    if (leftHeight == -1) return -1; // 左子树不平衡
    
    int rightHeight = checkHeight(node.right);
    if (rightHeight == -1) return -1; // 右子树不平衡
    
    if (Math.abs(leftHeight - rightHeight) > 1) return -1; // 当前节点不平衡
    
    return Math.max(leftHeight, rightHeight) + 1; // 返回当前节点高度
}
```
# 111. Minimum Depth of Binary Tree(简单)
    Given a binary tree, find its minimum depth.

    The minimum depth is the number of nodes along the shortest path from the root node down to the nearest leaf node.

* **约束**
  * The number of nodes in the tree is in the range `[0, 10^5]`.
  * `-1000 <= Node.val <= 1000`
* **思路**：递归，分治法
  * 若节点一支为空，则以另一支结果为结果
* **改进**
  * 记忆最小值，用于叶子节点高度对比
  * 增加叶子节点判断，细化左右子树为空的情况
```java
// 叶子节点：没有左右子节点
public int minDepth(TreeNode root) {
    if(root == null) return 0;
    if(root.left == null && root.right == null) return 1;
    if(root.left == null) return minDepth(root.right)+1;
    if(root.right == null) return minDepth(root.left)+1;
    return Math.min(minDepth(root.left),minDepth(root.right))+1;
}

// 改进版：增加叶子节点判断，细化左右子树为空的情况
private int minHeight = Integer.MAX_VALUE;
public int minDepth(TreeNode root) {
    if(root == null) return 0;
    dfs(root,0);
    return minHeight;
}
public void dfs(TreeNode node,int height){
    height++;
    if (height == minHeight) 
        return;
    
    if(isLeaf(node)){
        minHeight = Math.min(minHeight,height); // 更新最小值
        return;
    }
    if (node.left != null && (node.right==null || !isLeaf(node.right))) // 确定另一支非叶子节点
        dfs(node.left,height);
    
    if(node.right!=null)
        dfs(node.right,height);
}
public boolean isLeaf(TreeNode node){
    return node.left == null && node.right == null;
}
```

# 112. Path Sum(简单)
    Given the `root` of a binary tree and an integer `targetSum`, return `true` if the tree has a root-to-leaf path such that adding up all the values along the path equals `targetSum`.

    A **leaf** is a node with no children.

* **约束**
* The number of nodes in the tree is in the range `[0, 5000]`.
  * `-1000 <= Node.val <= 1000`
  * `-1000 <= targetSum <= 1000`
* **思路**：递归,回溯法
  * **注意**：`targetSum,val`可能为负数，不要直接判断`sum>targetSum`就返回false
```java
/**
 * 1. root == null return false
 * 2. sum == targetSum and root is leaf return true
 * 3. targetSum may be negative
 * 4. val may be negative
 */
public boolean hasPathSum(TreeNode root, int targetSum) {
    return hasPathSum(root, targetSum,0);
}

public boolean hasPathSum(TreeNode root, int targetSum, int sum) {
    if(root == null) return false;
    sum += root.val;

    if(isLeaf(root)) return sum == targetSum;

    // sum <=targetSum and root is not leaf
    if(hasPathSum(root.left,targetSum,sum)) return true;
    else if(hasPathSum(root.right,targetSum,sum)) return true;

    // (root.left!=null || root.right!=null) and hashPathSum = false
    return false;
}
public boolean isLeaf(TreeNode root){
    return root.left==null && root.right==null;
}
```

# 118. Pascal's Triangle(简单,帕斯卡三角形)
    Given an integer `numRows`, return the first numRows of Pascal's triangle.

    In Pascal's triangle, each number is the sum of the two numbers directly above it as shown:

```txt
    1
   1 1
  1 2 1
 1 3 3 1
1 4 6 4 1
..........
```

* **约束**
  * `1 <= numRows <= 30`
* **思路**：动态规划
  * 了解帕斯卡三角形规律
    * 每行元素数等于行数
    * 每一行的首尾数字都是1
    * 每一行中间的数字等于上一行中相邻两个数字的和
      * 上一行同索引和前一个索引对应值和，除1，2外
      * 每行元素从中间开始对称
* **改进**：**优化空间复杂度**
  * 当前行中，初始数为1
  * 同一行中，每一个索引数值是前一个数值的倍数(`行数-行内索引值`)，除以`行内索引`
```java
public List<List<Integer>> generate(int numRows) {
    if(numRows==0) return null;
    List<List<Integer>> res = new ArrayList<>();
    List<Integer> pre=null;
    int cur = 0;
    while (cur<numRows) {
        List<Integer> curList = new ArrayList<>(cur+1);
        if(cur < 2){
            for(int i = 0;i<= cur;i++){
                curList.add(1);
            }
        }
        else {
            curList.add(1);
            for(int j = 1;j<cur;j++){
                curList.add(pre.get(j-1)+pre.get(j));
            }
            
            curList.add(1);
        }
        pre = curList;
        cur++;
        res.add(curList);
    }
    return res;
}

// 改进版：优化空间复杂度
public List<List<Integer>> generate(int numRows) {
    List<List<Integer>> res = new ArrayList<>();
    for (int i = 0; i < numRows; i++) {
        List<Integer> curList = new ArrayList<>();
        int num = 1;
        for (int j = 0; j <= i; j++) {
            if (j == 0 || j == i) {
                curList.add(1);
            } else {
                num = num*(i-j+1)/j;
                curList.add(num);
            }
        }
        res.add(curList);
    }
    return res;
}
```

# 119. Pascal's Triangle II(简单,帕斯卡三角形)
    Given an integer `rowIndex`, return the `rowIndex`-th row of the Pascal's triangle.

    In Pascal's triangle, each number is the sum of the two numbers directly above it as shown:

* **约束**
  * `0<= rowIndex <= 33`
* **思路**：[参考118题的当前行生成方法](#118-pascals-triangle简单帕斯卡三角形))
  * 注意：
    * `rowIndex`从`0`开始
    * `num`可能会超出`int`范围
```java
public List<Integer> getRow(int rowIndex) {
    List<Integer> res = new ArrayList<>();
    long num = 1;
    for (int j = 0; j <= rowIndex; j++) {
        if (j == 0 || j == rowIndex) {
            res.add(1);
        } else {
            num = (int)(num*(rowIndex-j+1)/j);
            res.add(num);
        }
    }
    return res;
}
```

# 121. Best Time to Buy and Sell Stock(简单)
    You are given an array prices where prices[i] is the price of a given stock on the ith day.

    You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.
    Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.

* **约束**
  * `1 <= prices.length <= 10^5`
  * `0 <= prices[i] <= 10^4`
* **思路**：**暴力枚举**
  * 规则
    * 先买后卖，不能是同一天
    * 负收入则不买卖
  * 算法逻辑：
    * 前一天买价，与后边所有天卖价差值取最大
    * 将前一天后移一天，重复取差值最大
    * 知道最后一天，无法取差值，终止
    * 返回最大差值
  * **循环过多，容易超时**
* **改进**: **双指针法**
  * 分别记录，前一天买价和后一天卖价
  * 若卖价高于买价，则计算最大差值
  * 否则，更新买价为当前卖价，卖价后移一天
```java
// 普通版
public int maxProfit(int[] prices) {
    int max = 0;
    for(int i = 0;i<prices.length-1;i++){
        for (int j = i+1; j < prices.length; j++) {
            max = Math.max(max,prices[j]-prices[i]);
        }
    }
    return max<0?0:max;
}

// 改进版
public int maxProfit(int[] prices) {
    int min = prices[0],max = 0;
    for(int i = 1;i<prices.length;i++){
        if(prices[i]>min) max = Math.max(max,prices[i]-min);
        else  min = prices[i];
    } 
    return max<0?0:max;
}
```

# 125. Valid Palindrome(简单)
    A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward. Alphanumeric characters include letters and numbers.

    Given a string s, return true if it is a palindrome, or false otherwise.

* **约束**
  * `1 <= s.length <= 2*10^5`
  * `s` consists only of printable `ASCII` characters.
* **思路**：**双指针法**
  * 字符统一大小写
  * 前后指针过滤**非字母/数字**字符
  * 比较前后指针指向的字符是否相等，不等直接返回false
  * 最后返回true
```java
public boolean isPalindrome(String s) {
    s = s.toUpperCase();
    int lo=0,hi = s.length()-1;
    char lc,hc;
    while(lo<hi){
        for(lc = s.charAt(lo);!isVaild(lc);lo++,lc = s.charAt(lo)) if(lo == hi) break;
        for(hc = s.charAt(hi);!isVaild(hc);hi--,hc = s.charAt(hi)) if(hi == lo) break;
        if (lc != hc) return false;
        lo++;hi--;
    }
    return true;
}
public boolean isVaild(char c){
    return (c>=65 && c<=90) || (c>=48 && c<=57);
}
```

# 136. Single Number(简单)
    Given a non-empty array of integers nums, every element appears twice except for one. Find that single one.

    You must implement a solution with a linear runtime complexity and use only constant extra space.

* **约束**
  * `1 <= nums.length <= 3 * 10^4`
  * `-3 * 10^4 <= nums[i] <= 3 * 10^4`
  * Each element in the array appears twice except for one element which appears only once.
* **思路**：
  * 只有一个数字是单独的，其它的都是成双的
  * 可以将所有非记录的数字记录，已记录的移除，剩余的就是单独的数字
  * 选用利于查询的数据结构，如`HashSet`
* **改进**
  * 通过**异或**运算，相同的数字异或结果为`0`，`0`与任何数字异或结果为该数字
```java
public int singleNumber(int[] nums) {
    List<Integer> list = new ArrayList<>();
    for(int i:nums){
        if(list.contains(i)){
            list.remove(Integer.valueOf(i));
        }else{
            list.add(i);
        }
    }
    return list.get(0);
}

// 改进版
public int singleNumber(int[] nums) {
    int index=0;
    for(int i=0;i<nums.length;i++){
        index=index^nums[i];
    }
    
    return index;
}
```

# 141. Linked List Cycle(简单)
    Given head, the head of a linked list, determine if the linked list has a cycle in it.

* **约束**
  * The number of the nodes in the list is in the range [0, 10^4].
  * `-10^5 <= Node.val <= 10^5`
  * pos is `-1` or a **valid index** in the linked-list.
* **思路**：记忆法，通过额外数据结构(如`Set`)存储已筛选过的节点，若后面(`next`)包含，则说明有环
* **改进**: **快慢指针法**
```java
public boolean hasCycle(ListNode head) {
    Set<ListNode> set = new HashSet<>();
    while(head!=null){
        if(set.contains(head)) return true;
        set.add(head);
        head = head.next;
    }
    return false;
}

// 改进版
public boolean hasCycle(ListNode head) {
    ListNode slow = head,fast = head;
    while(fast!=null && fast.next!=null){
        slow = slow.next;
        fast = fast.next.next;
        if(slow == fast) return true;
    }
    return false;
}
```
# 144. Binary Tree Preorder Traversal(简单)
    Given the root of a binary tree, return the preorder traversal of its nodes' values.

* **约束**
  * The number of nodes in the tree is in the range `[0, 100]`.
  * `-100 <= Node.val <= 100`
* **思路**：**深度优先算法，先处理根节点，后递归遍历左右子树**
```java
public List<Integer> preorderTraversal(TreeNode root) {
    List<Integer> list = new ArrayList<>();
    dfs(root,list);
    return list;
}

public void dfs(TreeNode root,List<Integer> list) {
    if(root == null) return;
    list.add(root.val);
    dfs(root.left,list);
    dfs(root.right,list);
}
```

# 145. Binary Tree Postorder Traversal(简单)
    Given the root of a binary tree, return the postorder traversal of its nodes' values.

* **约束**
  * The number of nodes in the tree is in the range `[0, 100]`.
  * `-100 <= Node.val <= 100`
* **思路**：**深度优先算法，先递归遍历左右子树,后处理根节点**(与144题的区别)
```java
public List<Integer> postorderTraversal(TreeNode root) {
    List<Integer> list = new ArrayList<>();
    dfs(root,list);
    return list;
}


public void dfs(TreeNode root,List<Integer> list) {
    if(root == null) return;
    dfs(root.left,list);
    dfs(root.right,list);
    list.add(root.val);
}
```

# 157. Read N Characters Given Read4(中等)
    Given a file and assume that you can only read the file using a given method `read4`, implement a method to read n characters.

    Method `read4`: The API read4 reads 4 consecutive characters from the file, then returns the number of actual characters read. The return value is equal to the number of characters read.

    By using the read4 API, implement the function read:

    The function `read` should return the next `n` characters read from the file. The return value is the actual number of characters read. If the number is less than `n`, an end-of-file marker, represented by `EOF`, is returned instead. If the end of the file is reached, return `EOF`.

    The read function may be called multiple times.

* **约束**
  * `1 <= read4(char[]) <= 4`
  * `0 <= n <= 100`
  * `Position for next read is kept by the system`
  * You may assume that the destination buffer array, `buf`, is guaranteed to have enough space for storing `n` characters.
  * It is guaranteed that in a given test case the size of the output will always be at least `n`.
* **思路**
  * 从`read4`中读取数据(每次最多4个字符)，数据存入临时`char[] tmp`
  * 有数据并且实际读取长度`len`不足`n`时，继续读取，否则直接退出
  * 若没有数据，则退出循环
```java
public int read(char[] buf, int n) {
    int len = 0;
    int count = 0;
    char[] temp = new char[4];
    while((count = read4(temp))!=0){
        for(int i = 0;i<count;i++){
            if (len<n) buf[len++] = temp[i];
            // else return len; // 存在 n%4=0，当数据读取后 len=n 还会继续读取一次
            if(len==n) return len; // 满了立即退出
        } 
    }
    return len;
}
```

# 160. Intersection of Two Linked Lists(简单)
Given the heads of two singly linked-lists headA and headB, return the node at which the two lists intersect. If the two linked lists have no intersection at all, return null.

For example, the following two linked lists begin to intersect at node c1:

![image](https://assets.leetcode.com/uploads/2021/03/05/160_statement.png)
    
The test cases are generated such that there are no cycles anywhere in the entire linked structure.

Note that the linked lists must retain their original structure after the function returns. You must not modify the lists.

* **约束**
  * The number of nodes of `listA` is in the `m`.
  * The number of nodes of `listB` is in the `n`.
  * `1 <= m, n <= 3 * 10^4`
  * `1 <= Node.val <= 10^5`
  * `0 <= skipA <= m`
  * `0 <= skipB <= n`
  * `intersectVal` is `0` if `listA` and `listB` do not intersect.
  * `intersectVal == listA[skipA] == listB[skipB]` if `listA` and `listB` intersect.
* **思路**: 
  * 先分别获取链表长度
  * 长链表跳过多余节点，使两链表长度一致
  * 循环对比节点是否等同(**注意：是节点相等，不是节点值**)
```java
public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
    int m=0,n=0;
    ListNode nodeA = headA,nodeB = headB;
    // 获取链表长度
    while (nodeA!=null) {
        nodeA = nodeA.next;
        m++;
    }
    while (nodeB!=null) {
        nodeB = nodeB.next;
        n++;
    }
    // 处理为同等长度链表
    if(m>n){
        for(int skipa = m-n;skipa>0;skipa--){
            headA = headA.next;
        }
    }else if(n>m){
        for(int skipb = n-m;skipb>0;skipb--){
            headB = headB.next;
        }
    }
    // 循环对比节点值是否等同
    boolean flag = false;
    ListNode next = null;
    while (headA!=null && headB!=null) {
        if (headA == headB) {
            if (!flag){ // 第一次找到，则记录
                flag = true;
                next = headA;
            }
        }else{ // 不等重新置为空
            next = null;
        }
        headA = headA.next;
        headB = headB.next;
    }
    return next;
}
```

# 163. Missing Ranges(中等)
You are given an inclusive range [lower, upper] and a sorted unique integer array `nums` (0-indexed) where `nums` is the subset of the range `[lower, upper]`.

Return the smallest sorted list of ranges that cover every number in the range `[lower, upper]` that is not present in `nums`.

A valid range will be represented as a string in the form `"lower->upper"`, where `lower <= upper`. If there is only one number in the range, it will be represented as `"lower"` without the double hyphen. Also, two adjacent ranges `num1->num2` and `num3->num4` should be combined into one range `"num1->num4"` if they overlap.

* **约束**
  * `-10^9 <= lower <= upper <= 10^9`
  * `0 <= nums.length <= 100`
  * `lower <= nums[i] <= upper`.
  * `nums` contains no duplicate elements.
  * `nums` is sorted in ascending order.
* **思路**
```java
public List<String> FindMissingRanges(int[] nums, int lower, int upper) {
    List<String> res = new ArrayList<>();
    int pre = lower;
    for(int num:nums){
        if(num>pre) { // 不等，则判断是否在区间内，只可能时num>pre，不存在num<pre
            format(res,pre,num-1);
        }
        pre= num+1;// 无论上面如何处理，pre都前移至num+1
    }
    format(res,pre,upper); // nums[length-1] < upper
    return res;
}
public void format(List<String> res,int start,int end){
    if(start==end) res.add(String.valueOf(start)); // 单个元素
    else res.add(start+"->"+end); // 区间
}
```

# 168. Excel Sheet Column Title(简单)
Given an integer `columnNumber`, return its corresponding column title as it appears in an Excel sheet.

```txt
For example:
    A -> 1
    B -> 2
    C -> 3
    ...
    Z -> 26
    AA -> 27
    AB -> 28 
    ...
```

* **约束**
  * `1 <= columnNumber <= 2^31 - 1`
* **思路**: 
  * 实质为26进制数
  * 先计算余数，倍数`余数=倍数%26，倍数=倍数/26`
  * 余数为0，则追加`Z`，倍数减1
  * 余数不为0，则追加`余数+64`
  * 倍数不为0，则继续循环
  * 使用`StringBuilder`追加字符，最后反转
  * **注意**：`do...while,while,for`的执行逻辑区别
```java
public String convertToTitle(int columnNumber) {
    StringBuilder sb = new StringBuilder();
    int m =columnNumber,n=0;
    do{
        n = m%26;
        m = m/26;
        if (n == 0) m--;
        sb.append(n>0? (char)(n+64):'Z');
    }while(m>0);
    sb.reverse();
    return sb.toString();
}
```

# 169. Majority Element(简单)
Given an array `nums` of size `n`, return the majority element.

The majority element is the element that appears more than `⌊n / 2⌋` times. You may assume that the majority element always exists in the array.

* **约束**
  * `n == nums.length`
  * `1 <= n <= 5 * 10^4`
  * `-10^9 <= nums[i] <= 10^9`
  * The majority element is in `nums`.
* **思路**
  * 多数元素大于n/2
  * 找一个数据结构(**Map结构**)存储元素+计数，当计数大于n/2时，返回该元素
```java
public int majorityElement(int[] nums) {
    Map<Integer,Integer> map = new HashMap<>();
    int mid = nums.length/2;
    for(int num:nums){
        map.put(num,map.getOrDefault(num,0)+1);
        if(map.get(num)>mid) return num;
    }
    return -1;
}
```

# 170. Two Sum III - Data Structure Design(简单)
Design a data structure that accepts a stream of integers and checks if it has a pair of integers that sum up to a given value.

Implement the `TwoSum` class:
```markdown
* `TwoSum()` Initializes the `TwoSum` object, with an empty array initially.
* `void add(int number)` Adds the number to the data structure.
* `boolean find(int value)` Returns `true` if there exists any pair of numbers which sum is equal to the value, otherwise, return `false`.
```

* **约束**
  * `-10^5 <= number, value <= 10^5`
  * At most `5000` calls will be made to `add` and `find`.
* **思路**
  * 找一个数据结构存储`add`添加的元素
  * 从这个数据结构中双层循环，查找**两两之和**是否给定值
* **改进**:
  * 数据结构的选择改进：利于查询，如Map
  * 可以先排序，二分查找法，差值法
```java
static class TwoSum {
    private List<Integer> list = new ArrayList<>();
    public TwoSum(){}
    public void add(int number) {
        list.add(number);
    }
    public boolean find(int value) {
        for(int i = 0;i<list.size();i++){
            for(int j = i+1;j<list.size();j++){
                if(list.get(i)+list.get(j)==value) return true;
            }
        }
        return false;
    }
}

// 改进版：List+Sort+BinarySearch
public boolean find(int value) {
    Collections.sort(list);
    int left = 0,right = list.size()-1;
    while(left<right){
        int twoSum = list.get(left)+list.get(right);
        if(twoSum==value) return true;
        if(twoSum<value) left++;
        else right--;
    }
    return false;
}

// 改进版：Map + 差值法
public boolean find(int value) {
    for(int num:map.keySet()){
        int target = value - num;
        if(map.containsKey(target)){
            if(num == target && map.get(num) > 1) return true;
            if(num != target) return true;
        }
    }
    return false;
}
```

# 171. Excel Sheet Column Number(简单)
Given a string columnTitle that represents the column title as appears in an Excel sheet, return its corresponding column number.

```txt
For example:
    A -> 1
    B -> 2
    C -> 3
    ...
    Z -> 26
    AA -> 27
    AB -> 28 
    ...
```

* **约束**
  * `1 <= columnTitle.length <= 7`
  * `columnTitle` consists only of uppercase English letters.
  * `columnTitle` is in the range `["A", "FXSHRXW"]`.
* **思路**: 与十进制转26进制逻辑相反，[参考168题](#168-excel-sheet-column-title简单)
  * 从左向后循环，每循环依次，前一个和*26+当前数
* **改进**
  * 每个运算表达式，数据类型尽量保持一致，并且复杂运算独立成变量
```java
public int titleToNumber(String columnTitle) {
    int sum = 0;
    for(int i = 0;i<columnTitle.length();i++){
        char c = columnTitle.charAt(i);
        int val = c-'A'+1;
        sum = sum*26 + val;
        // sum = sum*26 + (columnTitle.charAt(i)-'A'+1);
    }
    return sum;
}
```

# 175. Combine Two Tables(简单)
```txt
`Person` table:
+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| PersonId    | int     |
| FirstName   | varchar |
| LastName    | varchar |
+-------------+---------+
PersonId is the primary key column for this table.

`Address` table:
+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| addressId   | int     |
| personId    | int     |
| city        | varchar |
| state       | varchar |
+-------------+---------+
```

**Write an SQL query to report the first name, last name, city, and state of each person in the `Person` table. If the address of a person is not present in the `Address` table, report null instead.**

Return the result table in **any order**.

* **思路** : 左连接`LEFT JOIN`
```sql
-- 不清楚为啥相同的逻辑，leetcode执行时间来回跳动
-- select `FirstName`,`LastName`,`city`,`state` from Person p left join Address a on p.PersonId = a.personId;

SELECT FirstName,LastName,city,state
FROM Person
LEFT JOIN Address
ON Person.PersonId = Address.personId;
```

# 181. Employees Earning More Than Their Managers(简单)
```txt
`Employee` table:
+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| name        | varchar |
| salary      | int     |
| managerId   | int     |
+-------------+---------+
```

**Write a solution to find the employees who earn more than their managers.**

Return the result table in **any order**.

* **思路** : `salary`超过经理的员工
  * **注意**：`JOIN`方式适合扩展属性，`WHERE`适合筛选数据
```sql
select e1.`name` as `Employee` from Employee e1 ,Employee e2 where  e1.managerId = e2.id AND e1.salary > e2.salary
```

# 182. Duplicate Emails(简单)
```txt
`Person` table:
+----+---------+
| id | email   |
+----+---------+
| 1  | a@b.com |
| 2  | c@d.com |
| 3  | a@b.com |
+----+---------+
```

**Write an SQL query to report all the duplicate emails.** 
Return the result table in **any order**.

* **思路**: `GROUP BY ... HAVING`分组查询
```sql
select email as Email from person group by email having count(email) > 1;
```

# 183. Customers Who Never Order(简单)
```txt
Table: Customers
+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| name        | varchar |
+-------------+---------+

Table: Orders
+-------------+------+
| Column Name | Type |
+-------------+------+
| id          | int  |
| customerId  | int  |
+-------------+------+
```
**Write a solution to find all customers who never order anything.**
* **思路**: `NOT IN`
```sql
select name as Customers from Customers where id not in (select customerId from Orders)
```
