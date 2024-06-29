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
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Класс-инициатор для работы с базой данных.
 * Читает свойства из properties файла.
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
            InputStream input = DatabaseInitiationService.class.getResourceAsStream("/config.properties");
            prop.load(input);
            this.dbUrl = prop.getProperty("db.url");
            this.dbUser = prop.getProperty("db.user");
            this.dbPassword = prop.getProperty("db.password");
            this.changeLogFile = prop.getProperty("liquibase.changeLogFile");
        } catch (IOException e) {
            ConsoleOutput.print(e.getMessage());
        }
    }

    /**
     *Создание новых схем в БД, и применение changelog Liquibase
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
