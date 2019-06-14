import org.junit.Test;

import java.io.IOException;
import java.sql.*;

public class MetaDataTest {


    /**
     * DatabaseMetaData是描述数据库的元数据库对象
     * 可以有connection得到
     */
    @Test
    public void testDatabaseMetaData(){

        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCTools.getConnection();
            DatabaseMetaData data = connection.getMetaData();
            //可以得到数据库本身的一些基本信息
            //数据库的版本号
            int version = data.getDatabaseMajorVersion();
            System.out.println(version);

            //连接到数据库的用户名
            String user = data.getUserName();
            System.out.println(user);

            //得到mysql的数据库
            resultSet = data.getCatalogs();
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));

            }



        }catch (Exception e){
            e.printStackTrace();

        }finally {
            JDBCTools.releaseDB(resultSet,null,connection);
        }

    }

    /**
     * ResultSetMetaData:描述结果集的元数据
     * 可以得到结果集中的基本信息：结果集中有那些列 列名 列的别名等
     * 别名可以是对应对象的属性名这样就可以直接赋值
     */

    @Test
    public void testResultSetMetaData(){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCTools.getConnection();
            String sql = "SELECT id myid,name myname,email myemail FROM customer";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                //列名
                String columnName = rsmd.getColumnName(i + 1);
                //列的别名
                String columnLable = rsmd.getColumnLabel(i + 1);
                System.out.println(columnName+","+columnLable);
            }

        }catch (Exception e){
            e.printStackTrace();

        }




    }



}
