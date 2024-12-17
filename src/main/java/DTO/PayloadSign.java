package DTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class PayloadSign {
  private String id;
  private String userId;
  private String info;
  private int price;
  private LocalDate createdAt;
  private LocalDateTime date;
  public PayloadSign(String id, String userId, String info, int price, LocalDate createdAt) {
    this.id = id;
    this.userId = userId;
    this.info = info;
    this.price = price;
    createdAt = LocalDate.now();
    date = LocalDateTime.now();
  }
}
