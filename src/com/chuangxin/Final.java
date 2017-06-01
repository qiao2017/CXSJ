package com.chuangxin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Final {

	static final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static final String dbURL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=CXSJ";
	static final String userName = "sa";
	static final String userPwd = "Chuangxin123";
	
	protected static boolean JudgeName(String s) {
		int number = s.length();
		if (number < 3 || number > 16)
			return false;
		char[] cs = s.toCharArray();
		int[] Number = new int[] { 0, 0, 0 };
		for (int i = 0; i < number; i++) {
			if (cs[i] >= '0' && cs[i] <= '9') {
				Number[0]++;
			} else if (cs[i] >= 'a' && cs[i] <= 'z' || cs[i] >= 'A' && cs[i] <= 'Z') {
				Number[1]++;
			} else if (cs[i] == '_')
				Number[2]++;
		}
		if ((Number[0] + Number[1] + Number[2]) == number)
			return true;
		else
			return false;
	}
	protected static boolean JudgeEmail(String Email) {

		String regEx = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
		// ����������ʽ
		Pattern pattern = Pattern.compile(regEx);
		// ���Դ�Сд��д��
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(Email);

		// �ַ����Ƿ���������ʽ��ƥ��
		return matcher.matches();

	}
	//��ѯUserA,UserB�Ƿ��Ѿ��Ǻ���
	protected static boolean QueryFriend(String UserA, String UserB){
		PreparedStatement pstmt = null;
		Connection conn = null;
		int Number=-1;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

			// ִ�� SQL ��ѯ
			String sql;
			sql = "Select Count(*) As Number From Friend Where UserA = ? And UserB = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserA);
			pstmt.setString(2, UserB);
			ResultSet rs = pstmt.executeQuery();
			//������
			rs.next();
			Number=rs.getInt("Number");
			rs.close();
			pstmt.close();
			conn.close();
		} catch(SQLException se) {
			// ���� JDBC ����
			Number=-1;
		} catch(Exception e) {
			// ���� Class.forName ����
			Number=-1;
		}finally{
			// ��������ڹر���Դ�Ŀ�
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

	
	//��ѯUserB�Ƿ��Ѿ�����UserA
	protected static boolean QueryApplicationRecord(String UserA, String UserB){
		PreparedStatement pstmt = null;
		Connection conn = null;
		int Number=-1;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

			// ִ�� SQL ��ѯ
			String sql;
			sql = "Select Count(*) As Number From ApplicationRecord Where BeApplicant = ? And Applicant = ? and IsAgree is null";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserA);
			pstmt.setString(2, UserB);
			ResultSet rs = pstmt.executeQuery();
			//������
			rs.next();
			Number=rs.getInt("Number");
			rs.close();
			pstmt.close();
			conn.close();
		} catch(SQLException se) {
			// ���� JDBC ����
			Number=-1;
		} catch(Exception e) {
			// ���� Class.forName ����
			Number=-1;
		}finally{
			// ��������ڹر���Դ�Ŀ�
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

	
	protected static Boolean LoginOrLogout(String UserName, String Flag){
		PreparedStatement pstmt = null;
		Connection conn = null;
		int Number = -1;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);
			
			String sql;
			sql = "Update MyUser "
					+ "Set IsOnLine = ? "
					+ " Where UserName= ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Flag.equals("true")?"1":"0");
			pstmt.setString(2, UserName);
			Number = pstmt.executeUpdate();
		}catch(SQLException | ClassNotFoundException e){
			Number=-1;
		}finally{
			// ��������ڹر���Դ�Ŀ�
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

	protected static boolean QueryUserExist(String UserName){
		PreparedStatement pstmt = null;
		Connection conn = null;
		int Number=-1;
		try{
			Class.forName(Final.driverName);
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);

			// ִ�� SQL ��ѯ
			String sql;
			sql = "Select Count(*) As Number From MyUser Where UserName = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			ResultSet rs = pstmt.executeQuery();
			//������
			rs.next();
			Number=rs.getInt("Number");
			rs.close();
			pstmt.close();
			conn.close();
		} catch(SQLException se) {
		} catch(Exception e) {
		}finally{
			// ��������ڹر���Դ�Ŀ�
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
		return Number==1?false:true;
		
	}

}
