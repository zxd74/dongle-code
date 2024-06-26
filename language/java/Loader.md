# 类加载器
1. 启动类加载器Bootstrap ClassLoader：
   1. 负责JDK\jre\lib目录下或-Xbootclasspath参数指定路径下能被JVM识别的所有类库
2. 扩展类加载器ExtClassLoader：
   1. sum.misc.Launcher负责加载JDK\jre\lib\ext或Java.ext.dirs变量指定路径下所有类库
3. 应用类加载器AppClassLoader：
   1. sun.misc.Launcher负责加载用户路径ClassPath指定类
4. 自定义类加载器

## 类加载方式
1. 命令行启动由JVM初始化加载
2. 通过Class.forName()动态加载
   1. 分初始化和不初始化(该初始化为变量默认值)
   2. 不初始化方式不执行静代码块static
3. 通过ClassLoader.loadClass()动态加载
   1. 不执行静态化


```java
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
```
```java
import java.io.InputStream;
import java.util.Properties;
/**
 * 加载配置文件
 */
public class LoaderResource {
    public static void main(String[] args) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        
        try {
            // 相对地址默认位于classpath目录下(即resources目录)或绝对地址
            // 资源地址
            String resource_path = "services";
            InputStream is = loader.getResourceAsStream(resource_path);
            // 配置属性
            Properties configProperties = new Properties();
            configProperties.load(is);
            System.out.println(configProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
```java
import java.io.*; 
/**
 * 自定义类加载器
 */
public class MyClassLoader extends ClassLoader {
    private String root;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(name, classData, 0, classData.length);
        }
    }

    private byte[] loadClassData(String className) {
        String fileName = root + File.separatorChar
                + className.replace('.', File.separatorChar) + ".class";
        InputStream ins;
        try {
            ins = new FileInputStream(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}
```
# 反射
```java
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
```

# 代理
## 静态代理
1. 定义通用接口
2. 定义接口实现类
3. 定义一个实现类的代理类
4. 使用时，通过代理类调用接口方法
```java
public interface Subject {
    void request();
}
public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("RealSubject");
    }
}
public class RealProxy implements Subject {
    private Subject subject;
    public RealProxy(Subject subject) {
        this.subject = subject;
    }
}
    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        RealProxy proxy = new RealProxy(realSubject);
        proxy.request();
        // 输出：RealSubject
    }
```
## 动态代理
### JDK动态代理
1. 定义通用服务接口
2. 定义接口实现类
3. 定义服务调用处理器，实现`InvocationHandler`接口
4. 使用`Proxy.newProxyInstance`方法创建代理对象: 需要用真实对象的类加载器，接口数组，处理器实例
5. 使用代理对象调用接口方法
```java
interface Service {
    void perform();
}

class ServiceImpl implements Service {
    public void perform() {
        System.out.println("Service performed.");
    }
}
class ServiceInvocationHandler implements InvocationHandler {
    private Object target;

    public ServiceInvocationHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before service");
        Object result = method.invoke(target, args);
        System.out.println("After service");
        return result;
    }
}

    public static void main(String[] args) {
        Service service = new ServiceImpl();
        Service proxy = (Service) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new ServiceInvocationHandler(service)
        );
        proxy.perform();
    }
```
### CGLIB动态代理
1. 定义普通类()
2. 定义方法拦截器，实现`MethodInterceptor`接口（cglib库）
3. 通过`Enhancer`类创建代理对象
4. 使用代理对象调用方法
```java
class Service {
    public void perform() {
        System.out.println("Service performed.");
    }
}
class ServiceInterceptor implements MethodInterceptor {
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = proxy.invokeSuper(obj, args);
        return result;
    }
}
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Service.class);
        enhancer.setCallback(new ServiceInterceptor());
        Service proxy = (Service) enhancer.create();
        proxy.perform();
    }
```