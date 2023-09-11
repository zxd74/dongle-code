package language.java.loader;
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