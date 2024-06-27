package ru.ylab_learning.coworking.repository.impl;

import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
import ru.ylab_learning.coworking.domain.exception.ResourceNotFoundException;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.repository.ResourceRepository;
import ru.ylab_learning.coworking.util.SQLQueries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория ресурсов
 */
public record ResourceRepositoryImpl(String dbUrl, String dbUser, String dbPassword) implements ResourceRepository {

    @Override
    public Optional<Resource> findById(long id) {
        Resource resource = null;
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_RESOURCE_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            // TODO maybe while (resultSet.next()) {
            if (resultSet.next()) {
                resource = buildResourceFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        return Optional.ofNullable(resource);
    }

    @Override
    public Optional<Resource> deleteById(Long id) {
        Optional<Resource> response = findById(id);
        if (response.isPresent()) {
            try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(SQLQueries.DELETE_RESOURCE_BY_ID)) {
                {
                    statement.setLong(1, id);
                    statement.executeUpdate();
                }
            } catch  (SQLException e)  {
                ConsoleOutput.print(e.getMessage());
            }
        }
        return response;
    }

    @Override
    public Resource save(ResourceDTO dto) throws ResourceNotFoundException {
        long generatedID = -1;
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(SQLQueries.INSERT_RESOURCE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dto.getType().toString());
            statement.setString(2, dto.getDescription());
            statement.setInt(3, dto.getRentPrice());
            statement.setBoolean(4, dto.isActive());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedID = generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        return findById(generatedID).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void update(Resource original) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(SQLQueries.UPDATE_RESOURCE)) {
            statement.setString(1, original.getType().toString());
            statement.setString(2, original.getDescription());
            statement.setInt(3, original.getRentPrice());
            statement.setBoolean(4, original.isActive());
            statement.setLong(5, original.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
    }

    @Override
    public List<Resource> findAll() {
        List<Resource> resources = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQLQueries.FIND_ALL_RESOURCES);
            while (resultSet.next()) {
                Resource resource = buildResourceFromResultSet(resultSet);
                resources.add(resource);
            }
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        return resources;
    }

    private Resource buildResourceFromResultSet(ResultSet resultSet) throws SQLException {
        return new Resource(
                resultSet.getLong("id"),
                ResourceType.valueOf(resultSet.getString("type")),
                resultSet.getInt("rent_price"),
                resultSet.getString("description"),
                resultSet.getBoolean("is_active"));
    }
}
