<%--
  Created by IntelliJ IDEA.
  User: zj
  Date: 2020/3/17
  Time: 14:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录页面</title>
</head>
<body>

<h1>user login</h1>
<form action="login" method="post">
    username:<input type="text" name="username"><p>
    password:<input type="password" name="password">
    <p>
        ${msg }
        <input type="submit" value="submit">
</form>
</body>
</html>
