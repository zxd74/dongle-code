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