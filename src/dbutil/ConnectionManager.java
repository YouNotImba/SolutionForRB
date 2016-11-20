package dbutil;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionManager {
	private static ConnectionManager ourInstance;
	private static Connection conn = null;

	public static ConnectionManager getInstance() {
		if (ourInstance == null)
			ourInstance = new ConnectionManager();
		return ourInstance;
	}

	private ConnectionManager() {
	}

	public Connection getConnection() {
		if (conn == null) {
			try {
				DataSource dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/myDb");
				Connection conn = dataSource.getConnection();
				System.out.println(conn);
				return conn;
			} catch (SQLException | NamingException e) {
				System.err.println("Connection failed " + e.getMessage());
				return null;
			}
		}
		return conn;
	}

	public void close() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
			System.err.println("Connection has been closed");
		} catch (SQLException e) {
			System.err.println("whoops!");
		}
	}

}
