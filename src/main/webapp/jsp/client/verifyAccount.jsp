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
      <form action="${pageContext.request.contextPath}/verifyAccount" method="post">
          <div class="forgot-password container-fluid">
              <div class="forgot-password-swap">
                  <div class="form-reset-password">
                      <c:if test="<%= !success.isEmpty() %>">
                          <div class="color-green mb-3 overflow-hidden">
                              <%= success %>
                          </div>
                      </c:if>
                      <c:if test="<%= !error.isEmpty() %>">
                          <div class="color-red mb-3 overflow-hidden">
                              <%= error %>
                          </div>
                      </c:if>
                      <c:if test="<%= waitVerify %>">
                          <p class="monts">Chúng tôi đã gửi link xác thực đến email của bạn</p>
                      </c:if>
                      <c:if test="<%= !error.isEmpty() || waitVerify %>">
                          <div class="btn-form-forgot-password">
                              <button class="btn-text-lg bgr-gray hover-bg-red monts" name="action">GỬI LẠI MÃ</button>
                          </div>
                      </c:if>
                  </div>
              </div>
          </div>
      </form>

      <jsp:include page="common/footer.jsp"/>
  </div>
  <jsp:include page="common/tail.jsp"/>
</body>
</html>
