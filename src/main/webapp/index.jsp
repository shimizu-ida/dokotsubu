<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>どこつぶ</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
  <form action="Login" method="post" class="card p-4 shadow-sm">
    <h1 class="text-center mb-4">どこつぶへようこそ</h1>
    <div class="mb-3">
      <label for="name" class="form-label">ユーザー名:</label>
      <input type="text" name="name" id="name" class="form-control">
    </div>
    <div class="mb-3">
      <label for="pass" class="form-label">パスワード:</label>
      <input type="password" name="pass" id="pass" class="form-control">
    </div>
    <button type="submit" class="btn btn-primary w-100">ログイン</button>
  </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</body>
</html>