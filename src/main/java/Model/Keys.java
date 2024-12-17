package Model;

public class Keys extends BaseModel {

  private String value;

  private String createdBy;

  public Keys() {}

  public Keys(String value, String createdBy) {
    this.value = value;
    this.createdBy = createdBy;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }
}
