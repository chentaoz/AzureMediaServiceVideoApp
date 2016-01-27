package com.videoapp.utilities;
import java.sql.*;
public class JDBC {
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/video";
	   static final String USER = "root";
	   static final String PASS = "root";
	   private static JDBC instance = new JDBC();
	   private Connection conn=null;
	   private JDBC(){
		   Statement stmt = null;
		   try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to a selected database...");
		    conn = DriverManager.getConnection(DB_URL, USER, PASS);
		    System.out.println("Connected database successfully...");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		      
	   }
	   
	   public static Connection getConnectinInstance(){
		   return instance.conn;
	   }
	   
	   public static void main(String [] args){
		   Connection con=getConnectinInstance();
		   
	   }
}
