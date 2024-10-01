# File工具代码库

## XML代码库

### W3C
    解析整个DOM到内存，支持对整个文件访问和修改，但占用内存大，不适用于大型文件解析

* `ClassLoader#getResourceAsStream` 读取资源文件
* `InputStream` 资源流
* `DocumentBuilderFactory`:`DocumentBuilder(DocumentBuilderImpl)`构建工厂
* `Document`: 由`DocumentBuilder`通过资源流构建的文档
* `NodeList`: 节点列表（单链，不能使用`foreach`）
* `Node`: 节点
```java
    public static void main(String[] args) throws Exception{
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null){
            System.out.println("cl not found");return;
        }
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("dongle.xml");
        if (is == null) {System.out.println("IS not found");return;}

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(false);
        factory.setCoalescing(false);
        factory.setExpandEntityReferences(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is); // 初始化时，整个文件为头部节点
        NodeList nodeList = document.getChildNodes();
        if (nodeList == null){
            System.out.println("node not found");
            return;
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
        }
    }
```
### SAX
    只读不可修改，占用内存少，适用于读取大型XML文件
* `ClassLoader,InputStream`: 加载xml文件资源流
* `SAXParserFactory`: SAX解析器工厂
* `SAXParser`: SAX解析器
* `DefaultHandler`: SAX默认解析处理器(只定义方法，但无实现)
  * `SAXParserHandler`: 自定义处理器(实现对数据的解析)
```java
public class Main {
    public static void main(String[] args) throws Exception{
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream("dongle.xml");
        // org.xml.sax..
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        SAXParserHandler handler = new SAXParserHandler();
        parser.parse(is,handler);
        // 根据需要从handler中获取处理结果
    }

}

/**
 * 自定义处理器
 */
class SAXParserHandler extends DefaultHandler { // DefaultHandler定义方法，但都未实现
    Map<String,String> map = new HashMap<>();

    public Map<String, String> getMap() {
        return map;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes); // 节点开始时的数据处理
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName); // 节点结束时的数据处理
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length); // 字符处理
    }
}
```

### 第三方
* dom4j实现：org.dom4j:dom4j:2.1.3
* JDOM:`org.jdom:jdom:2.0.6`

## Excel

### 依赖
* poi实现(xls)：`org.apache.poi:poi:5.0.0`
* poi实现(xlsx)：`org.apache.poi:poi-ooxml:5.0.0`
* easy-poi实现：`cn.afterturn:easypoi-base(easypoi-web[可无]、easypoi-annotation\):4.4.0`
* easyexcel实现: `com.alibaba:easyexcel:3.0.5`

#### 说明
1. csv文件可使用java自带的Reader类处理(java7+)
2. poi对于每行最后一列或者几列若数据为空值就会导致读取列与实际列不一致问题，请谨慎！
2. easy-poi可通过注解列明直接转对象数据
3. easyexcel亦可绑定列
