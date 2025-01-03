<%@ page import="Model.Order" %><%--
  Created by IntelliJ IDEA.
  User: zxc
  Date: 06/Jan/23
  Time: 7:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored = "false" %>
<html
        lang="en"
        class="light-style layout-menu-fixed"
        dir="ltr"
        data-theme="theme-default"
        data-assets-path="../assets/"
        data-template="vertical-menu-template-free"
>
<head>
    <meta charset="utf-8"/>
    <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>

    <title>Chi tiết đơn hàng</title>

    <meta name="description" content=""/>

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
    />

    <!-- Icons. Uncomment required icon fonts -->
    <link rel="stylesheet" href="../assets/vendor/fonts/boxicons.css"/>

    <!-- Core CSS -->
    <link rel="stylesheet" href="../assets/vendor/css/style.css">
    <!-- Vendors CSS -->
    <link rel="stylesheet" href="../assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css"/>

    <!-- Page CSS -->

    <!-- Helpers -->
    <script src="../assets/vendor/js/helpers.js"></script>

    <script src="../assets/js_admin/config.js"></script>
</head>
<body>
<%
    Order order = (Order) request.getAttribute("order");
%>
<!-- Layout wrapper -->
<div class="layout-wrapper layout-content-navbar">
    <div class="layout-container">
        <!-- Menu -->

        <jsp:include page="common/menu.jsp"/>
        <!-- / Menu -->

        <!-- Layout container -->
        <div class="layout-page">
            <!-- Navbar -->

            <jsp:include page="common/navbar.jsp"/>

            <!-- / Navbar -->

            <!-- Content wrapper -->
            <div class="content-wrapper">
                <!-- Content -->
                <div class="container-xxl flex-grow-1 container-p-y">
                    <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Đơn hàng /</span> Chi tiết đơn hàng</h4>
<%--                    <hr class="my-5"/>--%>
                    <div class="card">
                        <h5 class="card-header" style="padding: 1rem 0 1rem 1.5rem">Chi tiết đơn hàng</h5>
                        <div class="container mt-5 mb-5" style="margin-top: 0.5rem !important;">
                            <div class="table-responsive">
                                <div class="form-group">
                                    <label >Mã đơn hàng:</label>
                                    <label><%=order.getId()%></label>
                                </div>
                                <div class="form-group">
                                    <label >Trạng thái:</label>
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
                                <form action="${pageContext.request.contextPath}/admin/adminOrderDetail?id=<%=order.getId()%>" method="post">
                                    <div class="form-group">
                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="country">Quốc gia</label>
                                            <div class="col-sm-10">
                                                <input
                                                    type="text"
                                                    class="form-control"
                                                    id="country"
                                                    placeholder="Quốc gia"
                                                    name="country"
                                                    value="<%=order.getCountry()%>"
                                                    required
                                                />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="city">Thành phố</label>
                                            <div class="col-sm-10">
                                                <input
                                                        type="text"
                                                        class="form-control"
                                                        id="city"
                                                        placeholder="Thành phố"
                                                        name="city"
                                                        value="<%=order.getCity()%>"
                                                        required
                                                />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="district">Quận/huyện</label>
                                            <div class="col-sm-10">
                                                <input
                                                        type="text"
                                                        class="form-control"
                                                        id="district"
                                                        placeholder="Quận/huyện"
                                                        name="district"
                                                        value="<%=order.getDistrict()%>"
                                                        required
                                                />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="address">Địa chỉ</label>
                                            <div class="col-sm-10">
                                                <input
                                                        type="text"
                                                        class="form-control"
                                                        id="address"
                                                        placeholder="Địa chỉ"
                                                        name="address"
                                                        value="<%=order.getAddress()%>"
                                                        required
                                                />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="email">Email</label>
                                            <div class="col-sm-10">
                                                <input
                                                        type="text"
                                                        class="form-control"
                                                        id="email"
                                                        placeholder="email"
                                                        name="email"
                                                        value="<%=order.getEmail()%>"
                                                        required
                                                />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="phone">Số điện thoại</label>
                                            <div class="col-sm-10">
                                                <input
                                                        type="text"
                                                        class="form-control"
                                                        id="phone"
                                                        placeholder="Số điện thoại"
                                                        name="phone"
                                                        value="<%=order.getPhone()%>"
                                                        required
                                                />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row justify-content-end">
                                        <div class="col-sm-10">
                                            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                                        </div>
                                    </div>
                                </form>
                            </div>

                        </div>
                    </div>
                </div>
                <!-- Footer -->
                <footer class="content-footer footer bg-footer-theme">
                    <div class="container-xxl d-flex flex-wrap justify-content-between py-2 flex-md-row flex-column">
                        <div class="mb-2 mb-md-0">
                            ©
                            <script>
                                document.write(new Date().getFullYear());
                            </script>
                            <a href="https://themeselection.com" target="_blank" class="footer-link fw-bolder"></a>
                        </div>

                    </div>
                </footer>
                <!-- / Footer -->

                <div class="content-backdrop fade"></div>
            </div>
            <!-- Content wrapper -->
        </div>
        <!-- / Layout page -->
    </div>

    <!-- Overlay -->
    <div class="layout-overlay layout-menu-toggle"></div>
</div>


<!-- Core JS -->
<!-- build:js assets/vendor/js/core.js -->
<script src="/assets/vendor/libs/jquery/jquery.js"></script>
<script src="/assets/vendor/libs/popper/popper.js"></script>
<script src="/assets/vendor/js/bootstrap.js"></script>
<script src="/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.8.7/chosen.jquery.min.js"></script>
<script src="/assets/vendor/js/menu.js"></script>
<script src="/assets/js/upload.js"></script>
<!-- endbuild -->

<!-- Vendors JS -->

<!-- Main JS -->
<script src="/assets/js_admin/main.js"></script>
<script>
    $(".chosen-select").chosen({
        allow_single_deselect: true,
        width: '50%',
        no_results_text: "Không tìm thấy kết quả :"
    })
</script>
<!-- Page JS -->

<!-- Place this tag in your head or just before your close body tag. -->
<script async defer src="https://buttons.github.io/buttons.js"></script>
</body>
</html>
