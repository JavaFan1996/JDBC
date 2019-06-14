import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import selfTest.Customer;
import selfTest.ReflectionUtils;
import selfTest.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO {

    public void update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            JDBCTools.releaseDB(null, preparedStatement, connection);
        }


    }

    public <T> T get(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData = null;
        T entity = null;

        try {

            connection = JDBCTools.getConnection();

            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

           if (resultSet.next()) {

               Map<String, Object> values = new HashMap<String,Object>();
               ResultSetMetaData rsmd = resultSet.getMetaData();

               //多少列
               int columnCount = rsmd.getColumnCount();

               for (int i = 0; i < columnCount; i++) {
                   String columnLable = rsmd.getColumnLabel(i + 1);
                   Object columnValue = resultSet.getObject(i + 1);

                   values.put(columnLable, columnValue);
               }
               entity = clazz.newInstance();

               for (Map.Entry<String, Object> entry : values.entrySet()) {
                   String propertyName = entry.getKey();
                   Object value = entry.getValue();


                   ReflectionUtils.setFieldValue(entity, propertyName, value);
               }

           }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(resultSet, preparedStatement, connection);
        }
        return entity;


    }

    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<T> result  = null;

        try {

            //1.获取数据库连接
            //连接connection 需要四个字段：driverClass user password jdbcUrl
            // 需要用到driverManager。getConnection,来获取连接
            connection = JDBCTools.getConnection();
            //2.对已经传入的sql进行操作需要用到Statement，perpareStatement是其子接口，可以安全
            preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
            //3.传入的sql中有i个占位符，需要用循环来将传入的args[]中的元素，进行填充
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //4.执行sql语句，则得到结果集，结果集就是类型数据表的文件，有列明即对应clazz类对象的属性名，和值即对应其属性的值
            resultSet = preparedStatement.executeQuery();


            //5.创建一个list其中存放着每个记录的属性名和值，一个mao就对应一个记录
            List<Map<String, Object>> values = new ArrayList<>();
            //6.创建一个list 其中存放的是列名即属性名
            List<String> columLables = new ArrayList<>();
            //resultSet.getMeteData 获取元数据
            ResultSetMetaData rsmd = resultSet.getMetaData();
            //7.rsmd.getColumLable（）获取列明，然后再用for循环将一个个列名（属性名）添加到list（columLables）里面
            for (int i = 0; i < rsmd.getColumnCount();i++) {
                columLables.add(rsmd.getColumnLabel(i + 1));
            }
            //此时的ColumLables里面就存放着所有的列名
            Map<String, Object> map = null;
            //8.因为是获取多条记录，所有要用while循环来讲所有的列名和值添加到values（List）中
            while (resultSet.next()) {
                map = new HashMap<>();
                //8.遍历ColumLabes，填充map
                for (String columnLable : columLables) {

                    Object value = resultSet.getObject(columnLable);
                    map.put(columnLable, value);

                }
                //8.2填充values
                values.add(map);
            }
            //此时的values（List）是key为属性名 value为值的list（对应多条记录）


            //9.讲valuse转化为存放对应T类型对象的list
            result = new ArrayList<>();
            //10.创建一个T类型的对象，
            T bean = null;
            if (values.size() > 0) {
                for(Map<String, Object> m:values){
                    bean = clazz.newInstance();
                    for(Map.Entry<String, Object> entry:m.entrySet()){
                        String properName = entry.getKey();
                        Object value = entry.getValue();

                        BeanUtils.setProperty(bean, properName, value);
                    }
                    result.add(bean);
                }

            }



        } catch (Exception e) {
            e.printStackTrace();

            // TODO: handle exception
        } finally {
            JDBCTools.releaseDB(resultSet, preparedStatement, connection);

        }
        return result;
    }

    public <E> E getForValue(String sql, Object... args) {
        //1. 得到结果集: 该结果集应该只有一行, 且只有一列
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1. 得到结果集
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return (E) resultSet.getObject(1);
            }
        } catch(Exception ex){
            ex.printStackTrace();
        } finally{
            JDBCTools.releaseDB(resultSet, preparedStatement, connection);
        }
        //2. 取得结果

        return null;
    }



    @Test
    public void testupdate(){

        String sql = "INSERT INTO customer(name," + "" +
                "email,birth)VALUES(?,?,?) ";
        update(sql, "fanzhiqiang", "1057600474", new Date(new java.util.Date().getTime()));
    }




    @Test
    public void testGet(){
        String sql = "SELECT flowid flowId,type type,idCard idCard,examCard,examCard,studentName,studentName,location,location,grade,grade FROM examstudent WHERE flowid = ?";

        Student student = get(Student.class, sql, 10);
        System.out.println(student);

    }
    @Test
    public void testGet2(){
        String sql = "SELECT name name,email,email,birth,birth FROM customer WHERE id = ?";
        Customer customer = get(Customer.class, sql, 2);
        System.out.println(customer);

    }
    @Test
    public void tetGetForList(){
        String sql = "SELECT flow_id flowId, type, exam_card examCard, " +
                "id_card idCard, student_name studentName, location, " +
                "grade FROM examstudent";

        List<Student> students = getForList(Student.class, sql);
        System.out.println(students);


    }

    @Test
    public void  testGetForList2(){
        String sql = "SELECT name,email FROM customer";
        List<Customer> customers = getForList(Customer.class, sql);
        System.out.println(customers);
    }


}
