package Controller;

import DTO.AuthorizationData;
import DTO.VerifyOrderDTO;
import Model.Keys;
import Services.*;
import Model.Order;
import Model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.PublicKey;
import Redis.RedisService;

@WebServlet(name = "VerifySignController", value = "/verifySign")
public class VerifySign extends HttpServlet {
  private KeyService keyService;
  private UserService userService;
  private OrderService orderService;
  private LogsOrderService logsOrderService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.keyService = new KeyService("keys");
    this.userService = new UserService("users");
    this.orderService = new OrderService("orders");
    this.logsOrderService = new LogsOrderService("logsOrder");
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    boolean logged = (boolean) request.getAttribute("logged");
    String orderId = request.getParameter("orderId");
    if(!logged) {
      response.sendRedirect("/orderDetail?id=" + orderId);
      return;
    }
    User user = (User) request.getAttribute("user");
    if(user.getPublicKey() == 0) {
      response.sendRedirect("/orderDetail?id=" + orderId);
      return;
    }
    String hash = RedisService.instance.getValue("hashForSign_" + orderId + "_" + user.getId());
    if(hash == null) {
      response.sendRedirect("/orderDetail?id=" + orderId + "&error=true");
      return;
    }
    String sign = request.getParameter("sign");
    Order order = this.orderService.findById(orderId, Order.class);
    Keys key;
    if(order.getPublicKey() > 0) {
      key = this.keyService.findById(String.valueOf(order.getPublicKey()), Keys.class);
    } else {
      key = this.keyService.findById(String.valueOf(user.getPublicKey()), Keys.class);
    }
    PublicKey pubKey = DigitalSignService.instance.getPublicKey(key.getValue());
    boolean signIsValid = DigitalSignService.instance.verify(hash, sign, pubKey);
    if(!signIsValid) {
      response.sendRedirect("/orderDetail?id=" + orderId + "&error=true");
      return;
    }

    VerifyOrderDTO dto = new VerifyOrderDTO(orderId, sign, key.getId());

    this.orderService.verifyOrder(dto);

    RedisService.instance.removeValue("hashForSign_" + orderId + "_" + user.getId());

    this.logsOrderService.delete(orderId, user.getId());
    response.sendRedirect("/orderDetail?id=" + orderId + "&success=true");

  }
}
