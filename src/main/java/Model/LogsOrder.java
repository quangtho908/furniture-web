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
}
