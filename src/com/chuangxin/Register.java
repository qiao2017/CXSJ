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
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// JDBC 驱动名及数据库 URL

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 设置响应内容类型
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String name = request.getParameter("name");
		String Email = request.getParameter("Email");
		String password = request.getParameter("password");

		if (Email == null) {

			out.print(RegisterWithOutEmail(name, password));
		} else {
			out.print(RegisterWithEmail(name, password, Email));
		}

	}

	protected JSONObject RegisterWithOutEmail(String name, String password) {
		JSONObject jobj = new JSONObject();

		Connection conn = null;
		Statement stmt = null;
		if (Final.JudgeName(name)) {
			if (password.length() == 32) {

				try {
					Class.forName(Final.driverName);
					conn = DriverManager.getConnection(Final.dbURL, Final.userName, Final.userPwd);
					stmt = conn.createStatement();
					String sql;
					sql = "Select * From MyUser Where UserName = '" + name + "'";
					ResultSet rs = stmt.executeQuery(sql);

					int number = 0;
					while (rs.next()) {
						number++;
					}
					rs.close();

					if (number > 0) {
						// 该用户名已经存在
						jobj.put("Result", false);
						jobj.put("Message", "用户名已存在");
						jobj.put("Which", 0);
					} else {
						String Sql = "Insert Into MyUser(UserName,UserPass)Values('" + name + "','" + password + "')";
						int UpdateRs = stmt.executeUpdate(Sql);
						if (UpdateRs == 1) {
							jobj.put("Result", true);
						} else {
							jobj.put("Result", false);
						}
					}

					// 关闭连接
					stmt.close();
					conn.close();

				} catch (Exception e) {
					jobj.put("Result", false);
					jobj.put("Message", e.toString());
					jobj.put("Which", 1);
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

	protected JSONObject RegisterWithEmail(String name, String password, String Email) {

		JSONObject jobj = new JSONObject();

		Connection conn = null;
		Statement stmt = null;
		if (Final.JudgeName(name) && Final.JudgeEmail(Email)) {
			jobj.put("IsLegal", true);
			try {
				jobj.put("Flag", true);
				Class.forName(Final.driverName);
				conn = DriverManager.getConnection(Final.dbURL, Final.userName, Final.userPwd);
				stmt = conn.createStatement();
				String sql;
				sql = "Select * From MyUser Where UserName = '" + name + "'";
				ResultSet rs = stmt.executeQuery(sql);

				int number = 0;
				while (rs.next()) {
					number++;
				}
				rs.close();

				if (number > 0) {
					// 该用户名已经存在
					jobj.put("AlreadyHave", true);
				} else {

					jobj.put("AlreadyHave", false);
					String Sql = "Insert Into MyUser(UserName,UserPass,Email)" + "Values('" + name + "','" + password
							+ "','" + Email + "')";
					int UpdateRs = stmt.executeUpdate(Sql);
					if (UpdateRs == 1) {
						jobj.put("Result", true);
					} else {
						jobj.put("Result", false);
					}
				}

				// 关闭连接
				stmt.close();
				conn.close();

			} catch (Exception e) {
				jobj.put("Flag", false);
			}

		} else {
			jobj.put("IsLegal", false);
		}
		return jobj;

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	
}