package DTO;

import com.google.gson.Gson;

import java.time.LocalDateTime;

public class PayloadSignVerify {
  private String userId;
  private String code;
  private String redirect;
  private LocalDateTime createdAt;

  public PayloadSignVerify(String code, String redirect, String userId) {
    this.code = code;
    this.redirect = redirect;
    this.userId = userId;
    this.createdAt = LocalDateTime.now();
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getRedirect() {
    return redirect;
  }

  public void setRedirect(String redirect) {
    this.redirect = redirect;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getHashData() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
