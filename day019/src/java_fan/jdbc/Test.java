package java_fan.jdbc;

import org.junit.experimental.theories.FromDataPoints;

import javax.swing.text.html.parser.Entity;
import java.sql.*;

public class Test {


    @org.junit.Test
    public <T> T get(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Entity entity = null;
        try {
            connection = JDBCTools.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                entity = (Entity) clazz.newInstance();


            }

        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            JDBCTools.releaseDB(resultSet, ps, connection);
        }
        return null;

    }
}
