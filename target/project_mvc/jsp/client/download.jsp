<%@ page import="Model.Contact" %><%--
  Created by IntelliJ IDEA.
  User: Quang Tho
  Date: 02/12/2022
  Time: 12:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <jsp:include page="common/head.jsp">
        <jsp:param name="title" value="FURNITURE | LIÊN HỆ"/>
    </jsp:include>
</head>
<body>
<%
    Contact contact = (Contact) request.getAttribute("contact");
    String success = (request.getAttribute("success") == null) ? null : request.getAttribute("success").toString();
%>
<div id="contact-contariner">
    <header>
        <jsp:include page="common/menu.jsp"/>
        <jsp:include page="common/searchBarMenu.jsp"/>
        <jsp:include page="common/head-bottom-page.jsp">
            <jsp:param name="title" value="LIÊN HỆ"/>
            <jsp:param name="page" value="Liên hệ"/>
        </jsp:include>
    </header>
    <div id="contact-content" class="container-fluid">
        <div class="row justify-content-between">
            <div class="contact-info col-12 col-md-4">
                <h3 class="big_title">Tải phần mềm tạo chữ ký số tại đây</h3>
                <a href="${pageContext.request.contextPath}/assets/digital-signature.zip" download="digital_sign_furniture.zip">Tool tạo chữ ký</a>
            </div>
        </div>

    </div>
    <jsp:include page="common/footer.jsp"/>
</div>
<jsp:include page="common/tail.jsp"/>
</body>
</html>
