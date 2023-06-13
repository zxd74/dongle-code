package base.practice.structure.tree;

import javax.validation.constraints.NotNull;

/**
 * @author Dongle
 * @desc 二叉树
 * @since 2022/3/23 9:52
 */
public class BinaryTree<T extends Comparable<T>> implements Tree<T>{

    BNode<T> root;

    public BinaryTree() {
    }

    public BinaryTree(T t) {
        this.root = new BNode<>(t);
    }

    @Override
    public void insert(T t) {
        //root = insert(root,t); // 这种方案适用于BNode root不允许为空的情况
        insert(this,t);
    }

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

    @Override
    public boolean contain(T t) {
        return search(root,t) != null;
    }

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

    @Override
    public void remove(T t) {
        root = remove(root,t);

        BNode<T> delNode;
        while ((delNode = search(root,t)) !=null)
            remove(this,delNode);
    }

    private BNode<T> remove(BNode<T> node,T t){
        BNode<T> delNode;
        while ((delNode = search(node,t)) != null){
            node = remove(node,delNode);
        }
        return node;
    }

    private BNode<T> remove(BNode<T> node,BNode<T> delNode){
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
        if (replaceNode != delNode)
            delNode.t = replaceNode.t;
        replaceNode = null;


        // 以下为冗余逻辑，但便于理解的实现方法
        //BNode<T> parent = delNode.parent,newNode;
        //if (delNode.leftNode == null && delNode.rightNode == null){
        //    // 叶子节点处理
        //    newNode = null;
        //}else if (delNode.leftNode == null){
        //    // 仅有一个右子树节点处理
        //    newNode = delNode.rightNode;
        //    // 替换父节点
        //    newNode.parent = parent;
        //}else if(delNode.rightNode == null){
        //    // 仅有一个左子树节点处理
        //    newNode = delNode.leftNode;
        //    newNode.parent = parent;
        //}else{
        //    // 方案一：以左子树为替换对象处理
        //    BNode<T> lNode = delNode.leftNode,lrNode = null;
        //    while(lNode != null){
        //        lrNode = lNode;
        //        lNode = lNode.rightNode;
        //    }
        //    // TODO 重点理解以下逻辑 处理替换节点与其他节点关系
        //    // 若左子树不为空，则左子树替换最右子树的父节点的右子树
        //    if (lrNode.leftNode != null){
        //        lrNode.parent.rightNode = lrNode.leftNode;
        //        lrNode.leftNode.parent = lrNode.parent;
        //    }
        //    // 删除节点的左子树作为新节点的左子树处理，并替换对应父节点关系
        //    lrNode.leftNode = delNode.leftNode;
        //    delNode.leftNode.parent = lrNode;
        //    newNode = lrNode;
        //    // 替换新节点的父节点为删除节点的父节点
        //    newNode.parent = parent;
        //    // 方案一结束..........
        //
        //    // 方案二：以右子树最左节点为替换对象处理
        //    BNode<T> rNode = delNode.rightNode,rlNode = null;
        //    while(rNode != null){
        //        rlNode = rNode;
        //        rNode = rNode.leftNode;
        //    }
        //    // TODO 重点理解以下逻辑 处理替换节点与其他节点关系
        //    if (rlNode.rightNode != null){
        //        rlNode.parent.leftNode = rlNode.rightNode;
        //        rlNode.rightNode.parent = rlNode.parent;
        //    }
        //    rlNode.rightNode = delNode.rightNode;
        //    delNode.rightNode.parent = rlNode;
        //    newNode = rlNode;
        //    newNode.parent = parent;
        //    // 方案二结束..........
        //}
        //if (parent == null)
        //    // 若为树顶，则将新节点赋值给传入节点
        //    node = newNode;
        //else{
        //    if (parent.leftNode == delNode)
        //        parent.leftNode = newNode;
        //    else
        //        parent.rightNode = newNode;
        //}
        // 新节点的父节点配置可以在这里最后处理
        //if (newNode != null)
        //    newNode.parent = parent;
        return node;
    }

    /**
     * 需要确保node为内部节点，该方法不可对外开放
     *  // 优秀1：便捷！！！ 当删除顶点时，传世btree对象自身，可便于直接操作root，否则root新值只能通过返回值传递
     *  // 优秀2：高效！！！ 直接查找替换对象，将替换对象与删除对象做值转换
     * @param btree
     * @param delNode
     */
    private void remove(BinaryTree<T> btree,BNode<T> delNode){
        // 内部节点该步骤可省略
        //if (node == null)
        //    return;
        BNode<T> subNode,replaceNode;
        if (delNode.leftNode == null || delNode.rightNode == null)
            replaceNode = delNode;
        else
            // 左右子节点都不为空时，获取右节点的最小值
            replaceNode = minNode(delNode.rightNode);

        if (replaceNode.leftNode != null)
            subNode = replaceNode.leftNode;
        else
            subNode = replaceNode.rightNode;

        // 新节点不为空时，配置其父节点
        if (subNode != null)
            subNode.parent = replaceNode.parent;

        // 删除节点的父节点为空时则为顶部节点
        if (replaceNode.parent == null)
            btree.root = subNode;
        else if (replaceNode == replaceNode.parent.leftNode)
            // 内部节点才会生效
            replaceNode.parent.leftNode = subNode;
        else if (replaceNode == replaceNode.parent.rightNode)
            // 内部节点才会生效
            replaceNode.parent.rightNode = subNode;

        // 若替换节点和删除节点不同，则将待删除节点的值替换成替换节点中的值
        if (replaceNode != delNode)
            delNode.t = replaceNode.t;

        // 删除替换节点（值已经替换到删除节点中）// jdk较高版本无需再声明
        replaceNode = null;
    }

    private BNode<T> minNode(BNode<T> node){
        if (node == null)
            return null;
        while (node.leftNode!=null)
            node = node.leftNode;
        return node;
    }

    private BNode<T> maxNode(BNode<T> node){
        if (node == null)
            return null;
        while (node.rightNode != null)
            node = node.rightNode;
        return node;
    }

    @Override
    public int size() {
        return size(root);
    }
    private int size(BNode<T> node){
        if (node == null)
            return 0;
        int size = 1;
        if (node.leftNode != null){
            size += size(node.leftNode);
        }
        if (node.rightNode != null){
            size += size(node.rightNode);
        }
        return size;
    }

    @Override
    public int depth() {
        return depth(root);
    }
    private int depth(BNode<T> node){
        if (node == null)
            return 0;
        int depth = 1;
        if (node.leftNode != null || node.rightNode != null){
            depth += depth(node.leftNode) + depth(node.rightNode);
        }
        return depth;
    }

    @Override
    public int height(T t, T t1) {
        return Math.abs((height(root,search(root,t)) - height(root,search(root,t1))));
    }

    private int height(BNode<T> parent, BNode<T> node){
        if (node == null || parent == null)
            return 0;
        return parent == node ?
                1 : parent.t.compareTo(node.t) < 0 ?
                height(parent.leftNode,node) : height(parent.rightNode,node);
    }
}
