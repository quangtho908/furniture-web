package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogsOrder implements IModel {
  private int id;
  private String userId;
  private String orderId;
  private String oldInfo;
  private String oldPrice;
  private LocalDate oldDateOrder;
  private LocalDateTime createdAt;
  private String oldCountry;
  private String oldCity;
  private String oldDistrict;
  private String oldAddress;
  private String oldPhone;
  private String oldEmail;
  private String oldUsername;
  private int action;
  
  public LogsOrder() {}
  
  public LogsOrder(int id, String userId, String orderId, String oldInfo, String oldPrice, LocalDate oldDateOrder) {
    this.id = id;
    this.userId = userId;
    this.orderId = orderId;
    this.oldInfo = oldInfo;
    this.oldPrice = oldPrice;
    this.oldDateOrder = oldDateOrder;
    this.createdAt = LocalDateTime.now();
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getOrderId() {
    return orderId;
  }
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getOldInfo() {
    return oldInfo;
  }
  public void setOldInfo(String oldInfo) {
    this.oldInfo = oldInfo;
  }

  public String getOldPrice() {
    return oldPrice;
  }
  public void setOldPrice(String oldPrice) {
    this.oldPrice = oldPrice;
  }

  public LocalDate getOldDateOrder() {
    return oldDateOrder;
  }
  public void setOldDateOrder(LocalDate oldDateOrder) {
    this.oldDateOrder = oldDateOrder;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public void setUpdatedAt() {}

  public int getAction() {
    return action;
  }

  public void setAction(int action) {
    this.action = action;
  }

  public String getOldCountry() {
    return oldCountry;
  }

  public void setOldCountry(String oldCountry) {
    this.oldCountry = oldCountry;
  }

  public String getOldCity() {
    return oldCity;
  }

  public void setOldCity(String oldCity) {
    this.oldCity = oldCity;
  }

  public String getOldDistrict() {
    return oldDistrict;
  }

  public void setOldDistrict(String oldDistrict) {
    this.oldDistrict = oldDistrict;
  }

  public String getOldAddress() {
    return oldAddress;
  }

  public void setOldAddress(String oldAddress) {
    this.oldAddress = oldAddress;
  }

  public String getOldPhone() {
    return oldPhone;
  }

  public void setOldPhone(String oldPhone) {
    this.oldPhone = oldPhone;
  }

  public String getOldEmail() {
    return oldEmail;
  }

  public void setOldEmail(String oldEmail) {
    this.oldEmail = oldEmail;
  }

  public String getOldUsername() {
    return oldUsername;
  }

  public void setOldUsername(String oldUsername) {
    this.oldUsername = oldUsername;
  }
}
