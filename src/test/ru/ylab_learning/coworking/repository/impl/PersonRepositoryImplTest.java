package ru.ylab_learning.coworking.repository.impl;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab_learning.coworking.domain.dto.PersonDTO;
import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.enums.PersonRole;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.out.ConsoleOutput;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Testcontainers
class PersonRepositoryImplTest {
    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest");
    private static PersonRepositoryImpl repository;
    private static String changeLogFile;

    @BeforeAll
    static void setUp() {
        postgresqlContainer.start();
        String dbUrl = postgresqlContainer.getJdbcUrl();
        String dbUser = postgresqlContainer.getUsername();
        String dbPassword = postgresqlContainer.getPassword();
        repository = new PersonRepositoryImpl(dbUrl, dbUser, dbPassword);
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("src/main/resources/config.properties"));
            changeLogFile = prop.getProperty("liquibase.changeLogFile");
        } catch (IOException e) {
            ConsoleOutput.print(e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS lb_special");
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS entities");

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);

            database.setLiquibaseSchemaName("lb_special");
            database.setDefaultSchemaName("entities");

            liquibase.update();

        } catch (SQLException | LiquibaseException e) {
            ConsoleOutput.print(e.getMessage());
        }
    }
    @AfterAll
    static void tearDown() {
        postgresqlContainer.stop();
    }

    @Test
    @DisplayName("Тест на успешное получение пользователя по логину")
    void findByLoginSuccess() {
        Optional<Person> person = repository.findByLogin("user");

        assertThat(person).isPresent();
        assertThat(person.get().getLogin()).isEqualTo("user");
        assertThat(person.get().getRole()).isEqualTo(PersonRole.USER);
    }
    @Test
    @DisplayName("Тест на попытку возвращения пользователя по логину, если он не существует")
    void findByLoginReturnsEmpty() {
        Optional<Person> person = repository.findByLogin("test_test");

        assertThat(person).isEmpty();
    }

    @Test
    @DisplayName("Тест на успешное сохранение пользователя")
    void save() {
        PersonDTO dto = new PersonDTO("login", "password", "test_2test", "e@mail.com");

        Person person  = repository.save(dto);

        assertThat(person).isNotNull();
        assertThat(person.getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    @DisplayName("Тест на успешное возвращение всех пользователей")
    void findAll() {
        List<Person> resultBefore = repository.findAll();
        PersonDTO dto = new PersonDTO("login", "password", "test_test", "e@mail.com");
        repository.save(dto);

        List<Person> resultAfter = repository.findAll();

        assertThat(resultAfter.size()).isEqualTo(resultBefore.size()+1);
    }

    @Test
    @DisplayName("Тест на успешное возвращение пользователя по id")
    void findByIdSuccess() {

        Optional<Person> result = repository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }
    @Test
    @DisplayName("Тест на попытку возвращения пользователя по id, если он не существует")
    void findByIdReturnsEmpty() {

        Optional<Person> result = repository.findById(-1L);

        assertThat(result).isEmpty();
    }
}