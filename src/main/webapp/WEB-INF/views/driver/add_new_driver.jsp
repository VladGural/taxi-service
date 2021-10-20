<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        <%@include file='/WEB-INF/views/css/common.css'%>
    </style>
    <%@include file='/WEB-INF/views/head.html'%>
    <title>Add new driver</title>
</head>
<body>
    <h2>For add new Driver fill in the forms below</h2>
    <h3 style="color: red">${errorMsg}</h3>
    <form method="post" action="${pageContext.request.contextPath}/drivers/add">
        <label>Driver Name</label><br>
        <input type="text" name="name" required><br>
        <label>License Number (mast by unique and format must by like AA-1234)</label><br>
        <input type="text" name="license_number" required><br><br>
        <label>Driver login</label><br>
        <input type="text" name="login" required><br>
        <label>Driver password</label><br>
        <input type="text" name="password" required><br><br>
        <button type="submit">Confirm</button><br><br>
    </form>
    <h3><a href="${pageContext.request.contextPath}/drivers">All drivers</a></h3>
</body>
</html>
