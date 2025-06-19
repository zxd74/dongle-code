# 基础
## 空间复杂度
* 定义：衡量算法运行所需的额外内存空间随输入规模的变化趋势。
* 关键点：
  * 只计算 额外空间（输入数据本身不计算）。
  * 递归算法需考虑调用栈空间。

**常见空间复杂度**
复杂度	|示例算法	|解释
--|----------|------
O(1)	|迭代斐波那契	|仅用常数个变量
O(n)	|归并排序	|需要额外数组存储临时结果
O(log n)	|快速排序（递归栈）	|递归深度为 log n
O(n)	|递归斐波那契	|递归深度为 n

**计算方法**
* 统计额外变量、数据结构、递归栈的占用。
* 用输入规模 n 表示空间占用。

## 时间复杂度
* 定义：衡量算法运行时间随输入规模增长的变化趋势。
* 关键点：
  * 关注 最坏情况（`Big-O` 表示法，如 `O(n)`）。
  * 忽略常数项和低阶项（如 `O(3n² + 2n) → O(n²)`）。

**常见时间复杂度**
复杂度	|示例算法	|解释
--|----------|------
O(1)	|数组访问	|常数时间操作
O(log n)	|二分查找	|每次问题规模减半
O(n)	|线性搜索	|遍历所有元素
O(n log n)	|归并排序	|分治 + 线性合并
O(n²)	|冒泡排序	|嵌套循环
O(2ⁿ)	|斐波那契递归	|指数级增长

**计算方法**
* 统计基本操作次数（如循环、比较、赋值）。
* 用输入规模 n 表示操作次数。
* 保留最高阶项，忽略系数。

# 方法
## 基本方法
方法	|适用场景	|时间复杂度	|空间复杂度
------|----------|------------|----------
穷举法	|小规模数据	|O(n²)	|O(1)
递推法	|数学递推问题	|O(n)	|O(1)
递归法	|树遍历、分治	|O(n)	|O(n)
迭代法	|数值计算、查找	|O(n)	|O(1)
模拟法	|游戏逻辑、状态机	|O(n)	|O(n)
双指针	|有序数组、链表	|O(n)	|O(1)
位运算	|状态压缩、优化计算	|O(1)	|O(1)
### 穷举法（Brute Force）
* 原理：尝试所有可能的解，直到找到正确答案。
* 适用场景：小规模数据、密码破解、组合优化等。
* 步骤：
  * 列举所有可能的候选解。
  * 逐个验证是否符合条件。
  * 返回符合条件的解。
```python
# 示例：查找数组中的两数之和等于目标值（暴力解法）
def two_sum(nums, target):
    n = len(nums)
    for i in range(n):
        for j in range(i + 1, n):
            if nums[i] + nums[j] == target:
                return [i, j]
    return []
```
### 递推法（Iterative Approach）
* 原理：从初始条件出发，逐步推导后续结果。
* 适用场景：斐波那契数列、数学递推问题。
* 步骤：
  * 定义初始条件（如 f(0), f(1)）。
  * 使用循环逐步计算后续值。

```java
// 示例：斐波那契数列（递推版）
public int fibonacci(int n) {
    if (n <= 1) return n;
    int a = 0, b = 1;
    for (int i = 2; i <= n; i++) {
        int c = a + b;
        a = b;
        b = c;
    }
    return b;
}
```
### 递归法（Recursion）
* 原理：函数调用自身来分解问题。
* 适用场景：树遍历、汉诺塔、分治算法。
* 步骤：
  * 定义递归终止条件（Base Case）。
  * 将问题分解为更小的子问题。
  * 递归调用并合并结果。

```python
# 示例：计算阶乘（递归版）
def factorial(n):
    if n == 0:
        return 1
    return n * factorial(n - 1)
```
### 迭代法（Iteration）
* 原理：通过循环逐步逼近解。
* 适用场景：数值计算（如牛顿迭代）、查找算法。
* 步骤：
  * 初始化变量。
  * 循环更新变量，直到满足终止条件。

