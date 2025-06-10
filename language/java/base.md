# 基础
## 控制台输入输出
* `System.in`: 标准输入流，默认从键盘输入
* `System.out`: 标准输出流，默认输出到控制台
* `Scanner`: 用于从控制台读取输入
* `System.out.println()`: 输出到控制台并换行
* `InputStreamReader`: 将InputStream转换为Reader
* `OutputStreamWriter`: 将OutputStream转换为Writer
```java
    Scanner scanner = new Scanner(System.in);
    while (true){
        System.out.println("请输入任意内容（输入exit就退出，否则继续输入）：");
        String content = scanner.nextLine();
        if (content.equals("exit")){
            break;
        }
        System.out.println("你输入的内容是：" + content);
    }   
```

# 克隆
* **浅克隆**：创建新对象及地址，但内部引用属性仍未原来对象的属性对象
  * 实现`Cloneable`接口，(**默认浅克隆**)，当内部属性为**可变数据类型**时，属于浅克隆
    * 因`ArrayList`是可变对象，故克隆后，list引用同一个对象
```java
class Dongle implements Cloneable{
    ArrayList<String> list = new ArrayList<>(); 
    
    // 当只有基本数据类型或不可变对象时，效果类似深克隆
    @Override
    public Dongle clone() throws CloneNotSupportedException {         
        return (Dongle) super.clone(); // 默认super.clone，是浅克隆， list引用同一个对象
    }
}
```
  * 当内部属性为**基本数据类型和不可变数据类型时，浅克隆效果等同于深克隆**
    * 因`int`为基本数据类型，`String`为不可变对象，故克隆后，属性引用不同
```java
static class Dongle implements Cloneable{

    private int id;          // 基本数据类型 - 会被复制值
    private String name;     // 不可变对象 - 引用被复制但安全

    @Override
    public Dongle clone() throws CloneNotSupportedException {// 当只有基本数据类型或不可变对象时，效果类似深克隆
        return (Dongle) super.clone();  // 默认super.clone，是浅克隆，但本例效果等同于深克隆
    }
}
```

* **深克隆**：内部引用属性也是新对象
  * 通过**限定类型**: 属性数据类型为基本数据类型或不可变数据类型，达到深克隆效果
  * 通过**重写clone()方法**实现
```java
static class Dongle implements Cloneable{
    // 因String为不可变，故达到深克隆效果，若为可变对象，则仍需递归继续创建新对象属性
    ArrayList<String> list = new ArrayList<>(); 

    @Override
    public Dongle clone() throws CloneNotSupportedException {
        Dongle clone = (Dongle) super.clone();
        deepClone(clone); // 深克隆自实现逻辑
        return clone;
    }

    private void deepClone(Dongle clone){
        clone.list = new ArrayList<>(this.list); // 创建新集合
        // 其它数据属性深克隆处理
    }
}
```
  * 通过**拷贝构造器**实现
```java
static class Dongle implements Cloneable{
    // 因String为不可变，故达到深克隆效果，若为可变对象，则仍需递归继续创建新对象属性
    ArrayList<String> list = new ArrayList<>(); 

    public Dongle(){}
    public Dongle(Dongle dongle){
        // this.list = new ArrayList<>(dongle.list); // 通过构造函数实现
        deepClone(this); // 深克隆自实现逻辑
    }

    private void deepClone(Dongle clone){
        clone.list = new ArrayList<>(this.list); // 创建新集合
        // 其它数据属性深克隆处理
    }
}
```
  * 通过**序列化**实现(**推荐，最彻底的深拷贝**)
```java
class Dongle{
    // 因String为不可变，故达到深克隆效果，若为可变对象，则仍需递归继续创建新对象属性
    ArrayList<String> list = new ArrayList<>(); 

    public Dongle deepClone() throws IOException,ClassNotFoundException{ // 序列化实现，推荐，最彻底深拷贝
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (Dongle)ois.readObject();
    }
}
```
  * 通过第三方工具实现：实际底层仍序列或或者递归至基本数据类型和不可变数据类型为止
* `Effective Java`建议
  *  谨慎使用 `Cloneable`，最好提供**拷贝构造器**或**静态工厂方法**
```java
// 拷贝构造器方案
public MyClass(MyClass another) { /*...*/ }

// 静态工厂方案
public static MyClass newInstance(MyClass another) { /*...*/ }
```
  * 如果必须实现 `Cloneable`，则：
    * 重写 `clone()` 为 `public`
    * 确保实现深拷贝
    * 文档中明确说明克隆行为

* **最佳实践**
  * **优先选择不可变对象**：避免克隆复杂性
  * **对于集合类**：使用 Collections.unmodifiableList() 防御性编程
  * **复杂对象图**：考虑使用第三方库如 Apache Commons Lang 的 SerializationUtils.clone()
  * **记录克隆行为**：在类文档中明确说明是浅克隆还是深克隆
