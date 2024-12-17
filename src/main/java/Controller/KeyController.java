package Controller;

import DTO.AuthorizationData;
import Model.User;
import Services.KeyService;
import Services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import Model.Keys;

@WebServlet(name = "KeyController", value = "/keys")
public class KeyController extends HttpServlet {
  private KeyService keyService;
  private UserService userService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.keyService = new KeyService("keys");
    this.userService = new UserService("users");
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
    String key = request.getParameter("publicKey");
    Keys publicKey = new Keys(key, authorizationData.getId());
    String keyId = this.keyService.create(publicKey);

    this.userService.updatePublicKey(Integer.parseInt(keyId), authorizationData.getId());
    response.sendRedirect("/myAccount?success=addKey");
    return;

  }
}
