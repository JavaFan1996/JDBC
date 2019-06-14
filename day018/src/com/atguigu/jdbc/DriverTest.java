package com.atguigu.jdbc;

import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Statement;
import oracle.jdbc.driver.OracleDriver;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DriverTest {
    @Test
    public void driverTest() throws SQLException {
        Driver driver = new com.mysql.jdbc.Driver();


        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "f199612");


        Connection connect = driver.connect(url, info);
        System.out.println(connect);

    }


//    @Test
//    public void test2() throws SQLException {
//        Driver driver = new com.mysql.jdbc.Driver();
//        String url = "jdbc:mysql://localhost:3306/test";
//        Properties info = new Properties();
//        info.put("user", "root");
//        info.put("password", "f199612");
//
//        Connection connect = driver.connect(url, info);
//        System.out.println(connect);
//
//
//    }


    public Connection getConnection() throws Exception {
        String driverClass = null;
        String jdbcUrl = null;
        String user = null;
        String password = null;

        //读取类路径下的 jdbc.properties 文件
        InputStream in =
                getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(in);
        driverClass = properties.getProperty("driver");
        jdbcUrl = properties.getProperty("jdbcUrl");
        user = properties.getProperty("user");
        password = properties.getProperty("password");

        //通过反射常见 Driver 对象.
        OracleDriver driver =
                (OracleDriver) Class.forName(driverClass).newInstance();

        Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);

        //通过 Driver 的 connect 方法获取数据库连接.
        Connection connection = driver.connect(jdbcUrl, info);

        return connection;
    }

    @Test
    public void testGetConnection() throws Exception {
        System.out.println(getConnection());
    }


    @Test
    public void testDriverManager() throws Exception {
        String driverClass = null;
        String jdbcUrl = null;
        String user = null;
        String password = null;

        //读取类路径下的 jdbc.properties 文件
        InputStream in =
                getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(in);
        driverClass = properties.getProperty("driver");
        jdbcUrl = properties.getProperty("jdbcUrl");
        user = properties.getProperty("user");
        password = properties.getProperty("password");

        //加载数据库驱动程序
        Class.forName(driverClass);

        Connection connection =
                DriverManager.getConnection(jdbcUrl, user, password);
        System.out.println(connection);
    }

    /**
     * DriverManager时驱动的管理类
     * 1） 可以通过重载getConnection()方法获取数据库连接，较为方便
     * 2) 可以同时管理多个应用程序：若注册了多个数据库连接，则调用getConnection()
     * 方法时传入参数不同，则返回不同的数据库连接
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Test
    public void test() throws ClassNotFoundException, SQLException {
        String driverClass = "com.mysql.jdbc.Driver";
        String driverClass1 = "oracle.jdbc.driver.OracleDriver";
        String jdbcUrl = "jdbc:mysql:///test";
        String jdbcUrl1 = "jdbc:oracle:thin:@localhost:1521/orcl";
        String user = "root";
        String user1 = "system";
        String password = "f199612";
        String password1 = "orcl";


//        加载数据库驱动程序（对应的Driver 实现类中有注册驱动的静态代码块）
        Class.forName(driverClass);
        Class.forName(driverClass1);

        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        System.out.println(connection);
        Connection connection1 = DriverManager.getConnection(jdbcUrl1, user1, password1);
        System.out.println(connection1);


    }

    /**
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */

    @Test

    public void testgetConnection2() throws SQLException, IOException, ClassNotFoundException {
//        System.out.println(getConnection2());
//        testStatement();
        update("delete from customer where id=4");


    }

    public Connection getConnection2() throws IOException, ClassNotFoundException, SQLException {
        //1)准备连接数据库的4个字符串
        //1).创建properties对象
        Properties info = new Properties();
        //2)获取jdbc.properties的输入连
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");

        //3.对应的输入流
        info.load(in);

        //4.具体决定user password
        String user = info.getProperty("user");
        String password = info.getProperty("password");
        String jdbcUrl = info.getProperty("jdbcUrl");
        String driverClass = info.getProperty("driver");

        Class.forName(driverClass);
        return DriverManager.getConnection(jdbcUrl, user, password);


    }


    /**
     * 通用的更新的方法： 包括INsert UPDATE DELETE
     * Version 1.0
     */

    public  void update(String sql)  {
        Connection conn = null;
        Statement statement = null;
        try {
            conn = JDBCTools.getConnection();
            statement = (Statement) conn.createStatement();
            statement.executeUpdate(sql);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTools.release(statement,conn);
        }



    }
    /**
     * 通过JDBC ，向指定的数据表中插入一条记录，
     * 步骤：
     * ① 获取数据库的连接
     * ② 准备SQL语句 insert delete 但不能select
     * ③ 获取操作SQL语句的Statement对象，Connection.createStatement（）
     * ④调用Statement.executeUpdate(sql)或者执行别的操作
     * <p>
     * ⑤关闭资源 statement connection 资源一定要关闭 所以要使用try-catch-finally
     * <p>
     * <p>
     * 注意：关闭的顺序是：先关闭获取的，即先关statement和connection
     */


    public void testStatement() {
        //1.获取数据库连接
        Connection conn = null;
        Statement statement = null;
        try {
            conn = getConnection2();
            //2.准备插入的sql语句

//            String sql = "INSERT INTO CUSTOMER(NAME,EMAIL,BIRTH) "+"VALUES('ABCD','1057600474@qq.com','2009-12-12')";"INSERT INTO CUSTOMER(NAME,EMAIL,BIRTH) "+"VALUES('ABCD','1057600474@qq.com','2009-12-12')";
//            String sql = "delete from customer where id=1";
            String sql = "UPDATE  customer SET  name = 'Tom' where id = 4";

            //3.执行插入


            //3.1 获取操作sql语句的statement对象 ：Connection.createStatement()
            statement = (Statement) conn.createStatement();

            //3.2 调用Statement对象的executeUpdate（sql）执行sql语句进行插入

            statement.executeUpdate(sql);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //3.关闭statement对象
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //4.关闭连接
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
