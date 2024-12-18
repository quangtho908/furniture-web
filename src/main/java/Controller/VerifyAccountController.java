package Controller;

import DTO.AuthorizationData;
import DTO.PayloadSignVerify;
import DTO.UpdateUserDTO;
import Model.StatusAccount;
import Model.User;
import Services.AuthenticationService;
import Services.DigitalSignService;
import Services.UserService;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "VerifyAccountController", value = "/verifyAccount")
public class VerifyAccountController extends HttpServlet {

    private UserService userService;
    private AuthenticationService authenticationService;
    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService("users");
        this.authenticationService = new AuthenticationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession(true);
      String id = (String) session.getAttribute("id");

      String error = request.getParameter("error");
      if (error != null) {
        request.setAttribute("error", "Link xác thực đã hết hạn vui lòng thử lại");
      }
      String success = request.getParameter("success");
      User user = this.userService.findById(id, User.class);

      if (success != null && user != null) {
        user.setStatus(StatusAccount.ACTIVE.ordinal());
        UpdateUserDTO dto = new UpdateUserDTO(user);
        this.userService.update(id, dto);
        session.removeAttribute("id");
        session.removeAttribute(user.getEmail());
        session.setAttribute("authorization", new AuthorizationData(id, user.getType()));
        request.setAttribute("success", "Tài khoản của bạn đã được xác thực thành công");
      }
      String waitVerify = request.getParameter("waitVerify");
      if (waitVerify != null) {
        request.setAttribute("waitVerify", true);
      }
      request.getRequestDispatcher("/jsp/client/verifyAccount.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String id = (String) session.getAttribute("id");
        User user = this.userService.findById(id, User.class);

        String rand = RandomStringUtils.randomAlphabetic(16);
        PayloadSignVerify payloadSignVerify = new PayloadSignVerify(
                rand,
                "/verifyAccount",
                id
        );
        String linkVerify = DigitalSignService.instance.getCredentials(payloadSignVerify);
        this.authenticationService.sendVerify(linkVerify, user.getEmail());
        session.setAttribute("id", id);
        response.sendRedirect("/verifyAccount?waitVerify=true");

    }
}
