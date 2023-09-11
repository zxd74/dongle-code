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
