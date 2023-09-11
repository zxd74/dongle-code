package language.java.practice.structure.tree;

import javax.validation.constraints.NotNull;

/**
 * @author Dongle
 * @desc
 * @since 2022/3/23 10:07
 */
public class BNode<T> extends AbstractNode<T>{

    T t;
    protected BNode<T> parent;
    protected BNode<T> leftNode;
    protected BNode<T> rightNode;

    public BNode(@NotNull T t) {
        super(t);
        this.t = t;
    }
}
