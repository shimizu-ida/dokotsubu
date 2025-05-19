<%@page import="model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
User u = (User)session.getAttribute("loginUser");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>どこつぶ</title>
</head>
<body>
<h1>どこつぶログイン</h1>
<% if(u != null){ %>
  <p>ようこそ<%= u.getName() %>さん</p>
  <a href="Main">つぶやき投稿・閲覧へ</a>
<% }else { %>
  <a href="index.jsp">TOP</a>
<% } %>
</body>
</html>