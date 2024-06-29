package ru.ylab_learning.coworking.util;

public class SQLQueries {
    public static final String CREATE_LIQUIBASE_SCHEMA_QUERY = "CREATE SCHEMA IF NOT EXISTS lb_special";
    public static final String CREATE_APP_SCHEMA_QUERY = "CREATE SCHEMA IF NOT EXISTS entities";
    public static final String FIND_ALL_PERSONS = "SELECT * FROM entities.persons";
    public static final String FIND_PERSON_BY_LOGIN = "SELECT * FROM entities.persons WHERE login =?";
    public static final String FIND_PERSON_BY_ID = "SELECT * FROM entities.persons WHERE id =?";
    public static final String INSERT_PERSON =
            "INSERT INTO entities.persons (login, password, name, role, email) VALUES (?,?,?,?,?)";
    public static final String FIND_RESOURCE_BY_ID = "SELECT * FROM entities.resources WHERE id =?";
    public static final String DELETE_RESOURCE_BY_ID = "DELETE FROM entities.resources WHERE id =?";
    public static final String INSERT_RESOURCE =
            "INSERT INTO entities.resources (type, description, rent_price, is_active) VALUES (?,?,?,?)";
    public static final String UPDATE_RESOURCE =
            "UPDATE entities.resources SET type=?, description=?, rent_price=?, is_active=? WHERE id=?";
    public static final String FIND_ALL_RESOURCES = "SELECT * FROM entities.resources";
    public static final String FIND_BOOKING_BY_ID = "SELECT * FROM entities.bookings WHERE id =?";
    public static final String UPDATE_BOOKING =
            "UPDATE entities.bookings SET date=?, start_time=?, end_time=?, resource_id=?, person_id=? WHERE id=?";
    public static final String FIND_ALL_BOOKINGS = "SELECT * FROM entities.bookings";
    public static final String FIND_ALL_BOOKINGS_BY_PERSON_ID =
            "SELECT * FROM entities.bookings WHERE person_id =?";
    public static final String INSERT_BOOKING = "INSERT INTO entities.bookings (date, start_time, end_time, " +
            "person_id, resource_id) VALUES (?,?,?,?,?)";
    public static final String DELETE_BOOKING_BY_ID = "DELETE FROM entities.bookings WHERE id =?";
}
