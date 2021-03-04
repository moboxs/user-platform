<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<%--<a href="hello-servlet">Hello Servlet</a>--%>
<form action="loginServlet" method="get" >
    用户名：<input type="text" name="name" value=""><br>
    密码：  <input type="password" name="password" value=""><br><br>
    <input type="submit" value="登录" name="login">
    <input type="reset"value="重置"><br>
</form>

</body>
</html>