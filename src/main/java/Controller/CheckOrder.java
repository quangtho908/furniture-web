package Controller;

import Model.LogsOrder;
import Model.MailContent;
import Model.Order;
import Model.User;
import Redis.RedisService;
import Services.LogsOrderService;
import Services.MailService;
import Services.OrderService;
import Services.UserService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/checkOrder")
public class CheckOrder extends HttpServlet {
  private LogsOrderService logsOrderService;
  private UserService userService;
  private OrderService orderService;
  private MailService mailService;
  @Override
  public void init() throws ServletException {
    super.init();
    this.logsOrderService= new LogsOrderService("logsOrder");
    this.userService=new UserService("users");
    this.orderService=new OrderService("orders");
    this.mailService=new MailService();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ArrayList<LogsOrder> logsOrders = this.logsOrderService.findAll(LogsOrder.class);
    for(LogsOrder logsOrder:logsOrders) {
      String readyForCancel = RedisService.instance.getValue(
              "readyForCancel_" + logsOrder.getUserId() + "_" + logsOrder.getOrderId()
      );
      String waitRemove = RedisService.instance.getValue(
              "waitRemove_" + logsOrder.getUserId() + "_" + logsOrder.getOrderId()
      );
      if(readyForCancel!=null && waitRemove == null) {
        this.orderService.cancelOrder(logsOrder.getOrderId());
        RedisService.instance.removeValue("waitingVerify_" + logsOrder.getUserId() + "_" + logsOrder.getOrderId());
        RedisService.instance.removeValue("readyForCancel_" + logsOrder.getUserId() + "_" + logsOrder.getOrderId());
        RedisService.instance.removeValue("hashForSign_" + logsOrder.getId() + "_" + logsOrder.getUserId());
        RedisService.instance.removeValue("remind_" + logsOrder.getId() + "_" + logsOrder.getUserId());
        sendMailCancel(logsOrder);
        this.logsOrderService.delete(logsOrder.getOrderId(), logsOrder.getUserId());
        continue;
      }

      String existRemind = RedisService.instance.getValue(
              "remind_" + logsOrder.getUserId() + "_" + logsOrder.getOrderId()
      );
      if(logsOrder.getAction() > 0 && existRemind == null) {
        sendMailRemind(logsOrder);
        RedisService.instance.setValueEx("remind_" + logsOrder.getUserId() + "_" + logsOrder.getOrderId(), "true", 3600*5);
        continue;
      } else if (logsOrder.getAction() >0) continue;

      String exist = RedisService.instance.getValue(
              "waitingVerify_" + logsOrder.getUserId() + "_" + logsOrder.getOrderId()
      );
      if(exist!=null) {
        continue;
      }
      sendMailWarning(logsOrder);
      RedisService.instance.setValueEx(
              "waitingVerify_" + logsOrder.getUserId() + "_" + logsOrder.getOrderId(), "true",
              7200
      );
      RedisService.instance.setValue("readyForCancel_" + logsOrder.getUserId() + "_" + logsOrder.getOrderId(), "true");
      RedisService.instance.setValueEx("waitRemove_" + logsOrder.getUserId() + "_" + logsOrder.getOrderId(), "true", 3600*12);
    }
  }

  private void sendMailWarning(LogsOrder logsOrder) {
    User user = this.userService.findById(logsOrder.getUserId(), User.class);
    Order order = this.orderService.findById(logsOrder.getOrderId(), Order.class);
    MailContent mailContent = new MailContent(
            "Đơn hàng của bạn bị thay đổi",
            "Mã đơn hàng: " + order.getId() + "\n" +
                    "Đã thay đổi lúc: " + logsOrder.getCreatedAt() + "\n\n" +
                    "Tên người đặt mới: " + order.getUsername() + "\n" +
                    "Tên người đặt cũ: " + logsOrder.getOldUsername() + "\n\n" +
                    "Thông tin sản phẩm mới: " + order.getInfo() + "\n" +
                    "Thông tin sản phẩm cũ: " + logsOrder.getOldInfo() + "\n\n" +
                    "Tổng giá mới: " + order.getPrice() + "\n" +
                    "Tổng giá cũ: " + logsOrder.getOldPrice() + "\n\n" +
                    "Ngày tạo đơn mới: " + order.getCreatedAt() + "\n" +
                    "Ngày tạo đơn cũ: " + logsOrder.getOldDateOrder() + "\n\n" +
                    "Quốc gia mới: " + order.getCountry() + "\n" +
                    "Quốc gia cũ: " + logsOrder.getOldCountry() + "\n\n" +
                    "Thành phố mới: " + order.getCity() + "\n" +
                    "Thành phố cũ: " + logsOrder.getOldCity() + "\n\n" +
                    "Quận/Huyện mới :" + order.getDistrict() + "\n" +
                    "Quận/Huyện cũ: " + logsOrder.getOldDistrict() + "\n\n" +
                    "Địa chỉ mới: " + order.getAddress() + "\n" +
                    "Địa chỉ cũ: " + logsOrder.getOldAddress() + "\n\n" +
                    "Số điện thoại mới: " + order.getPhone() + "\n" +
                    "Số điện thoại cũ: " + logsOrder.getOldPhone() + "\n\n" +
                    "Email mới: " + order.getEmail() + "\n" +
                    "Email cũ: " + logsOrder.getOldEmail() + "\n\n"
            );
    try {
      mailService.send(user.getEmail(), mailContent);
    } catch (MailjetSocketTimeoutException | MailjetException e) {
      throw new RuntimeException(e);
    }
  }

  private void sendMailRemind(LogsOrder logsOrder) {
    User user = this.userService.findById(logsOrder.getUserId(), User.class);
    Order order = this.orderService.findById(logsOrder.getOrderId(), Order.class);
    MailContent mailContent = new MailContent(
            "Vui lòng xác nhận đơn hàng của bạn",
            "Mã đơn hàng: " + order.getId() + "\n" +
                    "Huỷ lúc: " + logsOrder.getCreatedAt() + "\n\n" +
                    "Thông tin sản phẩm: " + order.getInfo() + "\n" +
                    "Tổng giá: " + order.getPrice() + "\n" +
                    "Ngày tạo đơn: " + order.getCreatedAt() + "\n"
    );
    try {
      mailService.send(user.getEmail(), mailContent);
    } catch (MailjetSocketTimeoutException | MailjetException e) {
      throw new RuntimeException(e);
    }
  }

  private void sendMailCancel(LogsOrder logsOrder) {
    User user = this.userService.findById(logsOrder.getUserId(), User.class);
    Order order = this.orderService.findById(logsOrder.getOrderId(), Order.class);
    MailContent mailContent = new MailContent(
            "Đơn hàng của bạn bị huỷ vì quá thời hạn xác nhận",
            "Mã đơn hàng: " + order.getId() + "\n" +
                    "Huỷ lúc: " + logsOrder.getCreatedAt() + "\n\n" +
                    "Thông tin sản phẩm: " + order.getInfo() + "\n" +
                    "Tổng giá: " + order.getPrice() + "\n" +
                    "Ngày tạo đơn: " + order.getCreatedAt() + "\n"
    );
    try {
      mailService.send(user.getEmail(), mailContent);
    } catch (MailjetSocketTimeoutException | MailjetException e) {
      throw new RuntimeException(e);
    }
  }
}
