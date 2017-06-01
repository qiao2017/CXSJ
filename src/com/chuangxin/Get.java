package com.chuangxin;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
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
@WebServlet("/Get")
public class Get extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Get() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String UserName =new String(request.getParameter("UserName").getBytes("ISO8859-1"),"UTF-8");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		String ApplyTime = new String(df.format(new Date()));
		JSONObject jobj = new JSONObject();
		JSONArray json = null;
/*
		Iterator<String> it = json.keys();
		while(it.hasNext()){
			String json_key = it.next();
			String json_value = json.getString(json_key);
			System.out.println(UserName+"hbsafijnfij\n");
			Update(UserName,json_key,ApplyTime,json_value);
			
		}
*/
		jobj.put("Reply", json);
		jobj.put("HaveApplivant", Query(UserName));
		out.print(jobj);

	}		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private boolean Update(String UserA, String UserB, String Time,String Flag){
		PreparedStatement pstmt = null;
		Connection conn = null;
		int Number = -1;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);
			
			String sql;
			sql = "UpDate ApplicationRecord"
					+ " Set ReplyTime = ? ,IsAgree= ? "
					+ " Where Applicant = ? And BeApplicant = ? And IsAgree is null";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Time);
			pstmt.setString(2, Flag);
			pstmt.setString(3, UserA);
			pstmt.setString(4, UserB);
			Number = pstmt.executeUpdate();
		}catch(SQLException | ClassNotFoundException e){
			Number=-1;
			System.out.println(UserA+UserB);
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
		if(Number>=1){
			return true;
		}else{
		return false;
		}
	}


	//查询申请记录
	protected static JSONObject Query(String UserName){
		JSONArray jarry = new JSONArray();
		JSONObject jobj = new JSONObject();
		jobj.put("Flag", true);
		PreparedStatement pstmt = null;
		Connection conn = null;
		int Number=0;
		boolean flag=true;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

			// 执行 SQL 查询
			String sql;
			sql = "Select Applicant,ATime From ApplicationRecord Where BeApplicant = ? And IsAgree is null ";
			pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, UserName);
			ResultSet rs = pstmt.executeQuery();
			//处理结果
			while(rs.next()){
				Number++;
			}
			jobj.put("Number", Number);
			if(Number>=1){
				rs.first();
				
				do{
					JSONObject obj = new JSONObject();
					obj.put("申请人",rs.getString("Applicant"));
					obj.put("申请日期", ((rs.getObject("ATime")).toString()).substring(0, 19));
					jarry.add(obj);
					
				}while(rs.next());
			}
			
			rs.close();
			pstmt.close();
			conn.close();
		} catch(SQLException se) {
			// 处理 JDBC 错误
			flag=false;
		} catch(Exception e) {
			// 处理 Class.forName 错误
			flag=false;
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
		jobj.put("requestList",jarry);
		if(flag==false){
			jobj.put("Flag", false);
//			jarry.set(0, jobj);
			
		}
		return jobj;
		
	}


}










