package configmgr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataLoadRun {
	
	Connection con;
	PreparedStatement stmt;
	ResultSet res_set;

	public Connection init() {
		String userID = "vkhuran";
		String pwd = "001082454";
		String port = "1521";
		String server = "ora.csc.ncsu.edu";
		String db_name = "ORCL";
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@"+server+":"+port+":"+db_name;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userID, pwd);
			/*stmt.executeQuery("Insert into varun values (5000,'1085 khatiwala tank')");
			stmt = con.createStatement();
			res_set = stmt.executeQuery("select * from varun");
			while(res_set.next())
			{
				System.out.println(res_set.getInt("ssn"));
				System.out.println(res_set.getString(2));				
			}*/
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		return con;
	}

}