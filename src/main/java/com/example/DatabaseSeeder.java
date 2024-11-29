package com.example;
import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSeeder {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username"; // Змініть на ваше ім'я користувача
    private static final String PASSWORD = "your_password"; // Змініть на ваш пароль

    public static void main(String[] args) {
        Faker faker = new Faker();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Додати статуси замовлень
            addOrderStatuses(connection, faker);

            // Додати клієнтів
            addClients(connection, faker);

            // Додати послуги
            addServices(connection, faker);

            // Додати замовлення
            addOrders(connection, faker);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addOrderStatuses(Connection connection, Faker faker) throws SQLException {
        String sql = "INSERT INTO order_statuses (status_name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            String[] statuses = {"Pending", "Completed", "Cancelled"};
            for (String status : statuses) {
                preparedStatement.setString(1, status);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private static void addClients(Connection connection, Faker faker) throws SQLException {
        String sql = "INSERT INTO clients (name, email) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < 100; i++) {
                preparedStatement.setString(1, faker.name().fullName());
                preparedStatement.setString(2, faker.internet().emailAddress());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private static void addServices(Connection connection, Faker faker) throws SQLException {
        String sql = "INSERT INTO services (service_name, price) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < 50; i++) {
                preparedStatement.setString(1, faker.commerce().productName());
                preparedStatement.setDouble(2, Double.parseDouble(faker.commerce().price()));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private static void addOrders(Connection connection, Faker faker) throws SQLException {
        String sql = "INSERT INTO orders (client_id, service_id, order_date, status_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < 200; i++) {
                preparedStatement.setInt(1, faker.number().numberBetween(1, 100)); // ID клієнта
                preparedStatement.setInt(2, faker.number().numberBetween(1, 50)); // ID послуги
                preparedStatement.setTimestamp(3, new java.sql.Timestamp(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).get Time()));
                preparedStatement.setInt(4, faker.number().numberBetween(1, 3)); // ID статусу
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}