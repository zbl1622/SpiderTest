package com.zbl.spider;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DBAccess {
	
	private static DBAccess dbAccess;
	//数据库配置
	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/spider";
	private String user = "root";
	private String password = "700007";

	private Connection connection;
	private PreparedStatement prepareInsertURL;
	private PreparedStatement prepareSelectURL;
	private PreparedStatement prepareDeleteURL;
	
	private DBAccess() {
		try {
			// 加载驱动程序
			Class.forName(driver);

			// 连续数据库
			connection = DriverManager.getConnection(url, user, password);

			if (!connection.isClosed()) {
				
			}
			
			initPreparedStatement();
			
		} catch (ClassNotFoundException | SQLException e) {

			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();

		}
		
	}
	
	public static DBAccess getInstance(){
		if(dbAccess==null){
			dbAccess=new DBAccess();
		}
		return dbAccess;
	}
	
	public void close(){
		try {
			if (connection!=null&&!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private void initPreparedStatement(){
		try {
			prepareInsertURL=connection.prepareStatement("INSERT INTO url_list (url,date) VALUES (?,?)");
			prepareSelectURL=connection.prepareStatement("SELECT * FROM url_list WHERE url=?");
			prepareDeleteURL=connection.prepareStatement("DELETE FROM url_list WHERE url=?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean insertURL(String url){
		boolean flag=false;
		try {
			prepareSelectURL.setString(1, url);
			prepareSelectURL.execute();
			ResultSet rs = prepareSelectURL.getResultSet();
			if(rs!=null&&!rs.first()){
				prepareInsertURL.setString(1, url);
				prepareInsertURL.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				prepareInsertURL.execute();
				flag = true;
			}else{
				flag = false;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	public boolean deleteURL(String url){
		try {
			prepareDeleteURL.setString(1, url);
			return prepareDeleteURL.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
