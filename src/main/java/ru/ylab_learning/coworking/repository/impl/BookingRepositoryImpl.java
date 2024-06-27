package ru.ylab_learning.coworking.repository.impl;

import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.exception.BookingNotFoundException;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.repository.BookingRepository;
import ru.ylab_learning.coworking.util.SQLQueries;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория бронирований
 */
public record BookingRepositoryImpl(String dbUrl, String dbUser, String dbPassword) implements BookingRepository {

    @Override
    public Optional<Booking> findById(Long bookingId) {
        Booking booking = null;
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_BOOKING_BY_ID)) {
            statement.setLong(1, bookingId);
            ResultSet resultSet = statement.executeQuery();
            // TODO maybe while (resultSet.next()) {
            if (resultSet.next()) {
                booking = buildBookingFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        return Optional.ofNullable(booking);
    }

    @Override
    public void update(Booking b) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(SQLQueries.UPDATE_BOOKING)) {
            setValuesOfStatement(statement, b.getDate(), b.getStartTime(),
                    b.getEndTime(), b.getPersonId(), b.getResourceId());
            statement.setLong(6, b.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
    }

    @Override
    public List<Booking> findAllByPersonId(Long id) {
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_ALL_BOOKINGS_BY_PERSON_ID)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Booking booking = buildBookingFromResultSet(resultSet);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        return bookings;
    }

    @Override
    public Booking save(BookingDTO dto) throws BookingNotFoundException {
        long generatedID = -1;
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(SQLQueries.INSERT_BOOKING, Statement.RETURN_GENERATED_KEYS)) {
            setValuesOfStatement(statement, dto.getDate(), dto.getStartTime(),
                    dto.getEndTime(), dto.getPersonId(), dto.getResourceId());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedID = generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        return findById(generatedID).orElseThrow(BookingNotFoundException::new);
    }

    @Override
    public Optional<Booking> deleteById(Long id) {
        Optional<Booking> booking = findById(id);
        if (booking.isPresent()) {
            try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(SQLQueries.DELETE_BOOKING_BY_ID)) {
                {
                    statement.setLong(1, id);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                ConsoleOutput.print(e.getMessage());
            }
        }
        return booking;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQLQueries.FIND_ALL_BOOKINGS);
            while (resultSet.next()) {
                Booking booking = buildBookingFromResultSet(resultSet);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            ConsoleOutput.print(e.getMessage());
        }
        return bookings;
    }

    private void setValuesOfStatement(PreparedStatement statement, LocalDate date,
            LocalTime startTime, LocalTime endTime, Long personId, Long resourceId) throws SQLException {
        statement.setDate(1, Date.valueOf(date));
        statement.setTime(2, Time.valueOf(startTime));
        statement.setTime(3, Time.valueOf(endTime));
        statement.setLong(4, personId);
        statement.setLong(5, resourceId);
    }

    private Booking buildBookingFromResultSet(ResultSet resultSet) throws SQLException {
        return new Booking(
                resultSet.getLong("id"),
                resultSet.getLong("resource_id"),
                resultSet.getLong("person_id"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getTime("start_time").toLocalTime(),
                resultSet.getTime("end_time").toLocalTime()
        );
    }
}
