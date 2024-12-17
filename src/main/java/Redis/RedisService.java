package Redis;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;

public class RedisService {
  private Jedis pool;
  public static RedisService instance;
  public RedisService() {
    pool = new RedisConnection(new JedisPoolConfig(), "localhost", 6379).getResource();
    instance = this;
  }

  public String getValue(String key) {
    return pool.get(key);
  }

  public void setValue(String key, String value) {
    pool.set(key, value);
  }

  public void setValueEx(String key, String value, long time) {
    pool.set(key, value, SetParams.setParams().ex(time));
  }

  public void removeValue(String key) {
    pool.del(key);
  }
}
