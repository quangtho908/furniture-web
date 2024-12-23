package Controller;

import DTO.AuthorizationData;
import Model.MailContent;
import Model.User;
import Services.KeyService;
import Services.MailService;
import Services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import Model.Keys;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

@WebServlet(name = "KeyController", value = "/keys")
public class KeyController extends HttpServlet {
  private KeyService keyService;
  private UserService userService;
  private MailService mailService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.keyService = new KeyService("keys");
    this.userService = new UserService("users");
    this.mailService = new MailService();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    boolean logged = (boolean) request.getAttribute("logged");
    if(!logged) {
      response.sendRedirect("/login");
      return;
    }

    HttpSession session = request.getSession(true);
    AuthorizationData authorizationData = (AuthorizationData) session.getAttribute("authorization");
    User user = this.userService.findById(authorizationData.getId(), User.class);
    String key = request.getParameter("publicKey");
    Keys publicKey = new Keys(key, authorizationData.getId());
    String keyId = this.keyService.create(publicKey);

    this.userService.updatePublicKey(Integer.parseInt(keyId), authorizationData.getId());
    MailContent mailContent = new MailContent("FURNITURE Thay đổi mã xác thực", "Tài khoản của bạn đã thay đổi mã xác thực");
    try {
      this.mailService.send(user.getEmail(), mailContent);
    } catch (MailjetSocketTimeoutException | MailjetException e) {
      throw new RuntimeException(e);
    }
    response.sendRedirect("/myAccount?success=addKey");
    return;

  }
}
