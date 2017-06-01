package com.chuangxin;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.net.URLEncoder;  
  
import javax.servlet.ServletException;  
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
@WebServlet("/UpdateDownload")  
public class UpdateDownload extends HttpServlet {  
  
      
    public UpdateDownload() {  
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
  
      
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {

    	//获取最新版本名称
        String apkName = request.getParameter("apkName");
        if(apkName==null){
        	apkName="RunHDU.apk";
        }
        
    	String apkPath = request.getServletContext().getRealPath("") + File.separator + "software" + File.separator + apkName;
        File f = new File(apkPath);  
        if(f.exists()){  
            FileInputStream  fis = new FileInputStream(f);  
            String filename=URLEncoder.encode(f.getName(),"utf-8");//解决中文文件名下载后乱码的问题  
            byte[] b = new byte[fis.available()];  
            fis.read(b);  
            response.setCharacterEncoding("utf-8");  
            response.setHeader("Content-Disposition","attachment; filename="+filename+"");  
            //获取响应报文输出流对象  
            ServletOutputStream  out =response.getOutputStream();  
            //输出  
            out.write(b);  
            out.flush();  
            out.close();  
        }     
          
    }  
  
    /** 
     * Initialization of the servlet. <br> 
     * 
     * @throws ServletException if an error occurs  
     *public void init() throws ServletException {  
     *   // Put your code here
     *}  
  	 *
     */ 
}