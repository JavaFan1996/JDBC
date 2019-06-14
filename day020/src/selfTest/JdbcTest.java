package selfTest;

import java.util.List;
import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;


import com.mysql.jdbc.Driver;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class JdbcTest {
	/**
	 * 获取数据库连接
	 * 
	 * @return 返回连接
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Test
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		String user = "root";
		String jdbcUrl = "jdbc:mysql:///atguigu";
		String password = "f199612";
		String driverClass = "com.mysql.jdbc.Driver";
		Class.forName(driverClass);
		Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
		return connection;

	}

	/**
	 * 增、删、改
	 * 
	 * @param sql
	 * @param args
	 */

	@Test
	public static void UpDate(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		} finally {
			releaseOB(null, preparedStatement, connection);
		}

	}

	private static void releaseOB(ResultSet resultSet, Statement statement, Connection connection) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public static <T> T Get(Class<T> clazz, String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		T entity = null;
	
		try {
			connection = getConnection();
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Map<String, Object> values = new HashMap<>();
				ResultSetMetaData rsdm = resultSet.getMetaData();
				int columnCount = rsdm.getColumnCount();

				for (int i = 0; i < columnCount; i++) {
					String columLable = rsdm.getColumnLabel(i + 1);
					Object columValue = resultSet.getObject(i + 1);
					values.put(columLable, columValue);

				}
				entity = clazz.newInstance();
				for (Entry<String, Object> entry : values.entrySet()) {
					String propertyName = entry.getKey();
					Object value = entry.getValue();

					ReflectionUtils.setFieldValue(entity, propertyName, value);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			releaseOB(resultSet, preparedStatement, connection);
		}
		return entity;

	}
	// public static void main(String[] args) {
	// String sql = "SELECT name name,email,email,birth,birth FROM customer
	// WHERE id = ?";
	// Customer customer = Get(Customer.class, sql, 2);
	// System.out.println(customer);
	//
	//
	//
	// }

	public static <T> List<T> getforlist(Class<T> clazz, String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<T> result  = null;

		try {

			connection = getConnection();
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();

			List<Map<String, Object>> values = new ArrayList<>();
			List<String> columLables = new ArrayList<>();
			ResultSetMetaData rsmd = resultSet.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount();i++) {
				columLables.add(rsmd.getColumnLabel(i + 1));
			}
			Map<String, Object> map = null;
			while (resultSet.next()) {
				map = new HashMap<>();
				for (String columnLable : columLables) {

					Object value = resultSet.getObject(columnLable);
					map.put(columnLable, value);
					
				}
				values.add(map);
			}
			
			result = new ArrayList<>();
			T bean = null;
			if (values.size() > 0) {
				for(Map<String, Object> m:values){
					bean = clazz.newInstance();
					for(Entry<String, Object> entry:m.entrySet()){
						String properName = entry.getKey();
						Object value = entry.getValue();
						
						BeanUtils.setProperty(bean, properName, value);
					}
				}
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();

			// TODO: handle exception
		} finally {
			releaseOB(resultSet, preparedStatement, connection);

		}
		return result;
	}

		
	

}
