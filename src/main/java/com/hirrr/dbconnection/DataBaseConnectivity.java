/**
 * 
 */
package com.hirrr.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * DataBase related operation class.
 * i.e, creates the connection & closes the
 * connection
 * @author Padmajith
 *
 */
public class DataBaseConnectivity {

	/**
	 * creates connection to the database
	 * @param ip ip address of the db system
	 * @param dataBase schema name
	 * @return connection object
	 */
	public static Connection getConnected(String ip, String dataBase) {
		
		Connection con = null;
		
		final String url = "jdbc:mysql://192.168.1." + ip + ":3306/" + dataBase;
		final String driverClass = "com.mysql.jdbc.Driver";
		final String username = "root";
		final String password = "careersnow@123";
		
		try {
			
			 Class.forName(driverClass);
			 con = DriverManager.getConnection(url, username, password);
			 
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return con;
	}
	
	public static Connection mainServerConnection() {
		
		Connection con = null;
		
		final String url = "jdbc:mysql://192.168.1.15/scrapperdb";
		final String driverClass = "com.mysql.jdbc.Driver";
		final String username = "mithunManohar";
		final String password = "mithun2999";
		
		try {
			
			 Class.forName(driverClass);
			 con = DriverManager.getConnection(url, username, password);
			 
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return con;
	}
	
	public static Connection mainServerConnectionResultsProviderDB() {

		Connection con = null;

		final String url = "jdbc:mysql://192.168.1.15/results_provider_db";
		final String driverClass = "com.mysql.jdbc.Driver";
		final String username = "mithunManohar";
		final String password = "mithun2999";

		try {

			Class.forName(driverClass);
			con = DriverManager.getConnection(url, username, password);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return con;
	}
	
	/**
	 *  closing the data base connection
	 * @param con database connection object
	 */
	public static void closingTheConnection(Connection con) {
		
		if ( con != null ) {
			
			try {	
				con.close();		
			} catch (Exception e) {	
				e.printStackTrace();	
			}
		}
	}
	
	/**
	 * closing the prepared statement
	 * @param ps prepared statement object
	 */
	public static void closingThePreparedStatement(PreparedStatement ps) {
		
		if ( ps != null ) {
			
			try {	
				ps.close();		
			} catch (Exception e) {	
				e.printStackTrace();	
			}
		}
	}
	
	/**
	 * closing the resultset object
	 * @param rs resultset object
	 */
	public static void closingTheResultSet(ResultSet rs) {
		
		if ( rs != null ) {
			
			try {	
				rs.close();		
			} catch (Exception e) {	
				e.printStackTrace();	
			}
		}
	}
	
	public static void main(String[] args) {
		mainServerConnection();
	}
}