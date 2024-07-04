package samatov.rest.api.config;

import org.flywaydb.core.Flyway;
import samatov.rest.api.utils.PropertiesUtil;

public class FlywayConfig {

    public static void main(String[] args) {

        String url = PropertiesUtil.getProperty("database.url");
        String user = PropertiesUtil.getProperty("database.user");
        String password = PropertiesUtil.getProperty("database.password");

        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .locations("classpath:db/changelog")
                .load();

        flyway.migrate();
    }
}
