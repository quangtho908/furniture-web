package Controller;

import DTO.PayloadSignVerify;
import Redis.RedisService;
import Services.AESService;
import Services.DigitalSignService;
import com.google.gson.Gson;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;

@WebServlet(name = "VerifyController", value = "/verify")
public class VerifyController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(true);
    String id = (String) session.getAttribute("id");
    if(id==null || id.isEmpty()){
      resp.sendRedirect("/login");
      return;
    }
    String encryptedData = req.getParameter("credentials");
    String sign = req.getParameter("sign");
    if(encryptedData==null || encryptedData.isEmpty() || sign==null || sign.isEmpty()){
      resp.sendRedirect("/login");
      return;
    }

    String iv = RedisService.instance.getValue("verify_iv_" + id);
    String key = RedisService.instance.getValue("verify_key_" + id);
    if(iv == null || iv.isEmpty() || key == null || key.isEmpty()){
      resp.sendRedirect("/login");
      return;
    }

    IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
    SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
    String decryptData = AESService.instance.decrypt(encryptedData, secretKeySpec, ivParameterSpec);
    Gson gson = new Gson();
    PayloadSignVerify payload = gson.fromJson(decryptData, PayloadSignVerify.class);
    if (payload.getRedirect() == null || payload.getRedirect().isEmpty()){
      resp.sendRedirect("/login");
    }
    String hash = RedisService.instance.getValue("verify_" + id + "_hash");
    if(hash==null || hash.isEmpty() ){
      resp.sendRedirect(payload.getRedirect() + "?error=true");
      return;
    }
    PublicKey publicKey = DigitalSignService.instance.getPublickeySystem();
    String encodeSign = Base64.getEncoder().encodeToString(Base64.getUrlDecoder().decode(sign));
    boolean validSign = DigitalSignService.instance.verify(hash, encodeSign, publicKey);
    if(!validSign){
      resp.sendRedirect("/login");
      return;
    }

    String newHash = DigitalSignService.instance.hash(payload);
    if(newHash.equals(hash)){
      RedisService.instance.removeValue("verify_" + payload.getUserId() + "_hash");
      RedisService.instance.removeValue("verify_iv_" + payload.getUserId());
      RedisService.instance.removeValue("verify_key_" + payload.getUserId());
      resp.sendRedirect(payload.getRedirect() + "?success=true");
      return;
    }
    resp.sendRedirect(payload.getRedirect() + "?error=true");
  }
}
