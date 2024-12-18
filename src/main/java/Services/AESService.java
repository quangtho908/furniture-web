package Services;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESService {
  public static AESService instance;

  public AESService() {
    instance = this;
  }

  public String encrypt(String plainText, SecretKey secretKey, IvParameterSpec iv) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
      byte[] encrypted = cipher.doFinal(plainText.getBytes());

      return Base64.getUrlEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  public String decrypt(String encryptedText, SecretKey secretKey, IvParameterSpec iv) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
      byte[] decrypted = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedText));
      return new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public SecretKey getSecretKey(String key) {
    return new SecretKeySpec(key.getBytes(), "AES");
  }

  public IvParameterSpec getIv() {
    byte[] iv = new byte[16];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(iv);
    return new IvParameterSpec(iv);
  }
}
