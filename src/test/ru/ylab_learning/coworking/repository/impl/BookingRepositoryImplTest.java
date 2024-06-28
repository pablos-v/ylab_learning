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
import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.out.ConsoleOutput;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class BookingRepositoryImplTest {
    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest");
    private static BookingRepositoryImpl repository;
    private static String changeLogFile;
    private static BookingDTO dto;

    @BeforeAll
    static void setUp() {
        postgresqlContainer.start();
        String dbUrl = postgresqlContainer.getJdbcUrl();
        String dbUser = postgresqlContainer.getUsername();
        String dbPassword = postgresqlContainer.getPassword();
        repository = new BookingRepositoryImpl(dbUrl, dbUser, dbPassword);
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("src/main/resources/config.properties"));
            changeLogFile = prop.getProperty("liquibase.changeLogFile");
        } catch (IOException e) {
            ConsoleOutput.print(e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword); Statement statement =
                connection.createStatement()) {
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS lb_special");
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS entities");

            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);

            database.setLiquibaseSchemaName("lb_special");
            database.setDefaultSchemaName("entities");

            liquibase.update();

        } catch (SQLException | LiquibaseException e) {
            ConsoleOutput.print(e.getMessage());
        }
        dto = new BookingDTO();
        dto.setPersonId(2L);
        dto.setResourceId(3L);
        dto.setDate(LocalDate.now());
        dto.setStartTime(LocalTime.now());
        dto.setEndTime(dto.getStartTime().plusHours(1));
    }

    @AfterAll
    static void tearDown() {
        postgresqlContainer.stop();
    }

    @Test
    @DisplayName("Тест на успешное возвращение бронирования")
    void findByIdSuccess() {

        Optional<Booking> result = repository.findById(4L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("Тест на попытку возвращения бронирования, если его не существует")
    void findByIdReturnsEmpty() {

        Optional<Booking> result = repository.findById(-1L);

        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("Тест на успешное сохранение бронирования")
    void saveSuccess() {

        Booking resource = repository.save(dto);

        assertThat(resource).isNotNull();
        assertThat(resource.getDate()).isEqualTo(dto.getDate());
    }

    @Test
    @DisplayName("Тест на успешное изменение бронирования")
    void update() {
        Booking booking = repository.findById(3L).get();
        LocalDate now = LocalDate.now();
        booking.setDate(now);

        repository.update(booking);

        assertThat(booking.getDate()).isEqualTo(now);
    }


    @Test
    @DisplayName("Тест на успешное удаление бронирования")
    void deleteByIdSuccess() {

        Optional<Booking> result = repository.deleteById(2L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Тест на попытку удаления бронирования, если его не существует")
    void deleteByIdReturnsEmpty() {

        Optional<Booking> result = repository.deleteById(-1L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Тест на успешное возвращение всех бронирований")
    void findAll() {
        List<Booking> resultBefore = repository.findAll();
        repository.save(dto);

        List<Booking> resultAfter = repository.findAll();

        assertThat(resultAfter.size()).isEqualTo(resultBefore.size() + 1);
    }

    @Test
    @DisplayName("Тест на успешное возвращение всех бронирований пользователя по его id")
    void findAllByPersonId() {
        List<Booking> resultBefore = repository.findAllByPersonId(2L);
        repository.save(dto);

        List<Booking> resultAfter = repository.findAllByPersonId(2L);

        assertThat(resultAfter.size()).isEqualTo(resultBefore.size() + 1);
    }
}