```java
// 示例：二分查找（迭代版）
public int binarySearch(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] == target) return mid;
        else if (nums[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

### 模拟法（Simulation）
* 原理：按照问题描述逐步模拟过程。
* 适用场景：游戏逻辑、物理仿真、状态机。
* 步骤：
  * 定义初始状态。
  * 按照规则逐步更新状态。
  * 直到达到终止条件。

```python
# 示例：模拟约瑟夫环问题（报数出圈）
def josephus(n, k):
    people = list(range(1, n + 1))
    index = 0
    while len(people) > 1:
        index = (index + k - 1) % len(people)
        people.pop(index)
    return people[0]
```

### 数学归纳法（Mathematical Induction）
* 原理：通过数学证明推导算法正确性。
* 适用场景：递归算法验证、动态规划状态转移方程。
* 步骤：
  * Base Case：证明初始情况成立。
  * Inductive Step：假设 `n=k` 成立，证明 `n=k+1` 也成立。

```markdown
示例：证明斐波那契数列递推公式

Base Case:
- F(0) = 0 ✔️
- F(1) = 1 ✔️

Inductive Step:
假设 F(k) 和 F(k-1) 正确，则：
F(k+1) = F(k) + F(k-1) 也成立。
```
### 双指针法（Two Pointers）
* 原理：用两个指针协同遍历数据。
* 适用场景：有序数组、链表操作。
* 步骤：
  * 初始化两个指针（如 left=0, right=n-1）。
  * 根据条件移动指针。

```python
# 示例：反转数组
def reverse_array(arr):
    left, right = 0, len(arr) - 1
    while left < right:
        arr[left], arr[right] = arr[right], arr[left]
        left += 1
        right -= 1
    return arr
```

### 位运算法（Bit Manipulation）
* 原理：利用位运算优化计算。
* 适用场景：状态压缩、快速乘除、哈希优化。

```java
// 示例：判断奇偶
public boolean isOdd(int n) {
    return (n & 1) == 1;
}
```

## 设计方法(思想)
方法	|时间复杂度	|空间复杂度	|典型问题
--	|:-:	|:-:	|:-:
分治法	|O(n log n)	|O(n)	|归并排序
动态规划	|O(n²)	|O(n)	|背包问题
贪心算法	|O(n log n)	|O(1)	|霍夫曼编码
回溯法	|O(n!)	|O(n)	|八皇后

### 分治法（Divide and Conquer）
* 原理：将问题分解为多个子问题，递归解决后合并结果。
* 适用场景：排序、快速幂、矩阵乘法等。
* 步骤：
  * 分解：将问题划分为子问题。
  * 解决：递归解决子问题。
  * 合并：合并子问题的解。
```py
# 归并排序
def merge_sort(arr):
    if len(arr) <= 1:
        return arr
    mid = len(arr) // 2
    left = merge_sort(arr[:mid])
    right = merge_sort(arr[mid:])
    return merge(left, right)

def merge(left, right):
    result = []
    i = j = 0
    while i < len(left) and j < len(right):
        if left[i] < right[j]:
            result.append(left[i])
            i += 1
        else:
            result.append(right[j])
            j += 1
    result.extend(left[i:])
    result.extend(right[j:])
    return result
```
### 动态规划法（Dynamic Programming）
**自底向上**或**自顶向下**（记忆化）的递推方法，通过将问题分解为重叠子问题，存储子问题的解（避免重复计算），最终合并得到全局最优解。
* 原理：通过存储子问题的解避免重复计算。
  * **最优子结构**：全局最优解依赖于子问题的最优解。
  * **重叠子问题**：子问题被多次重复计算，通过表格或数组存储结果。
* 适用场景：最短路径、背包问题等。
* 步骤：
  * 定义状态：定义子问题的状态。
  * 状态转移：定义状态之间的转移方程。
  * 初始化和边界条件：定义初始状态和边界条件。
  * 计算解：从初始状态开始，逐步计算状态转移方程，直到得到最终解。
```py
# 斐波那契数列
public int fibonacci(int n) {
    int[] dp = new int[n + 1];
    dp[0] = 0; dp[1] = 1;
    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
}
```

### 贪心算法（Greedy Algorithm）
* 原理：每一步选择局部最优解，希望达到全局最优。
* 适用场景：霍夫曼编码、最小生成树等。
* 步骤：
  * 将问题分解为多个步骤。
  * 每一步选择当前最优解。
```py
# 找零钱问题（用最少的硬币）
def coin_change(coins, amount):
    coins.sort(reverse=True)
    count = 0
    for coin in coins:
        while amount >= coin:
            amount -= coin
            count += 1
    return count if amount == 0 else -1
