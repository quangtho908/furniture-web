<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Model.Order" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored = "false" %>
<%@ page import="DTO.AuthorizationData" %>

<html lang="en">
    <head>
        <jsp:include page="common/head.jsp">
            <jsp:param name="title" value="FURNITURE | KẾT QUẢ THANH TOÁN"/>
        </jsp:include>
        <!-- Custom styles for this template -->

    </head>
    <body>
        <%
            Order order = (Order) request.getAttribute("order");
            AuthorizationData authorizationData = (AuthorizationData) session.getAttribute("authorization");
            boolean logged = (boolean) request.getAttribute("logged");
            String success = (String) request.getAttribute("success");
            String error = (String) request.getAttribute("error");
            boolean isOwner = false;
            if(logged) {
              isOwner = authorizationData.getId().equals(order.getUserId());
            }
            String hash = "";
            if(logged) {
              hash = (String) request.getAttribute("hash");
            }
        %>
        <!--Begin display -->
        <div id="result-payment-container">
            <header>
                <jsp:include page="common/menu.jsp">
                    <jsp:param name="logged" value="<%=false%>"/>
                </jsp:include>
                <jsp:include page="common/searchBarMenu.jsp"/>
                <jsp:include page="common/head-bottom-page.jsp">
                    <jsp:param name="title" value="KẾT QUẢ THANH TOÁN"/>
                    <jsp:param name="page" value="Thanh toán thành công"/>
                </jsp:include>
            </header>
            <div class="container mt-5 mb-5">
                <div class="header clearfix">
                    <h3 class="text-muted">VNPAY RESPONSE</h3>
                </div>
                <div class="table-responsive">
                    <div class="form-group">
                        <label >Mã đơn hàng:</label>
                        <label><%=order.getId()%></label>
                    </div>
                    <div class="form-group">
                        <label >Trạng thái đơn:</label>
                        <label><%=order.getStatusString()%></label>
                    </div>
                    <c:if test="<%= order.getTransID() != null %>">
                        <div class="form-group">
                            <label>Mã giao dịch VNPAY:</label>
                            <label><%=order.getTransID()%></label>
                        </div>
                    </c:if>
                    <div class="form-group">
                        <label >Tên khách hàng:</label>
                        <label><%=order.getUsername()%></label>
                    </div>
                    <div class="form-group">
                        <label>Quốc gia:</label>
                        <label><%=order.getCountry()%></label>
                    </div>
                    <div class="form-group">
                        <label >Thành phố:</label>
                        <label><%=order.getCity()%></label>
                    </div>
                    <div class="form-group">
                        <label >Quận/Huyện:</label>
                        <label><%=order.getDistrict()%></label>
                    </div>
                    <div class="form-group">
                        <label >Số điện thoại:</label>
                        <label><%=order.getPhone()%></label>
                    </div>
                    <div class="form-group">
                        <label >Email:</label>
                        <label><%=order.getEmail()%></label>
                    </div>
                </div>
                <c:if test="<%=isOwner && (order.getStatus() == 0)%>">
                    <form action="${pageContext.request.contextPath}/verifySign" method="post">
                        <h3 class="mt-5">Xác minh chữ ký số</h3>
                        <div class="form-group mt-3">
                            <label for="amount">Nội dung ký</label>
                            <textarea class="form-control" cols="20" id="vnp_OrderInfo" name="hash" rows="2" readOnly><%= hash %></textarea>
                        </div>
                        <div class="form-group mt-3">
                            <label for="OrderDescription">chữ ký</label>
                            <textarea class="form-control" cols="20" id="vnp_OrderInfo" name="sign" rows="2"></textarea>
                        </div>
                        <div style="display: none;">
                            <input name="orderId" value="<%= order.getId()%>" />
                        </div>
                        <button class="btn-text-lg bgr-black hover-bg-red monts">Xác minh chữ ký</button>
                    </form>
                </c:if>
            </div>
            <c:if test="<%=error != null%>">
                <div class="alert-danger alert alert-dismissible fade show fixed-top" role="alert">
                    <strong>Không thành công</strong> <%=error%>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="<%=success != null%>">
                <div class="alert-success alert alert-dismissible fade show fixed-top" role="alert">
                    <strong>Thành công</strong> <%=success%>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <jsp:include page="common/footer.jsp"/>
        </div>

        <jsp:include page="common/tail.jsp"/>
        <script src="/assets/payment/jquery-1.11.3.min.js"></script>
    </body>
</html>
