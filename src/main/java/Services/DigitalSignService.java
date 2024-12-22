package Services;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;

import DTO.PayloadSignVerify;
import Model.Order;
import Properties.LoadProperties;
import Redis.RedisService;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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
      loadKeys();
    } catch (Exception e) {

    }
    instance = this;
  }

  public String hash(Order order) {
    byte[] hmacResult = mac.doFinal(order.geHashData().getBytes());

    return bytesToHex(hmacResult);
  }

  public String hash(PayloadSignVerify payload) {
    byte[] hmacResult = mac.doFinal(payload.getHashData().getBytes());

    return bytesToBase64(hmacResult);
  }

  public String hash(String payload) {
    byte[] hmacResult = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
    return bytesToBase64(hmacResult);
  }

  public String sign(String hash) {
    try {
      Signature signature = Signature.getInstance("SHA256withDSA");
      byte[] data = hash.getBytes();
      PrivateKey privateKey = getPrivateKeySystem();
      signature.initSign(privateKey);
      signature.update(data);
      byte[] sign = signature.sign();

      return Base64.getUrlEncoder().encodeToString(sign);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
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
      // base64 => public key
      KeyFactory keyFactory = KeyFactory.getInstance("DSA");
      X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64));
      return keyFactory.generatePublic(publicSpec);
    } catch(Exception e) {
      return null;
    }
  }

  public void loadKeys() {
    String keyFolder = LoadProperties.properties.getProperty("keys.folder");
    File folders = new File(keyFolder);
    if (!folders.exists()) {
      folders.mkdir();
      _genKey();
    }
    getPublickeySystem();
    getPrivateKeySystem();
  }

  public PublicKey getPublickeySystem() {
    String publicFile = LoadProperties.properties.getProperty("keys.folder") + "/server.pub";
    File pubFile = new File(publicFile);
    try {
      if (!pubFile.exists()) {
        FileUtils.deleteDirectory(pubFile.getParentFile());
        loadKeys();
      }
      FileInputStream pubIn = new FileInputStream(pubFile);
      String pubKey = null;
      KeyFactory keyFactory = KeyFactory.getInstance("DSA");
      byte[] pubBytes = pubIn.readAllBytes();
      pubKey = new String(pubBytes, StandardCharsets.UTF_8).replaceAll("\\s+", "");
      if(pubKey.isBlank()) {
        FileUtils.deleteDirectory(pubFile.getParentFile());
        return getPublickeySystem();
      }
      X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.getDecoder().decode(pubKey));
      return keyFactory.generatePublic(publicSpec);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public PrivateKey getPrivateKeySystem() {
    String privateFile = LoadProperties.properties.getProperty("keys.folder") + "/server.key";
    File privFile = new File(privateFile);
    try {
      if (!privFile.exists()) {
        FileUtils.deleteDirectory(privFile.getParentFile());
        loadKeys();
      }
      FileInputStream pubIn = new FileInputStream(privFile);
      String privKey = null;
      KeyFactory keyFactory = KeyFactory.getInstance("DSA");
      byte[] pubBytes = pubIn.readAllBytes();
      privKey = new String(pubBytes, StandardCharsets.UTF_8).replaceAll("\\s+", "");
      if(privKey.isBlank()) {
        FileUtils.deleteDirectory(privFile.getParentFile());
        return getPrivateKeySystem();
      }

      PKCS8EncodedKeySpec privateSpec= new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privKey));
      return keyFactory.generatePrivate(privateSpec);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void _genKey() {
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
      keyGen.initialize(1024);
      KeyPair keyPair = keyGen.generateKeyPair();

      String publicFile = LoadProperties.properties.getProperty("keys.folder") + "/server.pub";
      String privateFile = LoadProperties.properties.getProperty("keys.folder") + "/server.key";
      PublicKey publicKey = keyPair.getPublic();
      PrivateKey privateKey = keyPair.getPrivate();

      FileOutputStream pubOut = new FileOutputStream(publicFile);
      FileOutputStream privOut = new FileOutputStream(privateFile);
      pubOut.write(Base64.getEncoder().encode(publicKey.getEncoded()));
      pubOut.close();
      privOut.write(Base64.getEncoder().encode(privateKey.getEncoded()));
      privOut.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public String getCredentials(PayloadSignVerify payloadSignVerify) {
    String hashData = hash(payloadSignVerify);
    String signSystem = sign(hashData);
    SecretKey secretKey = AESService.instance.getSecretKey(payloadSignVerify.getCode());
    IvParameterSpec iv = AESService.instance.getIv();
    String encryptData = AESService.instance.encrypt(
            payloadSignVerify.getHashData(),
            secretKey,
            iv
    );
    RedisService.instance.setValueEx("verify_" + payloadSignVerify.getUserId() + "_hash", hashData, 300);
    RedisService.instance.setValueEx("verify_iv_" + payloadSignVerify.getUserId() , Base64.getEncoder().encodeToString(iv.getIV()), 300);
    RedisService.instance.setValueEx("verify_key_" + payloadSignVerify.getUserId() , Base64.getEncoder().encodeToString(secretKey.getEncoded()), 300);

    return LoadProperties.properties.getProperty("application.host") + "/verify?credentials=" + encryptData + "&sign=" + signSystem;
  }

  private String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private String bytesToBase64(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }
  private byte[] decodeBase64(String base64Url) {
    return Base64.getDecoder().decode(base64Url);
  }
}
