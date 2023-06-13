package system;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	static final String url = "jdbc:h2:/data/db2;AUTO_SERVER=TRUE";
	static final String username = "sa";
	static final String password = "";

	public static Connection connectDB() {
		Connection conn = null;
		try {
			Class.forName("org.h2.Driver");

			conn = DriverManager.getConnection(url, username, password);
			return conn;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
