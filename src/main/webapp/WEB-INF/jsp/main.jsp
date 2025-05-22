<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>どこつぶ</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-3">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <p class="h5">ようこそ、<c:out value="${sessionScope.loginUser.name}" />さん</p>
    <a href="Logout" class="btn btn-outline-secondary">ログアウト</a>
  </div>

  <form action="Main" method="post" class="mb-4" enctype="multipart/form-data">
    <div class="mb-3">
      <label for="text" class="form-label">いまどうしてる？</label>
      <textarea name="text" id="text" class="form-control" rows="2" placeholder="テキストを入力"></textarea>
    </div>
    <div class="mb-3">
      <label for="image" class="form-label">画像 (任意):</label>
      <input type="file" name="image" id="image" class="form-control">
    </div>
    <button type="submit" class="btn btn-success">投稿</button>
  </form>
  
  <a href="Main" class="btn btn-info mb-3">更新</a>

  <c:if test="${not empty errorMsg}">
      <div class="alert alert-danger">${errorMsg}</div>
  </c:if>

  <c:forEach var="mutter" items="${mutterList}">
    <div class="card mb-2">
      <div class="card-body">
        <h6 class="card-subtitle mb-2 text-muted"><c:out value="${mutter.userName}" /></h6>
        <p class="card-text"><c:out value="${mutter.text}" /></p>
        <c:if test="${mutter.imageData != null && mutter.imageData.length > 0}">
          <img src="${pageContext.request.contextPath}/image?id=${mutter.id}" alt="投稿画像" class="img-fluid rounded mt-2" style="max-height: 200px;">
        </c:if>
      </div>
      <div class="card-footer bg-transparent border-top-0">
        <c:if test="${mutter.userId == sessionScope.loginUser.id}">
          <a href="EditMutterServlet?id=${mutter.id}" class="btn btn-sm btn-outline-primary ms-2">編集</a>
        </c:if>
      </div>
    </div>
  </c:forEach>
</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</body>
</html>