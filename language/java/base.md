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