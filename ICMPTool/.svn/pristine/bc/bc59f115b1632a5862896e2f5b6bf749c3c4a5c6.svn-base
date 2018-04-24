package com.optel.database;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.optel.constant.InvariantParameters;
import com.optel.thread.Logger;

/**
 * 自动生成映射所需表数据文件
 * @author LiH
 */
public class Connections {

	private static String SQL_DEVICE_NAME="com.mysql.jdbc.Driver";
	private static String URL="jdbc:mysql://%s/Uniview?autoReconnect=true";
	private static String USER_NAME = InvariantParameters.NDT_DST_DATA_USER;
	private static String USER_PWD = InvariantParameters.NDT_DST_DATA_PWD;
	
	private Connection conn;
	public Connections(){
	}
	
	public void doDataConfig() {
		StringBuffer sb = new StringBuffer("");
		Statement st = null;
		try{
			Class.forName(SQL_DEVICE_NAME);
			conn = DriverManager.getConnection(String.format(URL, InvariantParameters.NDT_DST_DATA_URL), USER_NAME, USER_PWD);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT DISTINCT ipaddr FROM uniview.emnecomm where state = 1");
			while(rs.next()){
				String ip = rs.getString("ipaddr");
				if(ip.contains(".0.") || ip.contains(".0")){
					continue;
				}
				sb.append(ip).append("\r");
			}
		}catch(Exception e){
			Logger.log(e.getMessage());
			e.printStackTrace();
		}finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					Logger.log(e.getMessage());
					e.printStackTrace();
				}
				if(conn != null){
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.log(e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}

		File file = new File("res/host.txt");
		if(file.exists()){
			file.delete();
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "rw");
			raf.write(sb.toString().getBytes());
		} catch (IOException e) {
			Logger.log(e.getMessage());
			e.printStackTrace();
		}finally{
			if(raf != null){
				try {
					raf.close();
				} catch (IOException e) {
					Logger.log(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

}
