# 扩展
## XML 转义字符
| 小于     | <    | &lt;       |
| -------- | ---- | ---------- |
| 小于等于 | <=   | `&lt;=`    |
| 大于     | >    | `&gt;`     |
| 大于等于 | >=   | `&gt;=`    |
| 不等于   | <>   | `&lt;&gt;` |
| 与       | &    | `&amp;`    |
| 单引号   | '    | `&apos;`   |
| 双引号   | "    | `&quot;`   |

## CDATA 转义区段

```xml
大于等于  <![CDATA[ >= ]]>
小于等于  <![CDATA[ <= ]]>
不等于    <![CDATA[ <> ]]>
```

# Dom4j
```java
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dongle
 * @desc
 * @since 2021/12/9 17:41
 */
public class XmlDom4jUtils {

    /**
     * 读取XML文件
     * @param file
     */
    public static void readXml(String file){
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(file));
			// XML属性及数据获取
            Element node = document.getRootElement();
            listNodeAtrr(node);
            listNodes(node);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 获取节点属性及值信息
     * @param node
     */
    private static void listNodeAtrr(Element node){
        List<Attribute> attributeList = node.attributes();
        for (Attribute attr : attributeList){
            // TODO 获取属性
            String name = attr.getName();
            String value = attr.getValue();
        }
        // TODO 获取指定属性
        Attribute atrr = node.attribute("name");
        String atVal = atrr.getValue();
    }

    /**
     * 遍历节点子节点
     * @param node
     */
    private static void listNodes(Element node){
        listNodeAtrr(node);
        Iterator<Element> nodes = node.elementIterator();
        while (nodes.hasNext()){
            // TODO 处理节点
            listNodes(nodes.next());
        }
    }
}
```