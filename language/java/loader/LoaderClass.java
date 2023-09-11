package language.java.loader;
/**
 * 类加载方式
 */
public class LoaderClass {
    public static void main(String[] args) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            // loader.loadClass不执行变量初始化和静态代码块
            Class cl = loader.loadClass(Demo.class.getClass().getName());

            // 默认执行静态代码块及变量初始化
            cl = Class.forName(Demo.class.getName());

            // 初始化false不执行静态代码块及变量初始化
            cl = Class.forName(Demo.class.getName(),false,loader);

            // 创建类实例
            Demo demo = (Demo) cl.newInstance();
            System.out.println(demo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Demo{

    }
}
