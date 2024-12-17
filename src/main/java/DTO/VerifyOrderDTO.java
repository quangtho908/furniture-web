package DTO;

import java.time.LocalDate;

public class VerifyOrderDTO {
  private String orderId;
  private String sign;
  private int publicKey;
  private LocalDate updatedAt;
  public VerifyOrderDTO(String orderId, String sign, int publicKey) {
    this.orderId = orderId;
    this.sign = sign;
    this.publicKey = publicKey;
    this.updatedAt = LocalDate.now();
  }

  // Default Constructor
  public VerifyOrderDTO() {
  }

  // Getters
  public String getOrderId() {
    return orderId;
  }

  public String getSign() {
    return sign;
  }

  public int getPublicKey() {
    return publicKey;
  }

  public LocalDate getUpdatedAt() {
    return this.updatedAt;
  }

  // Setters
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public void setPublicKey(int publicKey) {
    this.publicKey = publicKey;
  }
}
