<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>どこつぶ - ログイン結果</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
  <h1 class="mb-4">ログイン結果</h1>
  <c:choose>
    <c:when test="${not empty sessionScope.loginUser}">
      <div class="alert alert-success" role="alert">
        ログイン成功！ <c:out value="${sessionScope.loginUser.name}" />さん、ようこそ！
      </div>
      <a href="Main" class="btn btn-primary">メインページへ</a>
    </c:when>
    <c:otherwise>
      <div class="alert alert-danger" role="alert">
        ログイン失敗。ユーザー名またはパスワードが正しくありません。
      </div>
      <a href="index.jsp" class="btn btn-secondary">ログインページへ戻る</a>
    </c:otherwise>
  </c:choose>
</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</body>
</html>