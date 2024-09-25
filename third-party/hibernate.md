# 纯Java
1. 添加依赖：mysql，hibernate
```xml
		<!-- 数据库相关 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.4.32.Final</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
```
2. 在mysql中创建数据库(略)
3. 创建实体类关联数据库表 
   1. 采用JPA注解: `@Entity,@Table,@Id,@Column,...`
   2. 可选：采用lombok自动生成getter/setter
```java
@Data
@Entity
@Table(name="users")
public class Users{
    @Id
    private int id;
    @Column
    private String username;
    @Column
    private int age;
    @Column int sex;
}
```
4. 使用hibernate操作数据库
```java


```

# Spring boot
1. 添加依赖: mysql,spring-jpa(**底层默认hibernate实现**)
```xml
		<!-- 数据库相关 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
```
2. 数据库配置
```xml
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://<mysql数据库地址>:3306/<数据库名>
spring.datasource.username=<数据库用户名>
spring.datasource.password=<数据库用密码>
 
spring.jpa.database=MYSQL
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
3. 在mysql中创建数据库(略)
4. 创建实体类，与数据库表对应
   1. 采用JPA注解: `@Entity,@Table,@Id,@Column,...`
   2. 可选：采用lombok自动生成getter/setter
```java
@Data
@Entity
@Table(name="users")
public class Users{
    @Id
    private int id;
    @Column
    private String username;
    @Column
    private int age;
    @Column int sex;
}
```
5. 创建Repository接口(DAO)，继承JpaRepository
```java
public interface UserRepository extends JpaRepository<Users,Integer>{
    List<Users> findByAge(int age);
}
```
6. 在Service中注入Repository，并使用其方法(后续略)