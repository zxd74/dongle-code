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

# 13 Roman to Integer
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