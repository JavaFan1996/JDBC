package java_fan.jdbc;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ForkJoinPool;

public class JDBCTools {
    /**
     * 执行 SQL 语句, 使用 PreparedStatement
     * @param sql
     * @param args: 填写 SQL 占位符的可变参数
     */
    public static void update(String sql, Object ... args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for(int i = 0; i < args.length; i++){
                preparedStatement.setObject(i + 1, args[i]);
            }

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            JDBCTools.releaseDB(null, preparedStatement, connection);
        }
    }

    /**
     * 执行 SQL 的方法
     *
     * @param sql: insert, update 或 delete。 而不包含 select
     */
    public static void update(String sql) {
        Connection connection = null;
        Statement statement = null;

        try {
            // 1. 获取数据库连接
            connection = getConnection();

            // 2. 调用 Connection 对象的 createStatement() 方法获取 Statement 对象
            statement = connection.createStatement();

            // 4. 发送 SQL 语句: 调用 Statement 对象的 executeUpdate(sql) 方法
            statement.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 关闭数据库资源: 由里向外关闭.
            releaseDB(null, statement, connection);
        }
    }

    /**
     * 释放数据库资源的方法
     *
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void releaseDB(ResultSet resultSet, Statement statement,
                                 Connection connection) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取数据库连接的方法
     */
    public static Connection getConnection() throws IOException,
            ClassNotFoundException, SQLException {
        // 0. 读取 jdbc.properties
        /**
         * 1). 属性文件对应 Java 中的 Properties 类 2). 可以使用类加载器加载 bin 目录(类路径下)的文件
         */
        Properties properties = new Properties();
        InputStream inStream = ReviewTest.class.getClassLoader()
                .getResourceAsStream("jdbc.properties");
        properties.load(inStream);

        // 1. 准备获取连接的 4 个字符串: user, password, jdbcUrl, driverClass
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String jdbcUrl = properties.getProperty("jdbcUrl");
        String driverClass = properties.getProperty("driver");

        // 2. 加载驱动: Class.forName(driverClass)
        Class.forName(driverClass);

        // 3. 调用
        // DriverManager.getConnection(jdbcUrl, user, password)
        // 获取数据库连接
        Connection connection = DriverManager.getConnection(jdbcUrl, user,
                password);
        return connection;
    }




    @Test
    public  void testTwoSum(){
        int[] nums = new int[]{2, 7, 11,2, 15};
        int target = 9;
        for (int i = 0;i<nums.length-1; i++) {
            for (int j = 1 ; j <= nums.length-1;j++){
                if (nums[i] + nums[j] == target) {
                    if (i > j){
                        break;
                    }
                    System.out.println("["+i+" "+j+"]");
                }
            }
        }


    }

}