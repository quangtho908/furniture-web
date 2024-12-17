package Services;
import DTO.BaseDTO;
import Model.Contact;
import Model.Keys;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.statement.Update;
public class KeyService extends BaseService<Keys> {

  public KeyService(String tableName) {super(tableName);}

  @Override
  public String create(Keys model) {
    return this.jdbi.withHandle(handle -> {
      Update update = handle.createUpdate(
        "INSERT INTO `" + this.tableName + "` (value, createdBy) " + "VALUES (:value, :createdBy)"
      ).bindBean(model);

      return String.valueOf(update.executeAndReturnGeneratedKeys("id")
              .mapTo(int.class)
              .findOnly());
    });
  }

  @Override
  public boolean update(String id, BaseDTO model) throws Exception {
    return true;
  }
}
