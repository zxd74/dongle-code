# 类加载器
1. 启动类加载器Bootstrap ClassLoader：
   1. 负责JDK\jre\lib目录下或-Xbootclasspath参数指定路径下能被JVM识别的所有类库
2. 扩展类加载器ExtClassLoader：
   1. sum.misc.Launcher负责加载JDK\jre\lib\ext或Java.ext.dirs变量指定路径下所有类库
3. 应用类加载器AppClassLoader：
   1. sun.misc.Launcher负责加载用户路径ClassPath指定类
4. 自定义类加载器

### 类加载方式
1. 命令行启动由JVM初始化加载
2. 通过Class.forName()动态加载
   1. 分初始化和不初始化(该初始化为变量默认值)
   2. 不初始化方式不执行静代码块static
3. 通过ClassLoader.loadClass()动态加载
   1. 不执行静态化