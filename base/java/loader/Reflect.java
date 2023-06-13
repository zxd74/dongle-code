package base.java.loader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 对象反射，修改属性或调用方法
 */
public class Reflect{
    public static void main(String[] args) {
        try {
            Test test = new Test();
            test.test1();
            // 获取公有属性，不含私有变量属性
            // Field[] fields = test.getClass().getFields();
            // 获取全部属性
            Field nameFiled = test.getClass().getDeclaredField("name");
            // 设置属性可访问
            nameFiled.setAccessible(true);
            nameFiled.set(test,"Dongle");
            // 获取对象中的属性值并转换指定类型
            String name = (String) nameFiled.get(test);
            System.out.println(name);
            nameFiled.setAccessible(false);

            // 获取全部方法，可指定方法名
            Method method = test.getClass().getDeclaredMethod("test");
            // 设置方法可访问
            method.setAccessible(true);
            // 获取全部共有方法，也可指定方法名
            // Method[] methods = test.getClass().getMethods();
            // 调用指定对象的方法，可加请求参数
            method.invoke(test);
            method.setAccessible(false);
        }catch (Exception ignore){}
    }

    private static class Test{
        private int id;
        private String name;

        public void test1(){
            test();
        }

        private void test(){
            System.out.println(name + " " + id);
        }
    }
}