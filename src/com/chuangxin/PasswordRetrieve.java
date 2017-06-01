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
@WebServlet("/PasswordRetrieve")
public class PasswordRetrieve extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// JDBC 驱动名及数据库 URL
	static final String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static final String dbURL="jdbc:sqlserver://127.0.0.1:1433;DatabaseName=CXSJ";
	static final String userName="sa";
	static final String userPwd="Chuangxin123"; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasswordRetrieve() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String UserName = request.getParameter("UserName");
		String Email = request.getParameter("Email");
		
		
		if(UserName==null&&Email!=null){
			out.print(QueryPassByEmail(Email));
			
		}else{
			if(UserName!=null&&Email==null){
				out.print(QueryPassByName(UserName));
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
	
	private JSONObject QueryPassByName(String UserName){
		JSONObject jobj = new JSONObject();
		jobj.put("Flag", true);
		
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		try{
			Class.forName(driverName);
			conn = DriverManager.getConnection(dbURL,userName,userPwd);
			
			String sql;
			sql = " Select UserName,UserPass,Email "
					+ " From  MyUser "
					+ " Where UserName = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			rs = pstmt.executeQuery();
			while(rs.next()){
					
				jobj.put("UserName", rs.getString("UserName"));
				jobj.put("UserPass", rs.getString("UserPass"));					
				jobj.put("Email", rs.getString("Email"));
			}
			
		}catch(SQLException | ClassNotFoundException e){
			jobj.put("Flag", false);
		}finally{
			// 最后是用于关闭资源的块
			try{
				if(rs!=null)
				rs.close();
			}catch(SQLException se0){
				jobj.put("Flag", false);
			}
			try{
				if(pstmt!=null)
				pstmt.close();
			}catch(SQLException se1){
				jobj.put("Flag", false);
			}
			try{
				if(conn!=null)
				conn.close();
			}catch(SQLException se2){
				jobj.put("Flag", false);
			}
			
		}
		
		return jobj;
	}

	
	private JSONObject QueryPassByEmail(String Email){
		JSONObject jobj = new JSONObject();
		jobj.put("Flag", true);
		
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		try{
			Class.forName(driverName);
			conn = DriverManager.getConnection(dbURL,userName,userPwd);
			
			String sql;
			sql = " Select UserName,UserPass,Email "
					+ " From  MyUser "
					+ " Where Email = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Email);
			rs = pstmt.executeQuery();
			while(rs.next()){
				jobj.put("UserName", rs.getString("UserName"));
				jobj.put("UserPass", rs.getString("UserPass"));					
				jobj.put("Email", rs.getString("Email"));
			}
			
		}catch(SQLException | ClassNotFoundException e){
			jobj.put("Flag", false);
		}finally{
			// 最后是用于关闭资源的块
			try{
				if(rs!=null)
				rs.close();
			}catch(SQLException se0){
				jobj.put("Flag", false);
			}
			try{
				if(pstmt!=null)
				pstmt.close();
			}catch(SQLException se1){
				jobj.put("Flag", false);
			}
			try{
				if(conn!=null)
				conn.close();
			}catch(SQLException se2){
				jobj.put("Flag", false);
			}
			
		}
		
		return jobj;
	}

		//-1表示出错，0表示没有;

	protected JSONObject PassRetrieve(JSONObject jobj){
		JSONObject rjobj = new JSONObject();
		
		
		return null;
		
	}
}










