package configmgr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnectionManager {
	
	final String userID = "vkhuran";
	final String pwd = "001082454";
	final String port = "1521";
	final String server = "ora.csc.ncsu.edu";
	final String db_name = "ORCL";
	final String driver = "oracle.jdbc.driver.OracleDriver";
	final String url = "jdbc:oracle:thin:@" + server + ":" + port + ":" + db_name;
	
	Connection con;
	PreparedStatement stmt;
	ResultSet res_set;

	public Connection getConnection() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userID, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		return con;
	}

}
