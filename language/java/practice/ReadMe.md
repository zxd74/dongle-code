# 说明
该目录为自实现Code目录，例如实现数据结构定义等等

# 树结构
## 二叉树

## 二叉搜索树树
难点：删除操作
```java
    // 取最小节点，即最左子树节点
    private BNode<T> minNode(BNode<T> node){
        if (node == null)
            return null;
        while (node.leftNode!=null)
            node = node.leftNode;
        return node;
    }

    // 取最大节点，即最右子树节点
    private BNode<T> maxNode(BNode<T> node){
        if (node == null)
            return null;
        while (node.rightNode != null)
            node = node.rightNode;
        return node;
    }
```
### 查找
```java
    private BNode<T> search(BNode<T> node,T t){
        BNode<T> curNode = node;int cmp;
        while (curNode != null){
            cmp = curNode.t.compareTo(t);
            if (cmp < 0)
                curNode = curNode.leftNode;
            else if (cmp > 0)
                curNode = curNode.rightNode;
            else
                return curNode;
        }
        return null;
    }
```
### 插入
```java
    private BNode<T> insert(BNode<T> node, T t){
        BNode<T> curNode = node,parent = null;int cmp;
        while (curNode != null){
            parent = curNode;
            cmp = curNode.t.compareTo(t);
            if (cmp < 0)
                curNode = curNode.leftNode;
            else
                curNode = curNode.rightNode;
        }
        BNode<T> tNode = new BNode<>(t);
        if (parent == null){
            return tNode;
        }
        tNode.parent = parent;
        cmp = parent.t.compareTo(t);
        if (cmp < 0)
            parent.leftNode = tNode;
        else
            parent.rightNode = tNode;
        return node;
    }

    private void insert(BinaryTree<T> btree, T t){
        BNode<T> curNode = btree.root,parent = null;int cmp;
        while (curNode != null){
            parent = curNode;
            cmp = curNode.t.compareTo(t);
            if (cmp < 0)
                curNode = curNode.leftNode;
            else
            curNode = curNode.rightNode;
        }
        BNode<T> tNode = new BNode<>(t);
        if (parent == null)
            btree.root = tNode;
        else{
            tNode.parent = parent;
            cmp = parent.t.compareTo(t);
            if (cmp < 0)
                parent.leftNode = tNode;
            else
                parent.rightNode = tNode;
        }
    }

```
### 删除(重点)
1. 无叶子：父节点置空对应子节点
2. 仅有一个叶子：将子节点替换删除节点，并维护父节点关系
3. 有两个叶子：左子树最右节点和右子树最左节点值与删除节点值替换
```java
   // 优秀1：便捷！！！ 当删除顶点时，可以传btree对象自身，可便于直接操作root，否则root新值只能通过返回值传递
    // 优秀2：高效！！！ 直接查找替换对象，将替换对象与删除对象做值转换
    public void remove(BNode<T> node,BNode<T> delNode){
        BNode<T> subNode,replaceNode;
        replaceNode = (delNode.leftNode == null || delNode.rightNode == null) ?
                delNode : minNode(delNode.rightNode);
        subNode = replaceNode.leftNode != null ? replaceNode.leftNode : replaceNode.rightNode;
        if (subNode != null)
            subNode.parent = replaceNode.parent;

        if (replaceNode.parent == null)
            node = subNode;
        else if (replaceNode == replaceNode.parent.leftNode)
            replaceNode.parent.leftNode = subNode;
        else if (replaceNode == replaceNode.parent.rightNode)
            replaceNode.parent.rightNode = subNode;
        // 该逻辑为值替换
        if (replaceNode != delNode)
            delNode.t = replaceNode.t;
        replaceNode = null;
    }

    // 简化版;
    public void remove2(BNode<T> node,T t){
        if (node == null)
            return null;
        int cmp = node.t.compareTo(t);
        if (cmp > 0)
            node.leftNode = remove(node.leftNode,t);
        else if (cmp < 0)
            node.rightNode = remove(node.rightNode,t);
        else{
            BNode<T> tmpNode;
            if (node.leftNode == null || node.rightNode == null){
                tmpNode = node.leftNode == null ? node.rightNode : node.leftNode;
                if (node.parent != null){
                    if (node.parent.leftNode == node)
                        node.parent.leftNode = tmpNode;
                    else
                        node.parent.rightNode = tmpNode;
                    if (tmpNode != null)
                        tmpNode.parent = node.parent;
                }
                node = tmpNode;
            }else {
                // 若左右节点都存在，则找右子树下最小节点
                tmpNode = minNode(node.rightNode);
                node.t = tmpNode.t;
                // 删除右子树下最小节点
                node.rightNode = remove(node.rightNode,tmpNode.t);
                // C++ 请注意GC处理
            }
        }
        return node;
    }
```
## 平衡二叉树
实际增减逻辑同二叉搜索树一致，仅在每次插入或删除操作后进行节点平衡逻辑。

