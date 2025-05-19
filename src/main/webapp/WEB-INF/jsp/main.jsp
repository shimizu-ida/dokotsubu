<%@page import="model.Mutter"%>
<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
User u = (User)session.getAttribute("loginUser");

//List<Mutter> mutterList = (List<Mutter>)application.getAttribute("mutterList");
List<Mutter> mutterList = (List<Mutter>)request.getAttribute("mutterList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>どこつぶ</title>
</head>
<body>
<h1>どこつぶメイン</h1>
<p>
<%= u.getName() %>さん、ログイン中
<a href="Logout">ログアウト</a>
</p>
<p>
<a href="Main">更新</a>
<form action="Main" method="post">
<input type="text" name="text">
<input type="submit">
</form>

<% for(Mutter m: mutterList){ %>
	<p><%= m.getUserName() %>:<%= m.getText() %></p>
<% } %>
</p>
</body>
</html>