package Services;

import DTO.BaseDTO;
import Model.LogsOrder;

public class LogsOrderService extends BaseService<LogsOrder> {
  public LogsOrderService(String tableName) {
    super(tableName);
  }

  @Override
  protected String create(LogsOrder model) {
    return "";
  }

  @Override
  protected boolean update(String id, BaseDTO model) throws Exception {
    return false;
  }

  public void delete(String orderId, String userId) {
    this.jdbi.useHandle(handle -> {
      handle.createUpdate("DELETE FROM " + this.tableName + " WHERE orderId = :orderId AND userId = :userId")
              .bind("orderId", orderId)
              .bind("userId", userId)
              .execute();
    });
  }
}
