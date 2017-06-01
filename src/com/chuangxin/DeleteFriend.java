package com.chuangxin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

@WebServlet("/DeleteFriend")
public class DeleteFriend  extends HttpServlet {

	 
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String UserA = new String(request.getParameter("UserA").getBytes("ISO8859-1"), "UTF-8");
			String UserB = new String(request.getParameter("UserB").getBytes("ISO8859-1"), "UTF-8");
			
			JSONObject jobj = new JSONObject();
			if(Query(UserA,UserB)==1){
				if(DeleteFriend(UserA, UserB)){
					jobj.put("Result", true);
					out.print(jobj);
				}else{
					jobj.put("Result", false);
					jobj.put("Message", "服务器异常");
					out.print(jobj);
				}
			}else{
				jobj.put("Result", false);
				jobj.put("Message", "你们不是好友");
				out.print(jobj);
			}
		}
		
		
		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
			doGet(request, response);
		}
		
		private int Query(String UserA, String UserB){
			PreparedStatement pstmt = null;
			Connection conn = null;
			int Number=-1;
			try{
				Class.forName(Final.driverName);
				conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

				// 执行 SQL 查询
				String sql;
				sql = "Select Count(*) As Number From Friend Where UserA = ? And UserB = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, UserA);
				pstmt.setString(2, UserB);
				ResultSet rs = pstmt.executeQuery();
				//处理结果
				rs.next();
				Number=rs.getInt("Number");
				rs.close();
				pstmt.close();
				conn.close();
			} catch(SQLException se) {
				// 处理 JDBC 错误
				Number=-1;
			} catch(Exception e) {
				// 处理 Class.forName 错误
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
			return Number;
			
		}

		private boolean DeleteFriend(String UserA, String UserB){
			PreparedStatement pstmt = null;
			Connection conn = null;
			int Number=-1;
			try{
				Class.forName(Final.driverName);
				conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

				// 执行 SQL 查询
				String sql;
				sql = "Delete From Friend Where UserA= ? and UserB = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, UserA);
				pstmt.setString(2, UserB);
				Number = pstmt.executeUpdate();
				pstmt.setString(1, UserB);
				pstmt.setString(2, UserA);
				Number += pstmt.executeUpdate();
				pstmt.close();
				conn.close();
			} catch(SQLException se) {
				// 处理 JDBC 错误
				Number=-1;
			} catch(Exception e) {
				// 处理 Class.forName 错误
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
			return Number==2?true:false;
			
		}

		
		
}
