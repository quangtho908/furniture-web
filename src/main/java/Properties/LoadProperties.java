package Properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {
  public static Properties properties = new Properties();
  public LoadProperties() {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
      if (inputStream == null) {
        throw new IOException("Tệp application.properties không tìm thấy!");
      }

      properties.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
