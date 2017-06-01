package com.chuangxin;

import java.sql.*;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/Read")
public class Read extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Read() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Enumeration headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String paramName = (String)headerNames.nextElement();
			System.out.print(paramName + "===");
			String paramValue = request.getHeader(paramName);
			System.out.println(paramValue);
		}

//		PrintWriter out = response.getWriter();
//		String name =new String(request.getParameter("name").getBytes("ISO8859-1"),"UTF-8");
//		System.out.println(name);
		StringBuffer sb = new StringBuffer("");  
		String result = "";  
		try {  
		    BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));  
		    String temp;  
		    while ((temp = br.readLine()) != null) {  
		        sb.append(temp);  
		    }  
		    br.close();  
		    result = sb.toString();
		    //打印android端上传的JSON数据  
		    System.out.println(sb);  
		} catch (Exception e) {  
		    e.printStackTrace();  
		}  
//		JSONObject jsonObject = JSONObject.fromObject(result);  
//		String name = jsonObject.getString("name"); 
		PrintWriter pw = response.getWriter();
		response.setContentType("text/javascript;charset=UTF-8"); 
		//封装服务器返回的JSON对象  
		JSONObject jsonReply = new JSONObject(); 
		jsonReply.put("age","34");  
		jsonReply.put("id", "0002");  
		//打印返回的JSON数据  
		System.out.println(jsonReply);  
		pw.print(jsonReply);
  
		
//
//		JSONArray jsonArray = JSONArray.fromObject(result); 

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}