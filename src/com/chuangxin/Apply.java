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
@WebServlet("/Apply")
public class Apply extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Apply() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String UserA = new String(request.getParameter("UserA").getBytes("ISO8859-1"), "UTF-8");
		String UserB = new String(request.getParameter("UserB").getBytes("ISO8859-1"), "UTF-8");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		// System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		String ApplyTime = new String(df.format(new Date()));

		JSONObject jobj = new JSONObject();

		if (Final.QueryFriend(UserA, UserB)) {
			jobj.put("Result", false);
			jobj.put("Message", "已经是好友");
			out.print(jobj);
		} else {
			if (Final.QueryUserExist(UserA)) {
				// 申请人不存在
				jobj.put("Result", false);
				jobj.put("Message", "该用户不存在");
				jobj.put("which", 0);
				out.print(jobj);

			} else if (Final.QueryUserExist(UserB)) {
				// 被申请人不存在
				jobj.put("Result", false);
				jobj.put("Message", "该用户不存在");
				jobj.put("which", 1);
				out.print(jobj);
			} else {

				if (Final.QueryApplicationRecord(UserA, UserB)) {
					jobj.put("Result", true);
					out.print(jobj);
					BeFriend.AddFriend(UserA, UserB, ApplyTime);
					BeFriend.UpDateApplicantRecord(UserB, UserA, ApplyTime, "1");
				} else {

					int number = Query(UserA, UserB);
					if (number == -1) {
						jobj.put("Result", false);
						jobj.put("Message", "服务器异常");
						out.print(jobj);
					} else if (number == 1) {
						jobj.put("Result", false);
						jobj.put("Message", "已经申请过");
						out.print(jobj);
					} else {
						// 申请
						out.print(Insert(UserA, UserB, ApplyTime));
					}
				}
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
	
	private JSONObject Insert(String UserA, String UserB, String ATime){
		PreparedStatement pstmt = null;
		Connection conn = null;
		JSONObject jobj = new JSONObject();
		int Number = -1;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);
			
			String sql;
			sql = "Insert Into ApplicationRecord(Applicant,BeApplicant,Atime)"
					+ "Values(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserA);
			pstmt.setString(2, UserB);
			pstmt.setString(3, ATime);
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
			jobj.put("Result", true);
			return jobj;
		}else{
			jobj.put("Result", false);
			jobj.put("Message", "服务器异常");
			return jobj;
		}
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
			sql = "Select Count(*) As Number From ApplicationRecord Where Applicant = ? And BeApplicant = ? And IsAgree is null ";
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

	
	
}










