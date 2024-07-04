package samatov.rest.api.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import samatov.rest.api.model.Event;
import samatov.rest.api.model.File;
import samatov.rest.api.model.User;
import samatov.rest.api.utils.PropertiesUtil;


public class HibernateConfig {

    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            String url = PropertiesUtil.getProperty("database.url");
            String user = PropertiesUtil.getProperty("database.user");
            String password = PropertiesUtil.getProperty("database.password");

            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", url);
            configuration.setProperty("hibernate.connection.username", user);
            configuration.setProperty("hibernate.connection.password", password);
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.use_sql_comments", "true");


            configuration.addAnnotatedClass(Event.class);
            configuration.addAnnotatedClass(File.class);
            configuration.addAnnotatedClass(User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There was an error building the factory");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
