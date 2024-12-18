<%--
  Created by IntelliJ IDEA.
  User: Quang Tho
  Date: 04/12/2022
  Time: 09:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored = "false" %>
<html>
<head>
    <jsp:include page="common/head.jsp">
        <jsp:param name="title" value="FURNITURE | ĐĂNG KÝ"/>
    </jsp:include>
</head>
<body>
<%
    String error = (request.getAttribute("error") == null) ? "" : request.getAttribute("error").toString();
    String success = (request.getAttribute("success") == null) ? "" : request.getAttribute("success").toString();
    boolean waitVerify = request.getAttribute("waitVerify") != null && (boolean) request.getAttribute("waitVerify");
%>
<div id="signup-container">
    <header>
        <jsp:include page="common/menu.jsp"/>
        <jsp:include page="common/searchBarMenu.jsp"/>
        <jsp:include page="common/head-bottom-page.jsp">
            <jsp:param name="title" value="ĐĂNG KÝ"/>
            <jsp:param name="page" value="signup"/>
        </jsp:include>
    </header>
    <form action="${pageContext.request.contextPath}/verifyForgotPassword" method="post">
        <div class="forgot-password container-fluid">
            <div class="forgot-password-swap">
                <div class="form-reset-password">
                    <div class="mb-3 overflow-hidden">
                        Đây là trang quên mật khẩu, hãy sử dụng link đã được gửi tới email để xác thực
                    </div>
                    <c:if test="<%= waitVerify %>">
                        <p class="monts">Chúng tôi đã gửi link xác thực đến email của bạn</p>
                    </c:if>
                    <c:if test="<%= !error.isEmpty() %>">
                        <div class="color-red mb-3 overflow-hidden">
                            <%= error %>
                        </div>
                    </c:if>
                    <c:if test="<%= !success.isEmpty() %>">
                        <div class="container-ip-info-user">
                            <label class="monts" for="new-password">Mật khẩu mới</label>
                            <input id="new-password" name="password" type="password">
                        </div>
                        <div class="btn-form-forgot-password">
                            <button class="btn-text-lg bgr-black hover-bg-red monts" name="action" value="verify">ĐẶT LẠI MẬT KHẨU</button>
                        </div>
                    </c:if>
                    <div class="btn-form-forgot-password">
                        <button class="btn-text-lg bgr-black hover-bg-red monts" name="action" value="resend">GỬI LẠI MÃ</button>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <jsp:include page="common/footer.jsp"/>
</div>
<jsp:include page="common/tail.jsp"/>
</body>
</html>
