package ru.ylab_learning.coworking.repository.impl;

import ru.ylab_learning.coworking.domain.dto.PersonDTO;
import ru.ylab_learning.coworking.domain.enums.PersonRole;
import ru.ylab_learning.coworking.domain.exception.PersonNotFoundException;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.repository.PersonRepository;
import ru.ylab_learning.coworking.util.SQLQueries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория пользователей
 */

public record PersonRepositoryImpl(String dbUrl, String dbUser, String dbPassword) implements PersonRepository {
    @Override
    public Optional<Person> findByLogin(String login) {
        Person person = null;
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_PERSON_BY_LOGIN)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            // TODO maybe while (resultSet.next()) {
            if (resultSet.next()) {
                person = buildPersonFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        return Optional.ofNullable(person);
    }

    @Override
    public Person save(PersonDTO dto) throws PersonNotFoundException{
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(SQLQueries.INSERT_PERSON)) {
            statement.setString(1, dto.getLogin());
            statement.setString(2, dto.getPassword());
            statement.setString(3, dto.getName());
            statement.setString(4, "user");
            statement.setString(5, dto.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        // TODO переделать как в resrepo.save() ??
        return findByLogin(dto.getLogin()).orElseThrow(PersonNotFoundException::new);
    }

    @Override
    public List<Person> findAll() {
        List<Person> persons = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQLQueries.FIND_ALL_PERSONS);
            while (resultSet.next()) {
                Person person = buildPersonFromResultSet(resultSet);
                persons.add(person);
            }
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        return persons;
    }

    @Override
    public Optional<Person> findById(Long personId) {
        Person person = null;
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_PERSON_BY_ID)) {
            statement.setLong(1, personId);
            ResultSet resultSet = statement.executeQuery();
            // TODO maybe while (resultSet.next()) {
            if (resultSet.next()) {
                person = buildPersonFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        return Optional.ofNullable(person);
    }

    private Person buildPersonFromResultSet(ResultSet resultSet) throws SQLException {
        return new Person(
                resultSet.getLong("id"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                PersonRole.valueOf(resultSet.getString("role")),
                resultSet.getString("name"),
                resultSet.getString("email"));
    }

}