**平衡判断**
1. 节点的左子树比右子树高(>1)时
   1. 若左子树节点的平衡差不小于0，则需要LL型平衡，即左子树的左子树节点高
   2. 若左子树节点的平衡差小于0，则需要LR型平衡，即左子树的右子树节点高
2. 节点的左子树比右子树低(<-1)时
   1. 若右子树节点平衡差不大于0时，则需要RR型平衡，即右子树的右子树节点高
   2. 若右子树节点平衡差大于0时，则需要RL型平衡，即右子树的左子树节点高

**平衡方向**
1. LL 型平衡：左上升，左右变主左
   1. 左子树升为主节点，原主节点降为右节点
   2. 原左子树的右节点，变为原主节点的左子树
2. **LR 型平衡**：LR 型调整：左右升主，主降右（先左旋后右旋）
   1. 左子树的右子树升主节点，原主节点降为右子树
   2. 左子树变为原左子树的右子树的左节点
3. **RR 型调整**：右升主，主降左，右左变主右
   1. 右子树升为主节点，原主节点降为左子树
   2. 原右子树的左节点，变为原主节点的右子树
4. **RL 型平衡**：右左升主，主降左 （先右旋后左旋）
   1. 右子树的左子树升主节点，原主节点降为左子树
   2. 原右子树变为原右子树的左子树的右节点

```java
    // L平衡调节
    private BNode<T> llBalancer(BNode<T> node){
        if (node == null)
            return null;
        if (node.leftNode == null)
            return node;
        // 上述逻辑可以不需要
        BNode<T> tmpNode = node.leftNode;
        node.leftNode  = tmpNode.rightNode;
        tmpNode.parent = node.parent;
        tmpNode.rightNode = node;
        node.parent = tmpNode;

        node.height = Math.max(height(node.leftNode),height(node.rightNode));
        tmpNode.height = Math.max(height(tmpNode.leftNode),height(tmpNode.rightNode));
        return tmpNode;
    }
    // R平衡调节
    private BNode<T> rrBalancer(BNode<T> node){
        if (node == null)
            return null;
        if (node.rightNode == null)
            return node;
        // 上述逻辑可以不需要
        BNode<T> tmpNode = node.rightNode;
        node.rightNode  = tmpNode.leftNode;
        tmpNode.parent = node.parent;
        tmpNode.leftNode = node;
        node.parent = tmpNode;

        node.height = Math.max(height(node.leftNode),height(node.rightNode));
        tmpNode.height = Math.max(height(tmpNode.leftNode),height(tmpNode.rightNode));
        return tmpNode;
    }

    private int getBalancer(BNode<T> node){
        return node == null ? 0 : (height(node.leftNode) - height(node.rightNode));
    }

    private int height(BNode<T> node){
        return node == null ? 0 : node.height;
    }
```
```java
    // 平衡逻辑，前提node的父节点层是平衡的
    private BNode<T>  balancer(BNode<T> node){
        if (node == null)
            return node;

        node.height = 1  + Math.max(height(node.leftNode),height(node.rightNode));
        int balancer = getBalancer(node);

        if (balancer > 1 && getBalancer(node.leftNode) >= 0) // LL型平衡调整
            return llBalancer(node);
        else if (balancer > 1){ // LR型平衡调整
            node.leftNode = rrBalancer(node.leftNode);
            return llBalancer(node);
        }else if (balancer < -1 && getBalancer(node.rightNode) <= 0) // RR型平衡调整
            return rrBalancer(node);
        else if (balancer < -1){  // RL型平衡调整
            node.rightNode = llBalancer(node.rightNode);
            return rrBalancer(node);
        }

        return node;
    }
```
