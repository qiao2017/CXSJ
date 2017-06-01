package com.chuangxin;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class DatabaseAccess
 */
@WebServlet("/MyFriends")
public class MyFriends extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyFriends() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String UserName =request.getParameter("UserName");
//		String UserName =new String(request.getParameter("UserName"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		String Time = new String(df.format(new Date()));
		String year = Time.substring(0, 4);
		String month = Time.substring(5, 7);
		String day = Time.substring(8, 10);
		
		if(UserName==null){
			out.print("Please Input UserName");
		}else{
			JSONObject j=new JSONObject();
			j.put("MyFriends", QueryFriends(UserName,year+month+day));
			j.put("ApplyLists", Query(UserName));
			out.print(j);
		}

	}		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	//查询好友
	private JSONArray QueryFriends(String UserName,String Time){
		PreparedStatement pstmt = null;
		Connection conn = null;
		JSONArray jarr = new JSONArray();
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

			// 执行 SQL 查询
			String sql;
//			sql = "Select Count(*) As Number From ApplicationRecord Where Applicant = '" + UserA + "' "
//			+ "And BeApplicant = '" + UserB + "' And IsAgree is null ";
			sql = "Select UserB  From Friend Where UserA = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			ResultSet rs = pstmt.executeQuery();
			//处理结果
			while(rs.next()){
				JSONObject obj = new JSONObject();
				String User=rs.getString("UserB");
//				String Time = new String((rs.getObject("FTime").toString()).substring(0, 19));
				obj.put("UserName", User);
				obj.put("SumTimes", QueryTimes(User));
				obj.put("TodayTimes", QueryTimes(User,Time));
				obj.put("IsOnLine", QueryIsOnLine(User));
				jarr.add(obj);
			}
			
			rs.close();
			pstmt.close();
			conn.close();
		} catch(SQLException | ClassNotFoundException se) {
			// 处理 JDBC 错误
		}finally{
			// 最后是用于关闭资源的块
			try{
				if(pstmt!=null)
				pstmt.close();
			}catch(SQLException se2){
			}
			try{
				if(conn!=null)
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		
		return jarr;
		
	}

	private int QueryTimes(String UserName, String Time){
		PreparedStatement pstmt = null;
		Connection conn = null;
		int Number=0;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

			// 执行 SQL 查询
			String sql;
//			sql = "Select Count(*) As Number From ApplicationRecord Where Applicant = '" + UserA + "' "
//			+ "And BeApplicant = '" + UserB + "' And IsAgree is null ";
			sql = "Select Count(*)  From Data_running Where userName = ?  and runId like '"
					+ Time + "%'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			ResultSet rs = pstmt.executeQuery();
			//处理结果
			rs.next();
			Number = rs.getInt(1);
			
			rs.close();
			pstmt.close();
			conn.close();
		} catch(SQLException | ClassNotFoundException se) {
			// 处理 JDBC 错误
		}finally{
			// 最后是用于关闭资源的块
			try{
				if(pstmt!=null)
				pstmt.close();
			}catch(SQLException se2){
			}
			try{
				if(conn!=null)
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		
		return Number;
		
	}

	
	private int QueryTimes(String UserName){
		PreparedStatement pstmt = null;
		Connection conn = null;
		int Number=0;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

			// 执行 SQL 查询
			String sql;
//			sql = "Select Count(*) As Number From ApplicationRecord Where Applicant = '" + UserA + "' "
//			+ "And BeApplicant = '" + UserB + "' And IsAgree is null ";
			sql = "Select Count(*) as Number  From Data_running Where userName = ?  ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			ResultSet rs = pstmt.executeQuery();
			//处理结果
			rs.next();
			Number = rs.getInt("Number");
			
			rs.close();
			pstmt.close();
			conn.close();
		} catch(SQLException | ClassNotFoundException se) {
			// 处理 JDBC 错误
		}finally{
			// 最后是用于关闭资源的块
			try{
				if(pstmt!=null)
				pstmt.close();
			}catch(SQLException se2){
			}
			try{
				if(conn!=null)
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		
		return Number;
		
	}

	private boolean QueryIsOnLine(String UserName){
		PreparedStatement pstmt = null;
		Connection conn = null;
		String str=null;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

			// 执行 SQL 查询
			String sql;
			sql = "Select IsOnLine  From MyUser Where UserName = ?  ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			ResultSet rs = pstmt.executeQuery();
			//处理结果
			rs.next();
			str = rs.getString("IsOnLine");
			
			rs.close();
			pstmt.close();
			conn.close();
		} catch(SQLException | ClassNotFoundException se) {
			// 处理 JDBC 错误
		}finally{
			// 最后是用于关闭资源的块
			try{
				if(pstmt!=null)
				pstmt.close();
			}catch(SQLException se2){
			}
			try{
				if(conn!=null)
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		if(str==null){
			return false;
		}else{
			return str.equals("1")?true:false;
		}
		
	}

	private JSONArray Query(String UserName){
		JSONArray jarry = new JSONArray();
		PreparedStatement pstmt = null;
		Connection conn = null;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

			// 执行 SQL 查询
			String sql;
			sql = "Select Applicant,ATime From ApplicationRecord Where BeApplicant = ? And IsAgree is null ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			ResultSet rs = pstmt.executeQuery();
			//处理结果
			while(rs.next()){
				JSONObject obj = new JSONObject();
				obj.put("Applicant",rs.getString("Applicant"));
				obj.put("ApplyDate", ((rs.getObject("ATime")).toString()).substring(0, 19));
				jarry.add(obj);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch(SQLException se) {
			// 处理 JDBC 错误
		} catch(Exception e) {
			// 处理 Class.forName 错误
		}finally{
			// 最后是用于关闭资源的块
			try{
				if(pstmt!=null)
				pstmt.close();
			}catch(SQLException se2){
			}
			try{
				if(conn!=null)
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return jarry;
		
	}

	
}










