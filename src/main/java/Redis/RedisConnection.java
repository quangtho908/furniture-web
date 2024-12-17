package Redis;
import redis.clients.jedis.*;

public class RedisConnection extends JedisPool{
  public RedisConnection(JedisPoolConfig config, String host, int port) {
    super(config, host, port);
  }
}
