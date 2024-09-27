Starting `Hibernate 6`, the default JPA provider packages have been moved from `javax.*` to `jakarta.*`.

# 纯Java
    以MYSQL+Hibernate为例。

1. 添加依赖
    ```xml
        <!-- hibernate -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.6.15.Final</version>
        </dependency>
        <!-- 数据库相关 -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.1.0</version>
        </dependency>
    ```
2. 在mysql中创建数据库(略)：`test.users`
3. 配置`hibernate.cfg.xml`
```xml
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <!-- JDBC Database connection settings -->
        <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="connection.url">jdbc:mysql://db.dongle.com:3306/test?useSSL=false</property>
        <property name="connection.username">root</property>
        <property name="connection.password">Dongle@123</property>

        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>

        <!-- JDBC connection pool settings ... using built-in test pool -->
        <property name="connection.pool_size">1</property>

        <!-- Echo the SQL to stdout -->
        <property name="show_sql">true</property>

        <!-- Set the current session context -->
        <property name="current_session_context_class">thread</property>

        <!-- dbcp connection pool configuration -->
        <property name="hibernate.dbcp.initialSize">5</property>
        <property name="hibernate.dbcp.maxTotal">20</property>
        <property name="hibernate.dbcp.maxIdle">10</property>
        <property name="hibernate.dbcp.minIdle">5</property>
        <property name="hibernate.dbcp.maxWaitMillis">-1</property>

        <!-- TODO 实体映射配置 -->
    </session-factory>
</hibernate-configuration>
```
4. 创建实体类`Users`及映射配置
   * 方式一：通过JPA 注解`@Entity`关联实体与数据库，并配置映射关系
    ```java
    @Entity
    @Table(name="users")
    public class Users{
        @Id
        private int id;
        @Column
        private String username;
        @Column
        private int age;
        @Column 
        private int sex;
        // getter/setter
    }
    ```
    ```xml
    <!-- hibernate.cfg.xml -->
        <!-- 使用@Entity注解声明的Entity可直接映射类即可 -->
        <mapping class="com.xxx.Users" />
    ```
   * 方式二：不使用`@Entity`声明的，需要在配置文件中声明映射关系
    ```java
    public class Users{
        private int id;
        private String username;
        private int age;
        private  int sex;
        // getter/setter
    }
    ```
    ```xml
    <!-- hibernate.cfg.xml -->
        <!-- 当使用非@Entity注解配置实体类时，需要通过xml配置实体映射-->
        <mapping resource="hbm.xml"/>
    ```
    ```xml
    <!-- hbm.xml -->
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
    <hibernate-mapping>
        <class name="com.xxx.Users" table="users">
            <id name="id" column="id" />
            <property name="username" column="username"/>
            <property name="age" column="age"/>
            <property name="sex" column="sex"/>
        </class>
    </hibernate-mapping>
    ```
5. 使用Hibernate操作数据库
```java
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        Session session =  sessionFactory.openSession();
        // 查询
        List<Users> users = session.createQuery("FROM Users",Users.class).list();
        users.forEach(u-> System.out.println(u.getId() + " " + u.getUsername() + " " + u.getAge() + " " + u.getSex()));

        // 插入  当修改操作过多时，建议使用事务
        Random random = new Random();
        session.beginTransaction();
        for (int i = 0; i < 5; i++) {
            Users user = new Users();
            user.setId(i);
            user.setUsername("Dongle_" + i);
            user.setAge(random.nextInt(150));
            user.setSex(random.nextInt(2));
            session.save(user);
        }
        session.getTransaction().commit();

        session.close();
    }
```

### 纯代码
```java
public static void main(String[] args) {
        SessionFactory sessionFactory;
        Configuration configuration = new Configuration();

        configuration.setProperty( "dialect", "org.hibernate.dialect.MySQLDialect" )
                .setProperty("show_sql",true)
                .setProperty("hibernate.connection.driver_class","com.mysql.cj.jdbc.Driver")
                .setProperty("hibernate.connection.url","xxx")
                .setProperty("hibernate.connection.username","root")
                .setProperty("hibernate.connection.password","xxx")
                .addAnnotatedClass(Users.class);

        sessionFactory = configuration.buildSessionFactory();
        Session session =  sessionFactory.openSession();
        List<Users> users = session.createQuery("FROM Users", Users.class).list();
        users.forEach(u-> System.out.println(u.getId() + " " + u.getUsername() + " " + u.getAge() + " " + u.getSex()));

        session.close();
    }
```

## Hibernate  6.x及以上
    Starting `Hibernate 6`, the default JPA provider packages have been moved from `javax.*` to `jakarta.*`.

1. 管理依赖
```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.hibernate.orm</groupId>
                <artifactId>hibernate-platform</artifactId>
                <version>6.6.1.Final</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
        </dependency>
        <dependency>
            <groupId>jakarta.transaction</groupId>
            <artifactId>jakarta.transaction-api</artifactId>
        </dependency>


        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.1.0</version>
        </dependency>
    </dependencies>
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