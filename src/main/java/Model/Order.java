package Model;

import DTO.PayloadSign;
import com.google.gson.Gson;

public class Order extends BaseModelUUID{
    private String info;
    private int price;
    private String transID;
    private String username;
    private String country;
    private String city;
    private String district;
    private String address;
    private String phone;
    private String email;
    private String userId;
    private int status;
    private int publicKey;
    public Order() {
    }

    public Order(String info, int price, String transId, String username, String country, String city, String district, String address, String phone, String email) {
        super();
        this.info = info;
        this.price = price;
        this.transID = transId;
        this.username = username;
        this.country = country;
        this.city = city;
        this.district = district;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public String getInfo() {
        return info;
    }

    public int getPrice() {
        return price;
    }

    public String getTransID() {
        return transID;
    }

    public String getUsername() {
        return username;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTransID(String transId) {
        this.transID = transId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPublicKey() {return publicKey;}

    public void setPublicKey(int publicKeyId) {
      this.publicKey = publicKeyId;
    }
    public int getStatus() {
      return this.status;
    }
    public String getStatusString() {
      String[] statuses = {"Chờ xác thực", "Đã xác thực", "Đã huỷ" ,"Đã hoàn thành"};

      return statuses[status];
    }

    public void setStatus(int status) {
      this.status = status;
    }

    public String geHashData() {

      PayloadSign payload = new PayloadSign(id, userId, info, price, createdAt);
      // payload => json
      Gson gson = new Gson();
      return gson.toJson(payload);
    }
}
