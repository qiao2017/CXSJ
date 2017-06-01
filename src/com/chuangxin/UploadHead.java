package com.chuangxin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadHead")
public class UploadHead extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	
    protected void doPost(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");  
        PrintWriter out = response.getWriter();
        
        String UserName = request.getParameter("UserName");
        String UserImageStr = request.getParameter("Image");
  
        
        
/*        //查询数据库中用户头像数量
        int UserHeadNum = QueryHeads(UserName) + 1;
        //路径
        String Path = request.getServletContext().getRealPath("") + File.separator + "Image" + File.separator
        + UserName + "_" + UserHeadNum +".JPEG";
        

		JSONObject res = new JSONObject();
		
		
		if(UserHeadNum-1>=0){
			res.put("QueryHeads", UserHeadNum);
		}else{
			res.put("QueryHeads", UserHeadNum);
		}
        
        //存入磁盘
        if(StorImage(Path, UserImageStr)){
        	
    		res.put("StoreInDisk","true");
    		
        }else{
    		res.put("StoreInDisk","false");
        }        
        
        //头像路径存入数据库
        if(Stor(UserName, Path)){
    		res.put("StoreInDatabase","true");
        }else{
    		res.put("StoreInDatabase","false");
        }
        out.print(res);
*/       
        

        //路径
        String Path = request.getServletContext().getRealPath("") + File.separator + "Image" + File.separator
        + UserName  +".JPEG";

		JSONObject res = new JSONObject();
		//存入磁盘
        if(StorImage(Path, UserImageStr)){
        	
    		res.put("Result",true);
    		
        }else{
    		res.put("Result",false);
    		res.put("Message","头像未存入磁盘");
        }   
        
        
        //头像路径存入数据库
        if(QueryHeads(UserName)==1){
        	
        }else{
	        if(Stor(UserName, Path)){
	        	res.put("Result", true);
	        }else{
	    		res.put("Result",false);
	    		res.put("Message","头像未存入数据库");
	        }
        }
        out.print(res);
        
    }
    
    //头像存入磁盘
    private boolean StorImage(String ImagePath,String ImageStr){
    	try{
            byte[] b = new BASE64Decoder().decodeBuffer(ImageStr);  
           	for(int i = 0; i < b.length; ++i) {
           		if (b[i] < 0) {  
           			// 调整异常数据  
           			b[i] += 256;  
            	}  
           	}  
           	// 生成JPEG图片  
           	OutputStream out = new FileOutputStream(ImagePath); 
            out.write(b);  
            out.flush();  
            out.close();   
        } catch (Exception e) {  
	    	return false;
        }
    	return true;
    }
    
    
    //查询用户UserName有多少头像
    private int QueryHeads(String UserName){
			Connection conn = null;
			Statement stmt = null;
			int	number = 0;
	    	try{
	    	// 注册 JDBC 驱动器
			Class.forName(Final.driverName);
			
			// 打开一个连接
			conn = DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);
	
			// 执行 SQL 查询
			stmt = conn.createStatement();
			String sql;
			sql = "Select Count(*) as number From Image Where UserName = '" + UserName +"'";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			number = rs.getInt("number");
			
    	} catch(SQLException se){
    		return -1;
    	} catch(Exception e){
    		return -1;
    	} finally{
    		try{
				if(stmt!=null)
				stmt.close();
			}catch(SQLException se2){
			}
			try{
				if(conn!=null)
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
    	}
    	
    	return number;
    }
    
    
    //用户UserName新增头像路径存入数据库
    private boolean Stor(String UserName,String HeadPath){
    	int flag=0;
		Connection conn = null;
		Statement stmt = null;
    	try{
    	Class.forName(Final.driverName);
    	
		conn=DriverManager.getConnection(Final.dbURL,Final.userName,Final.userPwd);
		
		stmt =conn.createStatement();
		String sql = "Insert Into Image Values('" + UserName + "','" + HeadPath + "')";
		flag = stmt.executeUpdate(sql);
		stmt.close();
		conn.close();
    	}catch(SQLException se){
    		return false;
    	} catch(Exception e){
    		return false;
    	} finally{
    		try{
				if(stmt!=null)
				stmt.close();
			}catch(SQLException se2){
			}
			try{
				if(conn!=null)
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
    	}
    	if(flag==1){
    		return true;
    	}else{
    		return false;
    	}
    }

    protected void doGet(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response); 
    }
 
    
}


