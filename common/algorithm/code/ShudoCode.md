# python
1. 初始化数独数据
2. 数独数据分组(行，列，区)
   1. 区：**calc_region**  `int(row / 3) * 3 + int(col / 3) + 1`
3. 检查可选项，唯一选项直接赋值，并删除互斥分组可选对应值
4. 可选数排序，从最少开始处理
5. 循环排序可选数独 `handle_node_back_num`
   1. 遍历可选列表
   2. 检查可选数是否满足
      1. 满足赋值
      2. 递归处理下一个数独可选，查看是否满足
         1. 满足继续，直至无数独可处理，出解
         2. 不满足，赋值置为0，继续遍历
   3. 若遍历结束仍不满足，向上处理上一个数独遍历
      1. 无数独可回溯，则无解
```python
import copy
import sys
import time


class Node:
    val: int = 0
    row: int  # 行号，1-9
    col: int  # 列号，1-9
    # isFinal: bool = False  # 是否是确定值，True代表不可修改，False代表不确定
    region: int  # 区域，1-9
    back_num: list = []  # 备选数字

    def __init__(self, row: int, col: int, val=0):
        if val < 0 or val > ELEMENT_LEN:
            raise Exception("数值范围不符！")
        self.val = val
        self.row = row
        self.col = col
        # self.isFinal = val != 0
        self.region = calc_region(row - 1, col - 1)


# 节点区域计算，1-9
def calc_region(row, col):
    return int(row / 3) * 3 + int(col / 3) + 1


# 初始值写入
def write_row_data(row, values):
    col, numbers, data = 0, [], []
    for value in values:
        value = int(value)
        if col > ELEMENT_LEN:
            raise Exception("列数超出规定[9列]!")

        if value != 0 and numbers.__contains__(value):
            raise Exception("数据有重合!")

        numbers.append(value)
        data.append(Node(row + 1, col + 1, value))
        col += 1
    return data


# # 请输入每行值，每列空用0代替:如 103050608
def start_input_data():
    data: list = [[] for _ in range(ELEMENT_LEN)]
    print("请按行输入内容，每列为空用0代表!")
    for row in range(ELEMENT_LEN):
        rowVal = input("  请输入第%s行数值：" % (row + 1))
        if rowVal:
            try:
                data[row] = write_row_data(row, rowVal)
            except Exception as e:
                raise Exception("第%s行数据有误:%s" % (row, e.args))
    return data


# 初始节点可选列表处理
def init_back_num_to_node(backNums: list, node: Node):
    if node.val != 0:  # 如果节点有初值，在可选列表中移除该值
        return
    if len(node.back_num) == 0:  # 非初值节点可选为0时，赋值新的可选列表
        node.back_num = backNums
    else:  # 可选不为空，则取交集
        node_back = node.back_num
        node.back_num = []
        for num in node_back:
            if backNums.__contains__(num):
                node.back_num.append(num)


# 初始分组节点的可选列表处理
def check_group_data(data: list, tag: str):
    print("开始校验%s数据" % tag, end=" ")
    for i in range(len(data)):
        print(i + 1, end=" ")
        backNums = copy.copy(NUMBER_ARRAY)
        for node in data[i]:
            if backNums.__contains__(node.val):
                backNums.remove(node.val)
        for node in data[i]:
            init_back_num_to_node(backNums, node)
    print()


# 获取分组数据
def init_group_data():
    col_data, region_data = [[] for _ in range(ELEMENT_LEN)], [[] for _ in range(ELEMENT_LEN)]
    # 处理同列拒接
    for nodes in ROW_DATA:
        for node in nodes:
            col_data[node.col - 1].append(node)
    # 处理同区聚集
    for nodes in ROW_DATA:
        for node in nodes:
            region_data[node.region - 1].append(node)
    return col_data, region_data


def remove_back_num(data, i, num):
    for node in data[i]:
        if node.val != 0:
            continue
        if node.back_num.__contains__(num):
            node.back_num.remove(num)


# 获取最大可选数
def max_num_list(data):
    max_num = 0
    for nodes in data:
        for node in nodes:
            back_len = len(node.back_num)
            if back_len == 1:  # 将仅有一个选项的直接设置值，并剔除可选列表
                node.val = node.back_num[0]
                node.back_num = []
                # 其他节点可选值去掉该值更快
                remove_back_num(ROW_DATA, node.row - 1, node.val)
                remove_back_num(COL_DATA, node.col - 1, node.val)
                remove_back_num(REGION_DATA, node.region - 1, node.val)
                continue
            max_num = max(max_num, back_len)
    return max_num


# 筛选可选列表节点集
def get_back_num_list():
    back_num_len = max_num_list(ROW_DATA) - 1
    back_num_list = [[] for _ in range(back_num_len)]
    for nodes in ROW_DATA:
        for node in nodes:
            if node.val != 0:
                continue
            back_len: int = len(node.back_num)
            # if back_len == 1:    # 理论不会存在该现象，已经在max_num_list处理了
            #     node.val = node.back_num[0]
            #     continue
            back_len -= 2  # 直接记录可选数在2以上的节点
            back_num_list[back_len].append(node)
    return back_num_list


# 可选节点按数量排序集
def back_num_list_sort(data):
    num_list = []
    for nodes in data:
        for node in nodes:
            num_list.append(node)
    return num_list


# 检查分组数据是否存在该值
def check_value(data, num):
    for node in data:
        if node.val == num:
            return False
    return True


# 检查节点值是否在对应范围有效
def check_node_value(node, num):
    return check_value(ROW_DATA[node.row - 1], num) \
           and check_value(COL_DATA[node.col - 1], num) \
           and check_value(REGION_DATA[node.region - 1], num)


# 解数独递归算法
def handle_node_back_num(data, i):
    if i >= len(data):  # 结束
        return True
    node = data[i]
    flag = False
    for num in node.back_num:
        if check_node_value(node, num) is False:
            continue
        node.val = num
        flag = handle_node_back_num(data, i + 1)
        if not flag:
            node.val = 0
    return flag


# 常量区
ELEMENT_LEN, ROW_DATA, COL_DATA, REGION_DATA, CONTENT_TYPE = 9, None, None, None, 1
NUMBER_ARRAY = [i for i in range(1, ELEMENT_LEN + 1)]  # 数值列表
TMP_CONTENTS = ["050320600","004070050","600008000","000002008","201703000","800069410","910000005","036805000","000001004"]

def init_data():
    # 使用临时数据模拟
    # data = [write_row_data(row, TMP_CONTENTS[row]) for row in range(ELEMENT_LEN)]
    data = None
    if CONTENT_TYPE == 1:
        content = input("开始输入全部数据:\n")
        TMP_CONTENTS = content.split(",")
        data = [write_row_data(row, TMP_CONTENTS[row]) for row in range(ELEMENT_LEN)]
    else:
        data = start_input_data()
    return data

# 输出数独信息
def print_data(data):
    for row in data:
        for col in row:
            print(col.val, end=" ")
        print()

if __name__ == "__main__":
    content = input("欢迎进入数独处理程序，请选择输入内容方式\n"
                    "1 全部数据,如050320600,004070050,600008000,000002008,201703000,800069410,910000005,036805000,000001004\n"
                    "2 按行输入，如050320600\n >>>")
    if content == "2":
        CONTENT_TYPE = 2
    else:
        CONTENT_TYPE = 1
    content = input("按回车开始输入数据，否则按Q退出")
    while content != "Q":
        try:
            # 真实逻辑
            ROW_DATA = init_data()
            if ROW_DATA is None:
                sys.exit()
            # 输出原始数据
            print_data(ROW_DATA)

            start = time.time()
            # 分组处理
            COL_DATA, REGION_DATA = init_group_data()

            print("-----------开始处理-----------")
            # 节点可选值初筛
            # 同行校验
            check_group_data(ROW_DATA, tag="行")
            # 同列校验
            check_group_data(COL_DATA, tag="列")
            # 同区校验
            check_group_data(REGION_DATA, tag="区")

            # 获得按可选列表长度排序的节点列表
            handle_node_list = back_num_list_sort(get_back_num_list())

            if not handle_node_back_num(handle_node_list, 0):
                print("无解")

            print("-----------结束处理-----------")
            # 输出结果
            print_data(ROW_DATA)

            print("本次处理共用时: %.2f s" % (time.time() - start))
        except Exception as ex:
            print("数独解析出现异常", ex)
        content = input("按Q结束，否则继续")
```
# Java
* 基础版
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