```
### 回溯法（Backtracking）
* 原理：通过试错探索所有可能解，回溯无效路径。
* 适用场景：八皇后、数独等。
* 步骤：
  * 选择一条路径并前进。
  * 遇到死胡同时回溯。
```py
# N 皇后问题
public List<List<String>> solveNQueens(int n) {
    List<List<String>> solutions = new ArrayList<>();
    backtrack(new int[n], 0, solutions);
    return solutions;
}

private void backtrack(int[] queens, int row, List<List<String>> solutions) {
    if (row == queens.length) {
        solutions.add(generateSolution(queens));
        return;
    }
    for (int col = 0; col < queens.length; col++) {
        if (isValid(queens, row, col)) {
            queens[row] = col;
            backtrack(queens, row + 1, solutions);
        }
    }
}
```
### 分支限界法（Branch and Bound）
分支限界法通过广度优先或最小耗费优先的方式搜索问题的解空间树，逐步逼近最优解。
* 原理：通过优先级队列剪枝无效分支。
* 适用场景：旅行商问题（TSP）。
* 步骤：
  * 计算当前节点的最优边界。
  * 剪枝超出已知解的路径。
```py
# 0-1 背包问题（伪代码）
def knapsack(items, capacity):
    queue = PriorityQueue()
    best_value = 0
    queue.put((0, 0, 0))  # (bound, value, weight)
    while not queue.empty():
        bound, value, weight = queue.get()
        if bound <= best_value:
            continue
        best_value = max(best_value, value)
        # 扩展子节点...
```

# 排序算法
* [冒泡排序](algorithm-sort.md#冒泡排序)
* [直接排序](algorithm-sort.md#直接排序)
* [反转排序](algorithm-sort.md#反转排序)
* [插入排序](algorithm-sort.md#插入排序)
* [希尔排序](algorithm-sort.md#希尔排序)
* [归并排序](algorithm-sort.md#归并排序)
* [快速排序](algorithm-sort.md#快速排序)
* [堆排序](algorithm-sort.md#堆排序)
* [计数排序](algorithm-sort.md#计数排序)
* [桶排序](algorithm-sort.md#桶排序)
* [基数排序](algorithm-sort.md#基数排序)

# 查找算法
- [二分法查找](algorithm-find.md#二分法查找)
- [顺序查找](algorithm-find.md#顺序查找)

# 负载均衡算法
* 任务平分类(轮询算法):各服务依次轮询使用
* 负载均衡类: 服务负载最低优先，如连接数、http请求数、cpu负载等
  * 最小连接数
  * 加权随机数
  * 加权轮询
* 性能最优类: 优先分配给响应随时间最快的服务
* Hash分类:根据任务中的某些关键信息进行Hash运算，将相同Hash请求分配到相同服务上
* 随机分类:根据随机数，随机提供服务

## nginx负载
1. 轮询(默认)
2. weight权重
3. ip_hash
4. fair（第三方）
5. url_hash

# 实践
## 斐波那契数列
* 简单实现：动态规划，通过递归方式实现
  * 递归版：时间复杂度O(2^n)，由于前面第二个会被前面第一个计算一次，导致重复计算前面第二个，所以时间复杂度是指数级的
* 改进版：
  * 通过**保存前两个数**，避免重复计算
```java
// 递归版
public static int climbStairs(int n ){
    if(n == 0 || n == 1) return 1;
    return climbStairs(n-1) + climbStairs(n-2); // 递归超时了
}

// 循环版
public static int climbStairs2(int n ){
    if(n < 4) return n;
    int p1 = 2;
    int p2 = 3;
    int cur =0;
    for (int i = 4; i < n +1; i++) { // for (=> int i = 3; i < n; i++)
        cur = p1 + p2;
        p1 = p2;
        p2 = cur;
    }
    return cur;
}
```
