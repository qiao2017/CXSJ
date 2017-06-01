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

import net.sf.json.JSONObject;

/**
 * Servlet implementation class DatabaseAccess
 */
@WebServlet("/BeFriend")
public class BeFriend extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BeFriend() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String UserA =new String(request.getParameter("UserA").getBytes("ISO8859-1"),"UTF-8");
		String UserB =new String(request.getParameter("UserB").getBytes("ISO8859-1"),"UTF-8");
		String IsAgree =new String(request.getParameter("IsAgree").getBytes("ISO8859-1"),"UTF-8");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		String Time = new String(df.format(new Date()));
		JSONObject jobj = new JSONObject();
		if(Final.QueryFriend(UserA, UserB)){
			jobj.put("Result", false);
			jobj.put("Message", "已经是好友 ");
			out.print(jobj);
		}else{
			//修改申请表，将字段IsAgree置为1或0
			UpDateApplicantRecord(UserA, UserB, Time, IsAgree.equals("true")?"1":"0");
			
			if(IsAgree.equals("true")){
				if(AddFriend(UserA,UserB,Time)){
					jobj.put("Result", true);
					out.print(jobj);
				}else{
					jobj.put("Result", false);
					jobj.put("Message", "服务器异常");
					out.print(jobj);
				}
				
			}else{
				jobj.put("Result", true);
				out.print(jobj);
			}
		}

	}		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected static boolean AddFriend(String UserA, String UserB, String FTime){
		PreparedStatement pstmt = null;
		Connection conn = null;
		int Number = -1;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);
			
			String sql;
			sql = "Insert Into Friend(UserA,UserB,FTime)"
					+ "Values(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserA);
			pstmt.setString(2, UserB);
			pstmt.setString(3, FTime);
			int number = pstmt.executeUpdate();
			pstmt.setString(1, UserB);
			pstmt.setString(2, UserA);
			pstmt.setString(3, FTime);
			Number = pstmt.executeUpdate() + number;
		}catch(SQLException | ClassNotFoundException e){
			Number=-1;
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
		if(Number==2){
			return true;
		}else{
			return false;
		}
	}
	
	
	protected static boolean UpDateApplicantRecord(String UserA, String UserB, String RTime, String IsAgree){
		PreparedStatement pstmt = null;
		Connection conn = null;
		int Number = -1;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);
			
			String sql;
			sql = "Update ApplicationRecord "
					+ "Set IsAgree = ? ,ReplyTime = ? "
					+ " Where Applicant = ? and BeApplicant = ? and IsAgree is null";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, IsAgree);
			pstmt.setString(2, RTime);
			pstmt.setString(3, UserA);
			pstmt.setString(4, UserB);
			Number = pstmt.executeUpdate();
		}catch(SQLException | ClassNotFoundException e){
			Number=-1;
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
		if(Number==1){
			return true;
		}else{
				
			return false;
		}
	}


}











