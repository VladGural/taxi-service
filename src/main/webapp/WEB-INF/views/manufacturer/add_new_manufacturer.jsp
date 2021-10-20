<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        <%@include file='/WEB-INF/views/css/common.css'%>
    </style>
    <%@include file='/WEB-INF/views/head.html'%>
    <title>Add new manufacturer</title>
</head>
<body>
    <h1>For add new Manufacturer fill in the forms below</h1>
    <h3 style="color: red">${errorMsg}</h3>
    <form method="post" action="${pageContext.request.contextPath}/manufacturers/add">
        <label>Manufacturer Name (must by unique)</label><br>
        <input type="text" name="name" required><br>
        <label>Country</label><br>
        <input type="text" name="country" required><br><br>
        <button type="submit">Confirm</button><br><br>
    </form>
    <h3><a href="${pageContext.request.contextPath}/manufacturers">
        All manufacturers</a></h3>
</body>
</html>
