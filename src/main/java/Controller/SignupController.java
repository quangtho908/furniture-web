package Controller;

import DTO.PayloadSignVerify;
import Model.MailContent;
import Model.StatusAccount;
import Model.TypeAccount;
import Model.User;
import Properties.LoadProperties;
import Redis.RedisService;
import Services.*;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Base64;

@WebServlet(name = "SignupController", value = "/signup")
public class SignupController extends HttpServlet {

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

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            if(email.isEmpty() || password.isEmpty()) {
                request.setAttribute ("error", "Email và mật khẩu không được bỏ trống");
                request.getRequestDispatcher("/jsp/client/authentication.jsp").forward(request, response);
                return;
            }
            if(this.userService.findByEmail(email) != null){
                request.setAttribute ("error", "Email của bạn đã được sử dụng");
                request.setAttribute("email", request.getParameter("email"));
                request.getRequestDispatcher("/jsp/client/authentication.jsp").forward(request, response);
            }else {
                String passwordHash = BCrypt.withDefaults().hashToString(8, password.toCharArray());
                User model = new User(email, passwordHash, StatusAccount.DISABLE.ordinal(), TypeAccount.USER.ordinal());

                String id = this.userService.create(model);
                String rand = RandomStringUtils.randomAlphabetic(16);

                PayloadSignVerify payloadSignVerify = new PayloadSignVerify(
                        rand,
                        "/verifyAccount",
                        id
                );
                String linkVerify = DigitalSignService.instance.getCredentials(payloadSignVerify);
                this.authenticationService.sendVerify(linkVerify, email);

                session.setAttribute("id", id);
                response.sendRedirect("/verifyAccount?waitVerify=true");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
