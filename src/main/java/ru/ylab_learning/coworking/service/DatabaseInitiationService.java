package ru.ylab_learning.coworking.service;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Data;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.util.SQLQueries;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * TODO
 */
@Data
public class DatabaseInitiationService {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String changeLogFile;

    public DatabaseInitiationService() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("src/main/resources/config.properties"));
            this.dbUrl = prop.getProperty("db.url");
            this.dbUser = prop.getProperty("db.user");
            this.dbPassword = prop.getProperty("db.password");
            this.changeLogFile = prop.getProperty("liquibase.changeLogFile");
        } catch (IOException e) {
            ConsoleOutput.print(e.getMessage());
        }
    }
    // for test containers TODO
//    public DatabaseInitiationService(String dbUrl) {
//        try {
//            Properties prop = new Properties();
//            prop.load(new FileInputStream("src/main/resources/config.properties"));
//            // Получение значений переменных
//            this.dbUrl = dbUrl;
//            this.dbUser = prop.getProperty("db.user");
//            this.dbPassword = prop.getProperty("db.password");
//            this.changeLogFile = prop.getProperty("liquibase.changeLogFile");
//            this.view = new Output();
//        } catch (IOException e) {
//            view.inform(e.getMessage());
//        }
//    }

    /**
     *Создание новых схем и применение changelog Liquibase
     */
    public void InitiateDatabaseWithLiquibase() {
        // Получение соединения
        try (Connection connection = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
             Statement statement = connection.createStatement()) {
            // создание схемы для Liquibase
            statement.executeUpdate(SQLQueries.CREATE_LIQUIBASE_SCHEMA_QUERY);

            // создание схемы для приложения
            statement.executeUpdate(SQLQueries.CREATE_APP_SCHEMA_QUERY);
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(this.changeLogFile, new ClassLoaderResourceAccessor(), database);

            // назначение схем
            database.setLiquibaseSchemaName("lb_special");
            database.setDefaultSchemaName("entities");

            liquibase.update();
        } catch (SQLException | LiquibaseException e) {
            ConsoleOutput.print(e.getMessage());
        }
    }

}
