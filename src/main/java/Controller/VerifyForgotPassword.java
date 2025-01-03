package Controller;

import DTO.AuthorizationData;
import DTO.PayloadSignVerify;
import DTO.UpdateUserDTO;
import Model.StatusAccount;
import Model.User;
import Services.AuthenticationService;
import Services.DigitalSignService;
import Services.UserService;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "VerifyForgotPassword", value = "/verifyForgotPassword")
public class VerifyForgotPassword extends HttpServlet {

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
      String error = request.getParameter("error");
      if (error != null) {
        request.setAttribute("error", "Link xác thực đã hết hạn vui lòng thử lại");
      }
      String success = request.getParameter("success");

      if (success != null) {
        request.setAttribute("success", "Xác thực thành công hãy thực hiện đổi mật khẩu");
      }
      String waitVerify = request.getParameter("waitVerify");
      if (waitVerify != null) {
        request.setAttribute("waitVerify", true);
      }

      request.getRequestDispatcher("/jsp/client/verifyForgotPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String id = (String) session.getAttribute("id");
        String action = request.getParameter("action");
        User user = this.userService.findById(id, User.class);

        switch (action){
            case "resend":
            {
                String rand = RandomStringUtils.randomAlphabetic(16);
                PayloadSignVerify payloadSignVerify = new PayloadSignVerify(
                        rand,
                        "/verifyForgotPassword",
                        id
                );
                String linkVerify = DigitalSignService.instance.getCredentials(payloadSignVerify);
                this.authenticationService.sendVerify(linkVerify, user.getEmail());
                session.setAttribute("id", id);
                response.sendRedirect("/verifyForgotPassword?waitVerify=true");
            }break;
            case "verify":
            {
                String newPassword = request.getParameter("password");
                this.userService.resetPassword(user.getId(), newPassword);
                session.removeAttribute("id");
                response.sendRedirect("/CompleteForgotPassword");
            }break;
        }
    }
}
