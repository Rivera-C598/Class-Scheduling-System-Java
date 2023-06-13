package system;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	static Connection dbconn = DBConnection.connectDB();

	final static String ADMIN_KEY = "root";

	final static String G5 = "grade5sched";
	final static String G6 = "grade6sched";
	final static String G7 = "grade7sched";
	final static String G8 = "grade8sched";
	final static String G9 = "grade9sched";
	final static String G10 = "grade10sched";

	final static String adminsTable = "admins";
	final static String usersTable = "users";
	final static String studentsTable = "students";
	final static String teachersTable = "teachers";
	final static String gradelvlTable = "gradelevels";
	final static String hsSubjectsTable = "highschoolsubjects";
	final static String elemSubjectsTable = "elementarysubjects";

	public static void main(String[] args) throws SQLException {
		initializeDatabase(dbconn, G5, G6, G7, G8, G9, G10, adminsTable, usersTable, studentsTable, teachersTable,
				gradelvlTable, hsSubjectsTable, elemSubjectsTable);
		launchApp();
	}

	private static void launchApp() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Login();

			}
		});
	}

	public static void initializeDatabase(Connection conn, String table1, String table2, String table3, String table4,
			String table5, String table6, String admins, String users, String students, String teachers,
			String gradelevels, String hsSubjectsTable, String elemSubjectsTable) throws SQLException {

		if (tableExist(conn, table1)) {
		} else {
			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + table1 + "(\r\n" + "tableId VARCHAR,\r\n"
							+ "timeRange VARCHAR,\r\n" + "mon VARCHAR,\r\n" + "tue VARCHAR,\r\n" + "wed VARCHAR,\r\n"
							+ "thu VARCHAR,\r\n" + "fri VARCHAR,\r\n" + "isMaxed boolean\r\n" + ")";
					String insertData = "INSERT INTO " + table1 + " (timeRange, mon, tue, wed, thu, fri, isMaxed)\r\n"
							+ "VALUES ('7:30am - 9:30am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('9:30am - 9:45am', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('9:45am - 11:45am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('11:45am - 12:45pm', 'lunch', 'lunch', 'lunch', 'lunch', 'lunch', null),\r\n"
							+ "('12:45pm - 2:45pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('2:45pm - 3:00pm', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('3:00pm - 5:00pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false)";
					Statement st = conn.createStatement();
					st.executeUpdate(createTable);
					st.executeUpdate(insertData);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		if (tableExist(conn, table2)) {
		} else {
			if (conn != null) {
				try {

					String createTable = "CREATE TABLE " + table2 + "(\r\n" + "tableId VARCHAR,\r\n"
							+ "timeRange VARCHAR,\r\n" + "mon VARCHAR,\r\n" + "tue VARCHAR,\r\n" + "wed VARCHAR,\r\n"
							+ "thu VARCHAR,\r\n" + "fri VARCHAR,\r\n" + "isMaxed boolean\r\n" + ")";
					String insertData = "INSERT INTO " + table2 + " (timeRange, mon, tue, wed, thu, fri, isMaxed)\r\n"
							+ "VALUES ('7:30am - 9:30am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('9:30am - 9:45am', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('9:45am - 11:45am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('11:45am - 12:45pm', 'lunch', 'lunch', 'lunch', 'lunch', 'lunch', null),\r\n"
							+ "('12:45pm - 2:45pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('2:45pm - 3:00pm', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('3:00pm - 5:00pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false)";
					Statement st = conn.createStatement();
					st.executeUpdate(createTable);
					st.executeUpdate(insertData);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		if (tableExist(conn, table3)) {
		} else {
			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + table3 + "(\r\n" + "tableId VARCHAR,\r\n"
							+ "timeRange VARCHAR,\r\n" + "mon VARCHAR,\r\n" + "tue VARCHAR,\r\n"
							+ "wed VARCHAR(100),\r\n" + "thu VARCHAR(100),\r\n" + "fri VARCHAR(100),\r\n"
							+ "isMaxed boolean\r\n" + ")";
					String insertData = "INSERT INTO " + table3 + " (timeRange, mon, tue, wed, thu, fri, isMaxed)\r\n"
							+ "VALUES ('7:30am - 9:30am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('9:30am - 9:45am', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('9:45am - 11:45am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('11:45am - 12:45pm', 'lunch', 'lunch', 'lunch', 'lunch', 'lunch', null),\r\n"
							+ "('12:45pm - 2:45pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('2:45pm - 3:00pm', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('3:00pm - 5:00pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false)";
					Statement st = conn.createStatement();
					st.executeUpdate(createTable);
					st.executeUpdate(insertData);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		if (tableExist(conn, table4)) {
		} else {

			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + table4 + "(\r\n" + "tableId VARCHAR(10),\r\n"
							+ "timeRange VARCHAR(20),\r\n" + "mon VARCHAR(100),\r\n" + "tue VARCHAR(100),\r\n"
							+ "wed VARCHAR(100),\r\n" + "thu VARCHAR(100),\r\n" + "fri VARCHAR(100),\r\n"
							+ "isMaxed boolean\r\n" + ")";
					String insertData = "INSERT INTO " + table4 + " (timeRange, mon, tue, wed, thu, fri, isMaxed)\r\n"
							+ "VALUES ('7:30am - 9:30am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('9:30am - 9:45am', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('9:45am - 11:45am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('11:45am - 12:45pm', 'lunch', 'lunch', 'lunch', 'lunch', 'lunch', null),\r\n"
							+ "('12:45pm - 2:45pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('2:45pm - 3:00pm', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('3:00pm - 5:00pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false)";
					Statement st = conn.createStatement();
					st.executeUpdate(createTable);
					st.executeUpdate(insertData);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		if (tableExist(conn, table5)) {
		} else {
			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + table5 + "(\r\n" + "tableId VARCHAR(10),\r\n"
							+ "timeRange VARCHAR(20),\r\n" + "mon VARCHAR(100),\r\n" + "tue VARCHAR(100),\r\n"
							+ "wed VARCHAR(100),\r\n" + "thu VARCHAR(100),\r\n" + "fri VARCHAR(100),\r\n"
							+ "isMaxed boolean\r\n" + ")";
					String insertData = "INSERT INTO " + table5 + " (timeRange, mon, tue, wed, thu, fri, isMaxed)\r\n"
							+ "VALUES ('7:30am - 9:30am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('9:30am - 9:45am', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('9:45am - 11:45am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('11:45am - 12:45pm', 'lunch', 'lunch', 'lunch', 'lunch', 'lunch', null),\r\n"
							+ "('12:45pm - 2:45pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('2:45pm - 3:00pm', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('3:00pm - 5:00pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false)";
					Statement st = conn.createStatement();
					st.executeUpdate(createTable);
					st.executeUpdate(insertData);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		if (tableExist(conn, table6)) {
		} else {
			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + table6 + "(\r\n" + "tableId VARCHAR(10),\r\n"
							+ "timeRange VARCHAR(20),\r\n" + "mon VARCHAR(100),\r\n" + "tue VARCHAR(100),\r\n"
							+ "wed VARCHAR(100),\r\n" + "thu VARCHAR(100),\r\n" + "fri VARCHAR(100),\r\n"
							+ "isMaxed boolean\r\n" + ")";
					String insertData = "INSERT INTO " + table6 + " (timeRange, mon, tue, wed, thu, fri, isMaxed)\r\n"
							+ "VALUES ('7:30am - 9:30am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('9:30am - 9:45am', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('9:45am - 11:45am', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('11:45am - 12:45pm', 'lunch', 'lunch', 'lunch', 'lunch', 'lunch', null),\r\n"
							+ "('12:45pm - 2:45pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false),\r\n"
							+ "('2:45pm - 3:00pm', 'break', 'break', 'break', 'break', 'break', null),\r\n"
							+ "('3:00pm - 5:00pm', 'vacant', 'vacant', 'vacant', 'vacant', 'vacant', false)";
					Statement st = conn.createStatement();
					st.executeUpdate(createTable);
					st.executeUpdate(insertData);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		if (conn != null) {
			try {
				String createTable = "CREATE TABLE IF NOT EXISTS " + users + " (\r\n"
						+ "  `id` varchar(45) NOT NULL,\r\n" + "  `pass` varchar(45) DEFAULT NULL,\r\n"
						+ "  `email` varchar(45) DEFAULT NULL,\r\n" + "  `userType` varchar(45) DEFAULT NULL,\r\n"
						+ "  `accCreated` timestamp NULL DEFAULT NULL,\r\n"
						+ "  `userName` varchar(255) DEFAULT NULL,\r\n" + "  `gender` varchar(10) DEFAULT NULL,\r\n"
						+ "  PRIMARY KEY (`id`)\r\n" + ")";
				Statement st = conn.createStatement();
				st.executeUpdate(createTable);

			} catch (SQLException ex) {
				Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		if (tableExist(conn, admins)) {
		} else {
			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + admins + " (\r\n"
							+ "  `adminId` int NOT NULL AUTO_INCREMENT,\r\n" + "  `userId` varchar DEFAULT NULL,\r\n"
							+ "  PRIMARY KEY (`adminId`)\r\n" + ")";

					Statement st = conn.createStatement();
					st.executeUpdate(createTable);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		if (tableExist(conn, students)) {
		} else {
			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + students + " (\r\n"
							+ "  `studentId` varchar DEFAULT NULL,\r\n" + "  `gradeLevel` varchar DEFAULT NULL,\r\n"
							+ "  `hasAddedGradeLevel` tinyint DEFAULT NULL\r\n" + ")";

					Statement st = conn.createStatement();
					st.executeUpdate(createTable);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		if (tableExist(conn, teachers)) {
		} else {
			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + teachers + " (\r\n"
							+ "  `teacherId` varchar DEFAULT NULL,\r\n"
							+ "  `preferredStartingGradeLevel` varchar DEFAULT NULL,\r\n"
							+ "  `preferredStartingSubject` varchar DEFAULT NULL,\r\n"
							+ "  `hasFollowedUp` tinyint DEFAULT NULL,\r\n" + "  `tableName` varchar DEFAULT NULL\r\n"
							+ ")";

					Statement st = conn.createStatement();
					st.executeUpdate(createTable);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		if (tableExist(conn, hsSubjectsTable)) {
		} else {
			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + hsSubjectsTable + " (\r\n"
							+ "  `subjects` varchar DEFAULT NULL\r\n" + ")";
					String insertData = "INSERT INTO " + hsSubjectsTable + " (subjects)\r\n" + "VALUES ('Math'),\r\n"
							+ "('Science'),\r\n" + "('English'),\r\n" + "('Filipino'),\r\n" + "('PE'),\r\n"
							+ "('Professional Elective'),\r\n" + "('TLE'),\r\n" + "('ESP')";

					Statement st = conn.createStatement();
					st.executeUpdate(createTable);
					st.executeUpdate(insertData);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		if (tableExist(conn, elemSubjectsTable)) {
		} else {
			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + elemSubjectsTable + " (\r\n"
							+ "  `subjects` varchar DEFAULT NULL\r\n" + ")";
					String insertData = "INSERT INTO " + elemSubjectsTable + " (subjects)\r\n" + "VALUES ('Math'),\r\n"
							+ "('Science'),\r\n" + "('English'),\r\n" + "('Filipino'),\r\n" + "('AP'),\r\n"
							+ "('Mapeh'),\r\n" + "('TLE'),\r\n" + "('ESP')";

					Statement st = conn.createStatement();
					st.executeUpdate(createTable);
					st.executeUpdate(insertData);
				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		if (tableExist(conn, gradelevels)) {
		} else {
			if (conn != null) {
				try {
					String createTable = "CREATE TABLE " + gradelevels + " (\r\n"
							+ "  `grade` varchar DEFAULT NULL,\r\n" + "  `isHighschool` tinyint DEFAULT NULL\r\n" + ")";
					String insertData = "INSERT INTO " + gradelevels + " (grade, isHighschool)\r\n" + "VALUES\r\n"
							+ "('5', false),\r\n" + "('6', false),\r\n" + "('7', true),\r\n" + "('8', true),\r\n"
							+ "('9', true),\r\n" + "('10', true)";

					Statement st = conn.createStatement();
					st.executeUpdate(createTable);
					st.executeUpdate(insertData);

				} catch (SQLException ex) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

	}

	private static boolean tableExist(Connection conn, String tableName) throws SQLException {
		String compareTable = tableName.toUpperCase();
		boolean tExists = false;
		if (conn != null) {
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery(
					"SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + compareTable + "'");
			if (res.next()) {
				

				tExists = true;
			} else {
			}
		}
		return tExists;
	}

}
