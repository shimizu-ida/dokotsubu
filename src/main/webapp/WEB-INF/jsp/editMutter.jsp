<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>投稿編集 - どこつぶ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h2>投稿を編集</h2>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty mutterToEdit}">
            <form action="EditMutterServlet" method="post">
                <input type="hidden" name="mutterId" value="<c:out value="${mutterToEdit.id}" />">
                <div class="mb-3">
                    <label for="text" class="form-label">内容:</label>
                    <textarea name="text" id="text" class="form-control" rows="3"><c:out value="${mutterToEdit.text}" /></textarea>
                </div>
                <c:if test="${not empty mutterToEdit.imagePath}">
                    <div class="mb-3">
                        <label class="form-label">現在の画像 (変更不可):</label><br>
                        <img src="${pageContext.request.contextPath}/${mutterToEdit.imagePath}" alt="現在の画像" class="img-fluid rounded" style="max-height: 150px;">
                    </div>
                </c:if>
                <button type="submit" class="btn btn-primary">更新</button>
                <a href="Main" class="btn btn-secondary">キャンセル</a>
            </form>
        </c:if>
        <c:if test="${empty mutterToEdit && empty errorMessage}">
             <div class="alert alert-warning">編集対象の投稿が見つかりません。</div>
             <a href="Main" class="btn btn-primary">メインページへ</a>
        </c:if>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</body>
</html>
