# 集成
## Spring环境
1. 依赖JPA：Spring环境JAP或Spring Boot环境Jap starter
   ```xml
   <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
		<version>2.7.8</version>
	</dependency>
   ```
2. 依赖数据库驱动如mysql
   ```xml
   <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.49</version>
    </dependency>
   ```
3. 定义Entity实体
```java
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "consumer")
@Entity
public class User {

    @Id
    @GeneratedValue()
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

}
```
4. 定义Repository(Dao)接口，集成Repository或其子接口(并用@Repository注解注释)，
```java
import com.dongle.sys.order.dao.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User,Long> {

    
}
```
5. 在接口自定义其他sql处理，使用@Query定义SQL语句
```java
    @Query(value = "select id,`name`,age from consumer where `name` = ?1",nativeQuery = true)
    List<User> queryByName(String name);
```
6. 包扫描配置@EnableJpaRepositories自定义Repository接口包地址和@EntityScan实体包地址
```java
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.dongle.sys.order.dao.mapper"})
@EntityScan(basePackages = {"com.dongle.sys.order.dao.entity"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
7. 配置数据库信息及其他jpa扩展信息等
```properties
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    username: root
    password: Dongle@123
    url: jdbc:mysql://db.dongle.com:3306/dongle-order?useUnicode=true&characterEncodeing=utf8&autoReconnect=true&useSSL=false
  jpa:
    show-sql: true  # 展示sql语句
```

## 多列主键
1. 定义主键Key类，需要实现序列化
2. 实体定义增加@IdClass修饰并关联主键Key类.class
3. JPA接口关联ID为主键Key类
```java
@Data
public class UserKey implements Serializable {
    private Long id;
    private String name;
}
```
```java
@Data
@Table(name = "user")
@IdClass(UserKey.class)
@Entity
public class User {

    @Id
    private Long id;
    @Id
    private String name;
    private int age;
}
```
```java
public interface UserKeyDao extends CrudRepository<User,UserKey> {}
```

## 语句
1. 所有执行需要使用`@Query`的`value(sql)`
2. 如果使用原生sql，需要将`@Query`中的`nativeQuery`设置为true(默认false)
3. 修改语句需要增加`@Modifying`修饰

### 传参
* `?` 根据参数位置传参，如`?!,?2`分别代表第一、第二个参数
* `:` 可以直接绑定参数名传参,如`:user.id`

### 查询@Query
```java
    @Query(value = "select * from `user`")
    List<User> findAll();

    @Query(value = "select name from `user`",nativeQuery = true)
    List<String> findAllName();
```
### 删除 @Query + @Modifying

```java
    @Query(value = "delete from `user` where `id`=?1",nativeQuery = true)
    @Modifying
    void deleteById(int id);
```
