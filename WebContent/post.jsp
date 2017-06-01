<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="js/json2.js"></script>
<body>
<form action="">
<table>
<tr>
<td>名字</td>
<td><input type="text" id="name" name="name"/></td>
</tr>
<tr>
<td>年龄</td>
<td><input type="text" id="age" name="age"/></td>
</tr>
<tr>
<td><input type="button" value="提交" onclick="commit();"/> </td>
</tr>
</table>
<table id="ulist" border="2">
</table>
</form>
</body>
<script type="text/javascript">
function commit(){
    $.ajax(
            {type : "post",
             data:{name:$('#name').val(),age:$('#age').val()},
             url : "testJson_testJson.action",
             dataType : "JSON",
             success : callback
                }
            );
}
function callback(data){
    var json =  JSON.parse(data);
    alert("fdf");
    var ulist =  $("#ulist");
    $.each(json, function(i,item){
         ulist.append(
        "<tr><td>"+item.name+"</td><td>"+item.age+"</td></tr>"
                 );
        })
}
</script>
</html>