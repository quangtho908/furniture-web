import Database.DBConnection;

import javax.servlet.*;
import javax.servlet.annotation.*;
import Redis.RedisService;

@WebListener
public class Application implements ServletContextListener{

    public Application() {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        new DBConnection();
        System.out.println("MySQL is connected");
        new RedisService();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
