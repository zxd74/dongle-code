# 最小栈问题
```txt
提供一个支持最小栈的栈，要求在O(1)时间复杂度内完成以下操作：
1. push(x) -- 将元素x压入栈中
2. pop() -- 移除栈顶元素
3. top() -- 获取栈顶元素
4. getMin() -- 检索栈中的最小元素
```
```js
class Strack {
    _strack = []
    _minStack = [] // 辅助栈：存放最小值
    push(x) {
        this._stack.push(x)
        // 当传入数据时，验证最小值，若最小栈无值或小于最小栈头元素时，则入栈
        if (this._minStack.length === 0 || x < this._minStack[this._minStack.length - 1]) {
            this._minStack.push(x)
        } else {
            this._minStack.push(this._minStack[this._minStack.length - 1])
        }
    }
    pop() {
        this._stack.pop()
        this._minStack.pop() // 当出站时，同步出战
    }
    top() {
        return this._stack[this._stack.length - 1] // 获取栈顶元素
    }
    getMin() {
        return this._minStack[this._minStack.length - 1] // 获取最小栈顶元素
    }
}
```
