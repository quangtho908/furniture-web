package Controller;

import DTO.UpdateOrderDTO;
import Model.Order;
import Services.OrderService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AdminOrderDetail", value = "/admin/adminOrderDetail")
public class AdminOrderDetail extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        orderService = new OrderService("orders");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Order order = this.orderService.findById(id, Order.class);
        request.setAttribute("order",order);
        request.getRequestDispatcher("/jsp/admin/orderDetail.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String id = request.getParameter("id");
      if (id == null || id.equals("")) {
        response.sendRedirect("/admin/order");
        return;
      }
      String country = request.getParameter("country");
      String city = request.getParameter("city");
      String district = request.getParameter("district");
      String address = request.getParameter("address");
      String phone = request.getParameter("phone");
      String email = request.getParameter("email");
      UpdateOrderDTO dto = new UpdateOrderDTO(id, country,city,district,address,phone,email);

      this.orderService.adminUpdateOrder(dto);
      response.sendRedirect("/admin/adminOrderDetail?id=" + id);
    }
}
