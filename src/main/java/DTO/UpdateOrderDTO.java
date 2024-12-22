package DTO;

import java.time.LocalDate;

public class UpdateOrderDTO {
  private String id;
  private String country;
  private String city;
  private String district;
  private String address;
  private String phone;
  private String email;
  private LocalDate updatedAt;
  public UpdateOrderDTO() {}

  public UpdateOrderDTO(String id, String country, String city, String district, String address, String phone, String email) {
    this.id = id;
    this.country = country;
    this.city = city;
    this.district = district;
    this.address = address;
    this.phone = phone;
    this.email = email;
    this.updatedAt = LocalDate.now();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDate updatedAt) {
    this.updatedAt = updatedAt;
  }
}
