package java_fan.jdbc;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.Test;

public class JDBCTest {

    @Test
    public void testResultSetMetaData() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        /*
        列的别名要和对象的属性一致
         */
        try {
            String sql = "SELECT flowid flowID, type, idcard idCard, "
                    + "examcard examCard, studentname studentName, "
                    + "location lcoation, grade " + "FROM examstudent WHERE flowid = ?";

            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 5);

            resultSet = preparedStatement.executeQuery();

            Map<String, Object> values =
                    new HashMap<String, Object>();

            //1. 得到 ResultSetMetaData 对象
            ResultSetMetaData rsmd = resultSet.getMetaData();

            while (resultSet.next()) {
                //2. 打印每一列的列名
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(columnLabel);

                    values.put(columnLabel, columnValue);
                }
            }

//			System.out.println(values);

            Class clazz = Student.class;

            Object object = clazz.newInstance();
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();

//				System.out.println(fieldName + ": " + fieldValue);

                ReflectionUtils.setFieldValue(object, fieldName, fieldValue);
            }

            System.out.println(object);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(resultSet, preparedStatement, connection);
        }
    }

    @Test
    public void testGet() {
        String sql = "SELECT id, name, email, birth "
                + "FROM customer WHERE id = ?";
//		System.out.println(sql);

     Customer customer = get(Customer.class, sql, 5);
        System.out.println(customer);

        sql = "SELECT flowid flowId, type, idcard idCard, "
                + "examcard examCard, studentname studentName, "
                + "location, grade " + "FROM examstudent WHERE flowid = ?";
//		System.out.println(sql);

        Student stu = get(Student.class, sql, 5);
        System.out.println(stu);
    }

    /**
     * 通用的查询方法：可以根据传入的 SQL、Class 对象返回 SQL 对应的记录的对象
     *
     * @param clazz: 描述对象的类型
     * @param sql:   SQL 语句。可能带占位符
     * @param args:  填充占位符的可变参数。
     * @return
     */
    public <T> T get(Class<T> clazz, String sql, Object... args) {
        T entity = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                entity = clazz.newInstance();

                //通过解析SQL语句来判断到底选择了那些列，以及需要为entity的对象
                //哪些属性赋值

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(resultSet, statement, connection);
        }


        return entity;


    }

    public Customer getCustomer(String sql, Object... args) {
        Customer customer = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // stu = new Student();
                // stu.setFlowId(resultSet.getInt(1));
                // stu.setType(resultSet.getInt(2));
                // stu.setIdCard(resultSet.getString(3));

                customer = new Customer();
                customer.setId(resultSet.getInt(1));
                customer.setName(resultSet.getString(2));
                customer.setEmail(resultSet.getString(3));
                customer.setBirth(resultSet.getDate(4));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(resultSet, preparedStatement, connection);
        }

        return customer;
    }

    public Student getStudent(String sql, Object... args) {
        Student stu = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                stu = new Student();
               stu.setFlowId(resultSet.getInt(1));
                stu.setType(resultSet.getInt(2));
                stu.setIdCard(resultSet.getString(3));
                // ...
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(resultSet, preparedStatement, connection);
        }

        return stu;
    }

    /**
     * 使用 PreparedStatement 将有效的解决 SQL 注入问题.
     */
    @Test
    public void testSQLInjection2() {
        String username = "a' OR PASSWORD = ";
        String password = " OR '1'='1";
        String sql = "SELECT * FROM users WHERE username = ? "
                + "AND password = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("登录成功!");
            } else {
                System.out.println("用户名和密码不匹配或用户名不存在. ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(resultSet, preparedStatement, connection);
        }
    }

    /**
     * SQL 注入.
     */
    @Test
    public void testSQLInjection() {
        String username = "a' OR PASSWORD = ";
        String password = " OR '1'='1";

        String sql = "SELECT * FROM users WHERE username = '" + username
                + "' AND " + "password = '" + password + "'";

        System.out.println(sql);

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCTools.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                System.out.println("登录成功!");
            } else {
                System.out.println("用户名和密码不匹配或用户名不存在. ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(resultSet, statement, connection);
        }
    }

    @Test
    public void testPreparedStatement() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBCTools.getConnection();
            String sql = "INSERT INTO customer (name, email, birth) "
                    + "VALUES(?,?,?)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "ATGUIGU");
            preparedStatement.setString(2, "simpleit@163.com");
            preparedStatement.setDate(3,
                    new Date(new java.util.Date().getTime()));

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(null, preparedStatement, connection);
        }
    }

    //    @Test
