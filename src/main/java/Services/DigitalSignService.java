package Services;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import Model.Order;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DigitalSignService {
  private Mac mac;
  public static DigitalSignService instance;
  public DigitalSignService(){
    try {
      mac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec = new SecretKeySpec("furnitureSceret".getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      mac.init(secretKeySpec);
    } catch (Exception e) {

    }
    instance = this;
  }

  public String hash(Order order) {
    byte[] hmacResult = mac.doFinal(order.geHashData().getBytes());

    return bytesToHex(hmacResult);
  }

  public boolean verify(String hash, String sign, PublicKey publicKey) {
    try {
      Signature signature = Signature.getInstance("SHA256withDSA");
      signature.initVerify(publicKey);
      byte[] data = hash.getBytes();
      byte[] signValue = Base64.getDecoder().decode(sign);

      signature.update(data);

      return signature.verify(signValue);

    } catch (Exception e) {
      return false;
    }
  }

  public PublicKey getPublicKey(String base64) {
    try{
      KeyFactory keyFactory = KeyFactory.getInstance("DSA");
      X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64));
      return keyFactory.generatePublic(publicSpec);
    } catch(Exception e) {
      return null;
    }
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
