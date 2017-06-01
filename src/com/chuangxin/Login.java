package com.chuangxin;


import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String Finally = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String name = request.getParameter("name");
		String password =request.getParameter("password");
		String Remark = request.getParameter("remark");
		String remark = null;
		if(Remark!=null){
			remark =new String(request.getParameter("remark").getBytes("ISO8859-1"),"UTF-8");
			InsertRemark(name,remark);
		}
		String Email = request.getParameter("Email");
		//用户上线
		Final.LoginOrLogout(name, "true");
		
		
		if(name==null||password==null){
			
		}else{
			
		}
			if(Email==null&&name!=null){
				out.print(LoginByName(name,password));
			}else{
				out.print(LoginByEmail(Email,password));
			}
		

		  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	protected JSONObject LoginByEmail(String Email, String password){
		JSONObject jobj = new JSONObject();
		Connection conn = null;
		PreparedStatement pstmt = null;
		if(Final.JudgeEmail(Email)){
			jobj.put("IsLegal", true);
			jobj.put("Flag", true);
			try{
				Class.forName(Final.driverName);
				conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);
				
				String sql;
				sql = "Select UserPass From MyUser Where Email = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, Email);
				ResultSet rs = pstmt.executeQuery();
				int NUMBER = 0;
				String UserPass = null;
				while(rs.next()){
						NUMBER++;
						UserPass  = rs.getString("UserPass");
						
				}
				if(NUMBER==1&&UserPass.equals(password)){
					jobj.put("Result", true);
				}
				else{
					jobj.put("Result", false);
				}
				// 完成后关闭
				rs.close();
				pstmt.close();
				conn.close();
			} catch(SQLException | ClassNotFoundException se) {
				// 处理 JDBC 错误
				//se.printStackTrace();
				jobj.put("Flag", false);
		
			}finally{
				// 最后是用于关闭资源的块
				try{
					if(pstmt!=null)
						pstmt.close();
				}catch(SQLException se2){
					jobj.put("Flag", false);
				}
				try{
					if(conn!=null)
						conn.close();
				}catch(SQLException se){
					jobj.put("Flag", false);
				}
			}
	} else {
		jobj.put("IsLegal", false);
	}
		
		return jobj;
	}
	
	protected JSONObject LoginByName(String name, String password) {
		JSONObject jobj = new JSONObject();

		Connection conn = null;
		PreparedStatement pstmt = null;
		if (Final.JudgeName(name)) {
			if (password.length() == 32) {

				try {
					Class.forName(Final.driverName);
					conn = DriverManager.getConnection(Final.dbURL, Final.userName, Final.userPwd);

					String sql;
					sql = "Select UserPass From MyUser Where UserName = ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, name);
					ResultSet rs = pstmt.executeQuery();
					int NUMBER = 0;
					String UserPass = null;
					while (rs.next()) {
						NUMBER++;
						UserPass = rs.getString("UserPass");

					}
					if (NUMBER == 1 && UserPass.equals(password)) {
						jobj.put("Result", true);
					} else {
						if (NUMBER == 0) {

							jobj.put("Result", false);
							jobj.put("Message", "用户名不存在");
							jobj.put("Which", 0);
						} else {

							jobj.put("Result", false);
							jobj.put("Message", "密码错误");
							jobj.put("Which", 1);
						}
					}
					// 完成后关闭
					rs.close();
					pstmt.close();
					conn.close();
				} catch (SQLException | ClassNotFoundException se) {
					// 处理 JDBC 错误
					// se.printStackTrace();
					jobj.put("Message", "服务器异常");
					jobj.put("Which", 1);

				} finally {
					// 最后是用于关闭资源的块
					try {
						if (pstmt != null)
							pstmt.close();
					} catch (SQLException se2) {
						jobj.put("Message", "服务器异常");
						jobj.put("Which", 1);
					}
					try {
						if (conn != null)
							conn.close();
					} catch (SQLException se) {
						jobj.put("Message", "服务器异常");
						jobj.put("Which", 1);
					}
				}
			} else {
				jobj.put("Result", false);
				jobj.put("Message", "MD5异常。");
				jobj.put("Which", 1);
			}
		} else {
			jobj.put("Result", false);
			jobj.put("Message", "用户名不合法");
			jobj.put("Which", 0);
		}

		return jobj;
	}
	
	private void InsertRemark(String UserName, String remark){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL, Final.userName, Final.userPwd);
	
			String sql;
			sql = "Insert Into Remark(UserName,remark) values(?,?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			pstmt.setString(2, remark);
			int rs = pstmt.executeUpdate();
		
		}catch(Exception e){
			
		} finally {
			
		}
	}
	
		
}

