package Controller;

import DTO.AuthorizationData;
import DTO.CartDTO;
import DTO.OrderDTO;
import Model.MailContent;
import Model.Order;
import Model.User;
import Services.MailService;
import Services.OrderService;
import Services.UserService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "CodOrderController", value = "/codOrder")
public class CodOrderController extends HttpServlet {
    private UserService userService;
    private MailService mailService;
    private OrderService orderService;
    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService("users");
        this.mailService = new MailService();
        this.orderService = new OrderService("orders");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        AuthorizationData authorizationData = (AuthorizationData) session.getAttribute("authorization");
        OrderDTO order = (OrderDTO) session.getAttribute("order");
        User user = ((boolean) request.getAttribute("logged")) ? (User) request.getAttribute("user") : (User) session.getAttribute("user");
        Order orderSave = new Order();
        String rand = RandomStringUtils.randomAlphabetic(10);
        orderSave.setEmail(order.getEmail());
        orderSave.setInfo(order.getName());
        orderSave.setPrice(order.getPrice());
        orderSave.setCountry(user.getCountry());
        orderSave.setCity(user.getCity());
        orderSave.setDistrict(user.getDistrict());
        orderSave.setAddress(user.getAddress());
        orderSave.setPhone(user.getPhone());
        orderSave.setUsername(user.getFirstName() + " " + user.getLastName());
        orderSave.setId(rand);

        if ((boolean) request.getAttribute("logged")) {
            orderSave.setUserId(authorizationData.getId());
            this.userService.removeAllCart(authorizationData.getId());
        }
        authorizationData.setCarts(new ArrayList<CartDTO>());

        this.orderService.create(orderSave);
        session.setAttribute("authorization", authorizationData);
        session.removeAttribute("order");
        this.sendEmail(orderSave);
        response.sendRedirect("/orderDetail?id=" + orderSave.getId());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void sendEmail(Order order) {
        try {
            this.mailService.send(order.getEmail(), new MailContent(
                    "FURNIURE | XÁC NHẬN ĐƠN HÀNG",
                    "Mã đơn hàng: " + order.getId() + "\n" +
                            "Thông tin đơn hàng: " + order.getInfo() +"\n" +
                            "Gía tiền: " + order.getPrice() +"\n" +
                            "Tên khách hàng: " + order.getUsername() + "\n" +
                            "Quốc gia: " + order.getCountry() + "\n" +
                            "Thành phố: " + order.getCity() + "\n" +
                            "Quận/Huyện: " + order.getDistrict() + "\n" +
                            "Địa chỉ: " + order.getAddress() + "\n" +
                            "Số điện thoại: " + order.getPhone() + "\n" +
                            "Email: " + order.getEmail() + "\n"
            ));
        } catch (MailjetSocketTimeoutException e) {
            throw new RuntimeException(e);
        } catch (MailjetException e) {
            throw new RuntimeException(e);
        }
    }
}
