package ru.ylab_learning.coworking.repository.impl;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
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
@Testcontainers
class ResourceRepositoryImplTest {
    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest");
    private static ResourceRepositoryImpl repository;
    private static String changeLogFile;

    @BeforeAll
    static void setUp() {
        postgresqlContainer.start();
        String dbUrl = postgresqlContainer.getJdbcUrl();
        String dbUser = postgresqlContainer.getUsername();
        String dbPassword = postgresqlContainer.getPassword();
        repository = new ResourceRepositoryImpl(dbUrl, dbUser, dbPassword);
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
    @DisplayName("Тест на успешное возвращение ресурса")
    void findByIdSuccess() {

        Optional<Resource> result = repository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }
    @Test
    @DisplayName("Тест на попытку возвращения ресурса, если он не существует")
    void findByIdReturnsEmpty() {

        Optional<Resource> result = repository.findById(-1L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Тест на успешное удаление ресурса")
    void deleteByIdSuccess() {

        Optional<Resource> result = repository.deleteById(6L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(6L);
    }
    @Test
    @DisplayName("Тест на попытку удаления ресурса, если он не существует")
    void deleteByIdReturnsEmpty() {

        Optional<Resource> result = repository.deleteById(-1L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Тест на успешное сохранение ресурса")
    void saveSuccess() {
        ResourceDTO resourceDTO  = new ResourceDTO(ResourceType.MEETING_ROOM, 123, "test", true);

        Resource resource  = repository.save(resourceDTO);

        assertThat(resource).isNotNull();
        assertThat(resource.getRentPrice()).isEqualTo(resourceDTO.getRentPrice());
    }

    @Test
    @DisplayName("Тест на успешное изменение ресурса")
    void update() {
        Resource resource = repository.findById(1L).get();
        int newPrice = resource.getRentPrice() + 351;
        resource.setRentPrice(newPrice);

        repository.update(resource);

        assertThat(resource.getRentPrice()).isEqualTo(newPrice);
    }

    @Test
    @DisplayName("Тест на успешное возвращение всех ресурсов")
    void findAll() {
        List<Resource> resultBefore = repository.findAll();
        ResourceDTO resourceDTO = new ResourceDTO(ResourceType.MEETING_ROOM, 123, "test", true);
        repository.save(resourceDTO);

        List<Resource> resultAfter = repository.findAll();

        assertThat(resultAfter.size()).isEqualTo(resultBefore.size()+1);
    }
}