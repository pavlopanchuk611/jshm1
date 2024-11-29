package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/your_database"; // Змініть на ваш URL
    private String jdbcUsername = "your_username"; // Ваше ім'я користувача
    private String jdbcPassword = "your_password"; // Ваш пароль

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public void addAnimal(String name, String species) {
        String sql = "INSERT INTO animals (name, species) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, species);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllAnimals() {
        List<String> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                animals.add(resultSet.getInt("id") + ": " + resultSet.getString("name") + " (" + resultSet.getString("species") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animals;
    }

    public void deleteAnimal(int id) {
        String sql = "DELETE FROM animals WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAnimal(int id, String name, String species) {
        String sql = "UPDATE animals SET name = ?, species = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, species);
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}