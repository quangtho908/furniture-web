package Controller;

import Model.Order;
import Services.OrderService;
import Services.DigitalSignService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import Redis.RedisService;

@WebServlet(name="OrderDetailController", value="/orderDetail")
public class OrderDetailController extends HttpServlet {
    private OrderService orderService;
    @Override
    public void init() throws ServletException {
        super.init();
        this.orderService = new OrderService("orders");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        boolean logged = (boolean) request.getAttribute("logged");
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        if(success != null) {
          request.setAttribute("success", "Xác nhận chữ ký thành công");
        }
        if(error != null) {
          request.setAttribute("error", "Chữ ký không chính xác, xác nhận thất bại");
        }
        if(id == null) {
            response.sendRedirect("/");
            return;
        }
        Order order = this.orderService.findById(id, Order.class);
        String hash = DigitalSignService.instance.hash(order);
        if(logged && hash != null && order.getStatus() == 0) {
          RedisService.instance.setValueEx("hashForSign_" + order.getId() + "_" + order.getUserId(), hash, 600);
          RedisService.instance.setValue("readyForCancel_" + order.getUserId() + "_" + order.getId(), "true");
          RedisService.instance.setValueEx("waitRemove_" + order.getUserId() + "_" + order.getId(), "true", 3600*12);
          RedisService.instance.setValueEx("remind_" + order.getUserId() + "_" + order.getId(), "true", 3600*6);
          request.setAttribute("hash", hash);
        }
        request.setAttribute("order", order);
        request.getRequestDispatcher("/jsp/client/orderDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}
