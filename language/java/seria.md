# 序列化
```java
public class DongleSerial implements Serializable {
    private String name;
    private transient int age;
    private int sex;
}
```
```java
    @Test
    public void test(){
        DongleSerial dongle = new DongleSerial();
        dongle.setAge(20);
        dongle.setName("Dongle");
        dongle.setSex(1);
        try {
            FileOutputStream fos = new FileOutputStream(new File("D://dongle.d"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dongle);
            oos.close();
            fos.close();
        }catch (NotSerializableException ex){
            ex.printStackTrace();
            System.out.println("序列化失败");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void test1(){
        try {
            FileInputStream fis = new FileInputStream(new File("D://dongle.d"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            DongleSerial dongle = (DongleSerial) ois.readObject();
            System.out.println("name:" + dongle.getName());
            System.out.println("age:" + dongle.getAge());
            System.out.println("sex:" + dongle.getSex());
        }catch (ClassNotFoundException ex){
            ex.printStackTrace();
            System.out.println("反序列化未找到");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
```