package base.practice.structure.tree;

/**
 * @author Dongle
 * @desc
 * @since 2022/3/23 9:45
 */
public interface Tree<T>{

    void insert(T t);
    boolean contain(T t);
    void remove(T t);
    int size();
    int depth();
    int height(T t,T t1);

}
