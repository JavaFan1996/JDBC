import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

public class JDBCTest {

    /**
     * 插入BLOB类型的数据必须使用preparestatement：因为无法用字符串拼接
     * 调用： preparedStatement.setBlob(4, inputStream);
     *
     *
     */
    @Test
    public void testInsertBlob() {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JDBCTools.getConnection();
            String sql = "INSERT INTO customer(name,email,birth,picture) VALUES(?,?,?,?)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "AABBC");
            preparedStatement.setString(2, "1057600474@qq.com");
            preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
            //插入BLOB
            InputStream inputStream = new FileInputStream("桌面.png");
            preparedStatement.setBlob(4, inputStream);


            preparedStatement.executeUpdate();
//
            //

        } catch (Exception e) {
            e.printStackTrace();

        } finally {


            JDBCTools.releaseDB(null, preparedStatement, connection);
        }

    }

    /**
     * 读取blob
     * Blob picture = resultSet.getBlob(5);
     * InputStream in = picture.getBinaryStream();
     */
    @Test
    public void readBlob() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCTools.getConnection();
            String sql = "SELECT id,name customerName,email,birth,picture FROM customer WHERE id = 15";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                System.out.println(id + "," + name + "," + email);

                Blob picture = resultSet.getBlob(5);
                InputStream in = picture.getBinaryStream();
                OutputStream out = new FileOutputStream("wode.png");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.close();
                in.close();

            }


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            JDBCTools.releaseDB(resultSet, preparedStatement, connection);
        }


    }

    /**
     * 取得数据库自动生成的主键
     */
    @Test
    public void testGetKeyValue() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JDBCTools.getConnection();
            String sql = "INSERT INTO customer(name,email,birth) VALUES(?,?,?)";
//            preparedStatement = connection.prepareStatement(sql);
            //使用重载的方法prepareStatement（SQL，flag）生成对象
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, "AABBC");
            preparedStatement.setString(2, "1057600474@qq.com");
            preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));


            preparedStatement.executeUpdate();
            //通过getGenerateKeys()获取包含了新生成主键的ResultSet对象
            //在ResultSet中只有一列。Genreate_key，用于存放新生成的主键值
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                System.out.println(rs.getObject(1));

            }

            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                System.out.println(rsmd.getColumnName(i + 1));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {


            JDBCTools.releaseDB(null, preparedStatement, connection);
        }

    }

}
