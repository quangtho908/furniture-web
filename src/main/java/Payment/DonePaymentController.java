package Payment;

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

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

@WebServlet(name = "DonePayment", value = "/payment/done")
public class DonePaymentController extends HttpServlet {
    private UserService userService;
    private OrderService orderService;
    private MailService mailService;
    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService("users");
        this.orderService = new OrderService("orders");
        this.mailService = new MailService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responseCode = request.getParameter("vnp_ResponseCode");
        String transId = request.getParameter("vnp_TxnRef");
        String vnp_transId = request.getParameter("vnp_TransactionNo");
        if (Objects.equals(responseCode, "00")) {
            HttpSession session = request.getSession(true);
            AuthorizationData authorizationData = (AuthorizationData) session.getAttribute("authorization");
            OrderDTO order = (OrderDTO) session.getAttribute("order");
            User user = ((boolean) request.getAttribute("logged")) ? (User) request.getAttribute("user") : (User) session.getAttribute("user");
            Order orderSave = new Order(order.getName(), order.getPrice(), vnp_transId,
                    user.getFirstName() + " " + user.getLastName(), user.getCountry(),
                    user.getCity(), user.getDistrict(), user.getAddress(), user.getPhone(), order.getEmail());
            if ((boolean) request.getAttribute("logged")) {
                orderSave.setUserId(authorizationData.getId());
                this.userService.removeAllCart(authorizationData.getId());
            }
            authorizationData.setCarts(new ArrayList<CartDTO>());

            orderSave.setId(transId);
            orderService.create(orderSave);
            session.setAttribute("authorization", authorizationData);
            session.removeAttribute("order");
            this.sendEmail(orderSave);
            response.sendRedirect("/orderDetail?id=" + orderSave.getId());

        }else {
            response.sendRedirect("/cart");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void sendEmail(Order order) {
        try {
            this.mailService.send(order.getEmail(), new MailContent(
                    "FURNIURE | X??C NH???N ????N H??NG",
                    "M?? ????n h??ng: " + order.getId() + "\n" +
                            "Th??ng tin ????n h??ng: " + order.getInfo() +"\n" +
                            "G??a ti???n: " + order.getPrice() +"\n" +
                            "M?? giao d???ch VNPAY: " + order.getTransID() + "\n" +
                            "T??n kh??ch h??ng: " + order.getUsername() + "\n" +
                            "Qu???c gia: " + order.getCountry() + "\n" +
                            "Th??nh ph???: " + order.getCity() + "\n" +
                            "Qu???n/Huy???n: " + order.getDistrict() + "\n" +
                            "?????a ch???: " + order.getAddress() + "\n" +
                            "S??? ??i???n tho???i: " + order.getPhone() + "\n" +
                            "Email: " + order.getEmail() + "\n"
            ));
        } catch (MailjetSocketTimeoutException e) {
            throw new RuntimeException(e);
        } catch (MailjetException e) {
            throw new RuntimeException(e);
        }
    }
}
