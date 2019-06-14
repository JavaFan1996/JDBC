import org.junit.Test;
import selfTest.JdbcTest;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionTest {

    /**
     * 隔离级别
     * connection.setTransactionIsolation()
     *
     *
     *
     */
    @Test
    public void testTransactionIsolation(){
        Connection connection = null;
        try {
            connection = JDBCTools.getConnection();
            connection.setAutoCommit(false);
            String sql = "UPDATE users SET balance = balance-500 WHERE id =1";
            update(connection, sql);

            connection.commit();


        } catch (Exception e) {
            e.printStackTrace();

        }finally {

        }

    }
    @Test
    public void testTransactionIsolationRead(){
        String sql = "SELECT balance FROM users WHERE id =1";

        Integer balance = getForValue(sql);
        System.out.println(balance);

    }
    public <E> E getForValue(String sql, Object... args) {
        //1. 得到结果集: 该结果集应该只有一行, 且只有一列
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1. 得到结果集
            connection = JDBCTools.getConnection();
            System.out.println(connection.getTransactionIsolation());
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
    /**
     * Tom 给Jerry汇款500
     * <p>
     * 关于事务：
     * 1.如果多个操作，每个操作使用的是自己的单独的连接，则无法保存事务
     * 2.具体步骤：
     *   ① 事务操作之前，开始事务：取消connection的默认提交行为
     *                          connection.setAutoCommit(false);
     *   ②事务操作成功 则connection.commit()
     *   ③ 出现异常则在catch中connection.rollback();
     *   ④关闭连接
     */
    @Test
    public void testTransaction() {
//        DAO dao = new DAO();
//        String sql = "UPDATE users SET balance = balance-500 WHERE id =1";
//        dao.update(sql);
//        sql = "UPDATE users SET balance = balance+500 WHERE id =2";
//        dao.update(sql);
//
        Connection connection = null;
        try {

            connection = JDBCTools.getConnection();
            //1.开始事务 取消默认提交
            connection.setAutoCommit(false);

            String sql = "UPDATE users SET balance = balance-500 WHERE id =1";
            update(connection, sql);

            int i = 10 / 0;
            System.out.println(i);

            sql = "UPDATE users SET balance = balance+500 WHERE id =2";
            update(connection, sql);

            //提交事务
            connection.commit();




        } catch (Exception e) {
            e.printStackTrace();

            //回滚事务
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } finally {
            JDBCTools.releaseDB(null, null, connection);
        }


    }

    public void update(Connection connection, String sql, Object... args) {

        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            JDBCTools.releaseDB(null, preparedStatement, null);
        }


    }


}