//    public void testGetStudent() {
//        // 1. 得到查询的类型
//        int searchType = getSearchTypeFromConsole();
//
//        // 2. 具体查询学生信息
//        Student student = searchStudent(searchType);
//
//        // 3. 打印学生信息
//        printStudent(student);
//    }
    public static void main(String[] args) {
        int searchType = getSearchTypeFromConsole();
        Student student = searchStudent(searchType);
        printStudent(student);
    }

    /**
     * 打印学生信息: 若学生存在则打印其具体信息. 若不存在: 打印查无此人.
     *
     * @param student
     */
    private static void printStudent(Student student) {
        if (student != null) {
            System.out.println(student);
        } else {
            System.out.println("查无此人!");
        }
    }

    /**
     * 具体查询学生信息的. 返回一个 Student 对象. 若不存在, 则返回 null
     *
     * @param searchType : 1 或 2.
     * @return
     */
    private static Student searchStudent(int searchType) {

        String sql = "SELECT flowid, type, idcard, examcard,"
                + "studentname, location, grade " + "FROM examstudent "
                + "WHERE ";

        Scanner scanner = new Scanner(System.in);

        // 1. 根据输入的 searchType, 提示用户输入信息:
        // 1.1 若 searchType 为 1, 提示: 请输入身份证号. 若为 2 提示: 请输入准考证号
        // 2. 根据 searchType 确定 SQL
        if (searchType == 1) {
            System.out.print("请输入准考证号:");
            String examCard = scanner.next();
            sql = sql + "examcard = '" + examCard + "'";
        } else {
            System.out.print("请输入身份证号:");
            String examCard = scanner.next();
            sql = sql + "idcard = '" + examCard + "'";
        }

        // 3. 执行查询
        Student student = getStudent(sql);

        // 4. 若存在查询结果, 把查询结果封装为一个 Student 对象

        return student;
    }

    /**
     * 根据传入的 SQL 返回 Student 对象
     *
     * @param sql
     * @return
     */
    private static Student getStudent(String sql) {

        Student stu = null;

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCTools.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                stu = new Student(resultSet.getInt(1), resultSet.getInt(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5), resultSet.getString(6),
                        resultSet.getInt(7));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(resultSet, statement, connection);
        }

        return stu;
    }

    /**
     * 从控制台读入一个整数, 确定要查询的类型
     *
     * @return: 1. 用身份证查询. 2. 用准考证号查询 其他的无效. 并提示请用户重新输入.
     */
    private static int getSearchTypeFromConsole() {

        System.out.print("请输入查询类型: 1. 用身份证查询. 2. 用准考证号查询 ");

        Scanner scanner = new Scanner(System.in);
        int type = scanner.nextInt();

        if (type != 1 && type != 2) {
            System.out.println("输入有误请重新输入!");
            throw new RuntimeException();
        }

        return type;
    }

    @Test
    public void testAddNewStudent() {
        Student student = getStudentFromConsole();
        addNewStudent2(student);
    }

    /**
     * 从控制台输入学生的信息
     */
    private Student getStudentFromConsole() {

        Scanner scanner = new Scanner(System.in);

        Student student = new Student();

        System.out.print("FlowId:");
        student.setFlowId(scanner.nextInt());

        System.out.print("Type: ");
        student.setType(scanner.nextInt());

        System.out.print("IdCard:");
        student.setIdCard(scanner.next());

        System.out.print("ExamCard:");
        student.setExamCard(scanner.next());

        System.out.print("StudentName:");
        student.setStudentName(scanner.next());

        System.out.print("Location:");
        student.setLocation(scanner.next());

        System.out.print("Grade:");
        student.setGrade(scanner.nextInt());

        return student;
    }

    public void addNewStudent2(Student student) {
        String sql = "INSERT INTO examstudent(flowid, type, idcard, "
                + "examcard, studentname, location, grade) "
                + "VALUES(?,?,?,?,?,?,?)";

        JDBCTools.update(sql, student.getFlowId(), student.getType(),
                student.getIdCard(), student.getExamCard(),
                student.getStudentName(), student.getLocation(),
                student.getGrade());
    }

    public void addNewStudent(Student student) {
        // 1. 准备一条 SQL 语句:
        String sql = "INSERT INTO examstudent VALUES(" + student.getFlowId()
                + "," + student.getType() + ",'" + student.getIdCard() + "','"
                + student.getExamCard() + "','" + student.getStudentName()
                + "','" + student.getLocation() + "'," + student.getGrade()
                + ")";

        System.out.println(sql);

        // 2. 调用 JDBCTools 类的 update(sql) 方法执行插入操作.
        JDBCTools.update(sql);
    }
    @Test
    public void test() throws  Exception{
        Connection connection = JDBCTools.getConnection();
        System.out.println(connection);
    }

}
