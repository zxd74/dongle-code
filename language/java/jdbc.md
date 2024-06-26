# SQL语言
1. DDL 数据定义语言
2. DML 数据操作语言
3. DCL 数据控制语言
4. TCL 事务控制语言
# JDBC 
Java DataBase Connectivity,一种用于执行SQL语句的Java API，链接数据库和Java应用程序的纽带
## 步骤
1. 加载驱动Driver
2. 创建链接对象Connection
3. 发送SQL：Statement
```java
// 本实例采用odbc驱动，
Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
// 数据源格式：  jdbc:odbc:数据源
Connection conn = DriverManager.getConnection("jdbc:odbc:数据源","user name","password");
Statement sql = conn.createStatement();

// 其他数据源仿照obdc，如mysql驱动（jdbc:mysql:数据源）
```
## 组成
1. Connection 接口：数据库连接接口
2. Statement 接口：用于在已建立连接的基础上向数据库发送SQL语句
    1. Statement：执行不带参数的SQL语句
    2. PreparedStatement：执行带参数的SQL语句
    3. CallableStatement：继承PreparedStatement，执行存储过程
3. DriverManager 类：管理数据库中的所有驱动程序
4. ResultSet 接口：类似临时表，暂存sql执行结果

# 驱动
1. JDBC：sun.jdbc.odbc.JdbcOdbcDriver
2. MYSQL: com.mysql.jdbc.Driver

# 示例
```java
package language.java.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 1. 加载驱动
 * 2. 创建连接
 * 3. 执行SQL
 * 4. 接收结果
 */
public class JdbcDemo {
    static Connection conn;
    public static Connection getConnection(){
        try {
            // 加载JDBC驱动
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test","test","123456");
        }catch (Exception ignore){}
        return conn;
    }
    
    public static ResultSet execute(){
        ResultSet resultSet = null;
        try {
            // 创建Statement，用于执行SQL语句
            Statement sql = conn.createStatement();
            // 接受执行结果
            resultSet = sql.executeQuery("select  * from users");
        }catch (Exception ignore){}
        return resultSet;
    }

    /**
     * 事务处理
     */
    public static void executeTransication(){
        try{
            conn.setAutoCommit(false); // 默认自动
            // 设置事务级别
            conn.setTransactionIsolation(Connection.TRANSACTION_NONE);
            Statement sql = conn.createStatement();
            sql.executeUpdate("");
            conn.commit();
            conn.setAutoCommit(true);
        }catch(Exception ex){
            try{
                conn.rollback(); // 触发回滚
            }catch (Exception eex){
                eex.printStackTrace();
            }
        }
    }
}
```