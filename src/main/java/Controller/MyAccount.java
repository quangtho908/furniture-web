package Controller;

import DTO.AuthorizationData;
import DTO.UpdateUserDTO;
import Model.StatusAccount;
import Services.KeyService;
import Services.UserService;
import Model.User;
import Model.Keys;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "MyAccount", value = "/myAccount")
public class MyAccount extends HttpServlet {
    private UserService userService;
    private KeyService keyService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService("users");
        this.keyService = new KeyService("keys");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean logged = (boolean) request.getAttribute("logged");
      HttpSession session = request.getSession(true);
        if(!logged) {
            response.sendRedirect("/");
        }else {
            boolean errorChangePass = request.getParameter("errorChangePass") != null;
            if(errorChangePass) {
                request.setAttribute("errorChangePass", "Mật khẩu cũ không chính xác");
            }

            String success = (request.getParameter("success") == null) ? "" : request.getParameter("success");
            if(success.equals("changePass")) {
                request.setAttribute("success", "Thay đổi mật khẩu thành công");
            }else if(success.equals("changeInfo")){
                request.setAttribute("success", "Cập nhật thông tin thành công");
            }else if(success.equals("addKey")) {
                request.setAttribute("success", "Key đã được update");
            }
            AuthorizationData data = (AuthorizationData) session.getAttribute("authorization");
            User user = this.userService.findById(data.getId(), User.class);
            if(user.getPublicKey() > 0) {
              Keys key = this.keyService.findById(String.valueOf(user.getPublicKey()), Keys.class);
              request.setAttribute("publicKey", key.getValue());
            }

            request.getRequestDispatcher("/jsp/client/myAccount.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        if(!((boolean) request.getAttribute("logged"))) {
            response.sendRedirect("/login");
            return;
        }

        AuthorizationData data = (AuthorizationData) session.getAttribute("authorization");

        UpdateUserDTO dto = new UpdateUserDTO(
                request.getParameter("firstName"),
                request.getParameter("lastName"),
                request.getParameter("phone"),
                request.getParameter("country"),
                request.getParameter("city"),
                request.getParameter("district"),
                request.getParameter("address")
        );
         dto.setStatus(StatusAccount.ACTIVE.ordinal());

        this.userService.update(data.getId(), dto);

        response.sendRedirect("/myAccount?success=changeInfo");
    }
}
