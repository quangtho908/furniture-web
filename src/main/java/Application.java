import Database.DBConnection;

import javax.servlet.*;
import javax.servlet.annotation.*;

import Properties.LoadProperties;
import Services.AESService;
import Services.DigitalSignService;
import Redis.RedisService;

@WebListener
public class Application implements ServletContextListener{

    public Application() {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        new DBConnection();
        System.out.println("MySQL is connected");
        new LoadProperties();
        new DigitalSignService();
        new RedisService();
        new AESService();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
