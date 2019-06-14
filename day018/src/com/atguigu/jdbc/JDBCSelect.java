package com.atguigu.jdbc;

import org.junit.Test;

import java.io.IOException;
import java.sql.*;

public class JDBCSelect {
    /**
     * ResultSet:结果集，邓庄了使用JDBC进行查询的结果
     * 调用statement对象的executeQuery（sql）可以得到结果i
     * ResultSet返回的实际上就是一张数据表，有一个指针指向数据表的第一行的前面
     * 可以调用next（）方法是否有效，若有效该方法返回true，切指针下移
     * 相当于while（iteror.hasNext() 与next（）的结合）、
     * 3.当指针对位到一行时，可以通过调用getXxx（index）或者getXxx(columName)
     * getInt(1)
     * 4.ResultSet也需要关闭
     */
    @Test
    public void testResultSet() {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCTools.getConnection();
            statement = conn.createStatement();
            String sql = "select id,name,email,birth from customer ";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date birth = resultSet.getDate(4);

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
            JDBCTools.release2(resultSet, statement, conn);

        }
    }
}