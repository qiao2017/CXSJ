package com.chuangxin;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;  
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
@WebServlet("/ShowHead")  
public class ShowHead extends HttpServlet {  
  
      
    public ShowHead() {  
        super();  
    }  
  
  
    public void destroy() {  
        super.destroy(); // Just puts "destroy" string in log  
        // Put your code here  
    }  
  
  
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        doPost(request,response);  
    }  

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

		String UserName =new String(request.getParameter("UserName").getBytes("ISO8859-1"),"UTF-8");
//		String Number =new String(request.getParameter("Number").getBytes("ISO8859-1"),"UTF-8");
        // ��Ӧ�����
        ServletOutputStream out = response.getOutputStream();
    	
    	
        // ��ȡ��ʽ
        response.setContentType("image/jpeg");
        // ��ȡͼƬ����·��
        String Path = request.getServletContext().getRealPath("") + File.separator + "Image" + File.separator
        		+ UserName +".JPEG";
        File file = new File(Path);
        	
        // �����ļ�������
        FileInputStream is = new FileInputStream(file);
	        	
	        // ����������
	        byte[] buffer = new byte[1024];
	        int len = 0;
	        while ((len = is.read(buffer))!= -1) {
	            out.write(buffer, 0, len);
	        }
	        is.close();
	        out.flush();
	        out.close();
        
 
    }
 
    public void init() throws ServletException {  
        // Put your code here  
    }  
  
}