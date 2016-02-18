

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MysqlConnector {

	public MysqlConnector() {
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("JDBC driver registered");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Connection getConnection() {
		String db_name = "tsdhlwal";
		try {
			boolean is_created = false;
			//Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/" + db_name + "?" + "user=root&password=greatsqldb");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/?" + "user=root&password=greatsqldb");
			System.out.println("Got Mysql database connection");
			ResultSet rs = conn.getMetaData().getCatalogs();
			while (rs.next()) {
				if(rs.getString(1).equals(db_name)){
					conn.setCatalog(db_name);
					is_created  = true;
					ResultSet tables = conn.getMetaData().getTables(null, null, "todo", null);
					if (!tables.next()) {
						PreparedStatement stmt2 = null;
						stmt2 = conn.prepareStatement("CREATE TABLE todo (`id` int(11) NOT NULL,"
								+ " `message` varchar(45) DEFAULT NULL,"
								+ " `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
								+ " PRIMARY KEY (`id`));");
						stmt2.executeUpdate();
						
						}
						
				}
			}
			if(!is_created){
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				stmt = conn.prepareStatement("CREATE DATABASE " + db_name);
				stmt.executeUpdate();
				conn.setCatalog(db_name);
				stmt2 = conn.prepareStatement("CREATE TABLE todo (`id` int(11) NOT NULL,"
						+ " `message` varchar(45) DEFAULT NULL,"
						+ " `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
						+ " PRIMARY KEY (`id`));");
				stmt2.executeUpdate();
				
			}
			return conn;
			
			
		} catch (SQLException ex) {
			
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return null;
	}

	public Map<Integer, String> getAll(){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			Map<Integer,String> messages = new HashMap<Integer, String>();

			// Get the connection to the database
			con = getConnection();

			// Execute the query
			stmt = con.prepareStatement("select * from todo;");
			rs = stmt.executeQuery();
			while (rs.next()) {
				messages.put(rs.getInt("id"), rs.getString("message"));
				
			}
			return messages;
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore

				stmt = null;
			}
			if(con != null){
				try {
					con.close();
				} catch (SQLException sqlEx) {
				} // ignore

				con = null;
			}

		}
		return null;
	}


	
	public boolean insert(String message, int id){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		
		try {
			// Get the connection to the database
			con = getConnection();

			// Execute the query
			stmt = con.prepareStatement(" insert into todo (id, message, time)" + "values (?, ?, ?) on duplicate key update message=?, time=?");
			stmt.setInt(1, id);
			stmt.setString(2, message);
			stmt.setTimestamp(3, currentTimestamp);
			stmt.setString(4, message);
			stmt.setTimestamp(5, currentTimestamp);

			return stmt.executeUpdate() > 0;

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore

				stmt = null;
			}
			if(con != null){
				try {
					con.close();
				} catch (SQLException sqlEx) {
				} // ignore

				con = null;
			}

		}
		return false;
	}

	public String get(int id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			String message;

			// Get the connection to the database
			con = getConnection();

			// Execute the query
			stmt = con.prepareStatement("select message,time from todo where id = ?");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			rs.next();
			message =  rs.getString("message");
			message = message + " ---- " + rs.getString("time");
			return message;
			
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore

				stmt = null;
			}
			if(con != null){
				try {
					con.close();
				} catch (SQLException sqlEx) {
				} // ignore

				con = null;
			}

		}
		return null;
	}


	public boolean delete(int id) {
		PreparedStatement stmt = null;
		Connection con = null;
		try {
			// Get the connection to the database
			con = getConnection();
			// Execute the query
			stmt = con.prepareStatement("delete from todo where id = ?");
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
			
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore

				stmt = null;
			}
			if(con != null){
				try {
					con.close();
				} catch (SQLException sqlEx) {
				} // ignore

				con = null;
			}

		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}