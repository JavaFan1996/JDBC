package com.atguigu.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCTools {

    /**
     * g同时关闭resultset
     *
     * @param statement
     * @param conn
     */


    public static void release2(ResultSet re, Statement statement, Connection conn) {
        if (statement != null) {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (re != null) {
            try {
                re.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {

                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 关闭statement 和connection
     *
     * @param statement
     * @param conn
     */
    public static void release(Statement statement, Connection conn) {
        if (statement != null) {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {

                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 1.获取连接的方法
     * 通过读取配置文件
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        //1)准备连接数据库的4个字符串
        //1).创建properties对象
        Properties info = new Properties();
        //2)获取jdbc.properties的输入连
        InputStream in = JDBCTools.class.getClassLoader().getResourceAsStream("jdbc.properties");

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
}