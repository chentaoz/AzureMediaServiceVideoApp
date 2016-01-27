package com.videoapp.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class VideoDAO {
	private static  Connection conn=JDBC.getConnectinInstance();
	
	private int id;
	private String uri;
	private String tag;
	private String time;
	
	public VideoDAO(int id,String uri,String tag, String time){
		
		this.id=id;
		this.uri=uri;
		this.tag=tag;
		this.time=time;
	}
	public VideoDAO(int id,String tag, String time){
		
		this.id=id;
		this.tag=tag;
		this.time=time;
	}
	
	public static List<VideoDAO> getAllVideos() throws SQLException{
		Statement stmt = conn.createStatement();
		 String sql2="SELECT * FROM video";
		 ResultSet rs=stmt.executeQuery(sql2);
		 List<VideoDAO> res=new ArrayList<VideoDAO>();
		 while(rs.next()){
			 
			 int tmpId  = rs.getInt("id");
			 String uri=rs.getString("uri");
			 String tag=rs.getString("tag");
			 String time=rs.getString("time");
			 
			 res.add(new VideoDAO(tmpId,uri,tag,time));
			 
		 }
		 rs.close();
		 
		 return res;
	}
	
	public static int updateTag(String id,String tag) throws SQLException{
		Statement stmt = conn.createStatement();
		String sql2="UPDATE video SET tag='"+tag+"' WHERE id="+id;
		int rs=stmt.executeUpdate(sql2);
		return rs;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public static VideoDAO insert(String tag, String uri) throws SQLException{
		 Statement stmt = conn.createStatement();
		 Date todaysDate = new Date();
		 DateFormat df = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
		 String time = df.format(todaysDate);
		 String sql = "INSERT INTO video (tag,uri,time) " +
                 "VALUES ('"+tag+"', '"+uri+"', '"+time+"')";
		 stmt.executeUpdate(sql);
		 String sql2="SELECT * FROM video";
		 ResultSet rs=stmt.executeQuery(sql2);
		 int tmpId=0;
		 while(rs.next()){
			 tmpId  = rs.getInt("id");
		 }
		 rs.close();
		 return new VideoDAO(tmpId,uri,tag,time);
	 }
		 
		 
		
	
	public static VideoDAO insert(String tag) throws SQLException{
		 Statement stmt = conn.createStatement();
		 Date todaysDate = new Date();
		 DateFormat df = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
		 String time = df.format(todaysDate);
		 String sql = "INSERT INTO video (tag,time) " +
                "VALUES ('"+tag+","+time+"')";
		 stmt.executeUpdate(sql);
		 String sql2="SELECT LAST_INSERT_ID() FROM video";
		 ResultSet rs=stmt.executeQuery(sql2);
		 int tmpId=0;
		 while(rs.next()){
			 tmpId  = rs.getInt("id");
		 }
		 rs.close();
		 return new VideoDAO(tmpId,tag,time);
	}
	
}
