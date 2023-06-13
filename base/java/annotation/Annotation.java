package base.java.annotation;

public class Annotation{


}

/**
 * FunctionalInterface 定义方法接口 ：接口有且只有一个方法时，可使用lambda表达式实例化接口
 *  JDK 1.8+ 支持
 */
@FunctionalInterface
interface DongleInterface{
    DongleInterface DONGLE = (tmp)->{
        return tmp;
    };

    String kevin(String tmp);

    static void dk(String tmp){
        System.out.println("dk method");
    }
}