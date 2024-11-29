package com.example;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnimalDataGenerator {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/animal_db";
    private static final String USER = "your_username"; // Змініть на ваше ім'я користувача
    private static final String PASSWORD = "your_password"; // Змініть на ваш пароль

    public static void main(String[] args) {
        Faker faker = new Faker();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO animals (name, species) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int i = 0; i < 100000; i++) {
                    String name = faker.animal().name();
                    String species = faker.animal().type();

                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, species);
                    preparedStatement.addBatch();

                    // Виконання батчу кожні 1000 записів для підвищення продуктивності
                    if (i % 1000 == 0) {
                        preparedStatement.executeBatch();
                    }
                }
                // Виконати залишок записів
                preparedStatement.executeBatch();
            }
            System.out.println("Дані успішно згенеровані та збережені в базі даних.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}