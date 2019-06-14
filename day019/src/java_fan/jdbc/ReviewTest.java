package java_fan.jdbc;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ReviewTest {

    /**
     *
     */
    @Test
    public void testGetConnection() throws SQLException, ClassNotFoundException {
        //1.准备获取连接的4个字符串
        String user = "root";
        String password = "f199612";
        String jdbcUrl = "jdbc:mysql:///atguigu";
        String driverClass = "com.mysql.jdbc.Driver";

        //2.加载驱动

        Class.forName(driverClass);

        //3.调用
        //DriverManager。getConnection(jdbcUrl,user,password)
        //获取数据库连接

        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        System.out.println(connection);

    }


    @Test
    public void testGetConnection2() throws Exception {
        GetConnection();


    }

    private Connection GetConnection() throws IOException, ClassNotFoundException, SQLException {
        Properties info = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        info.load(in);


        //1.准备获取连接的4个字符串
        String user = info.getProperty("user");
        String password = info.getProperty("password");
        String jdbcUrl = info.getProperty("jdbcUrl");
        String driverClass = info.getProperty("driver");

        //2.加载驱动

        Class.forName(driverClass);

        //3.调用
        //DriverManager。getConnection(jdbcUrl,user,password)
        //获取数据库连接

        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        return connection;


    }
    public void releaseDB(ResultSet rs, Connection con, Statement statement) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
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
    }

    public  void releaseOB(Connection conn,Statement statement){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * Statement是操作SQL的
     */
    @Test
    public void testStatemnt() {

        //1.获取连接
        Connection conn = null;
        Statement statement = null;
        try {
            conn = GetConnection();

            //2.调用Connection对象的createStatement（）创建Statement对象

            statement = conn.createStatement();

            //3.准备SQL语句
            String sql = "INSERT INTO CUSTOMER(NAME,EMAIL,BIRTH) "+"VALUES('ABCD','1057600474@qq.com','2009-12-12')";


            //4.执行Statement.executeUpdate()

            statement.executeUpdate(sql);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源

            releaseOB(conn, statement);
        }
    }

    @Test
    public void testResultSet(){
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            //1.获取数据库连接
            conn = GetConnection();
            //2.创建Statement对象，用于执行SQL语句
            statement = conn.createStatement();
            //3.准备SQL语句
            String sql = "select id,name,email,birth from customer";
            //4.发送SQL语句：调用Statement对象的executeQuery(sql)
            resultSet = statement.executeQuery(sql);

            //5.处理结果集
            //5.1 next（）查看下一条是否有效，有则下移
            while (resultSet.next()){

                //getInt() getString() getDate()
                int id = resultSet.getInt(1);
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date birth = resultSet.getDate("birth");


                System.out.println(id);
                System.out.println(name);
                System.out.println(email);
                System.out.println(birth);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }


        releaseDB(resultSet, conn, statement);


    }

}




