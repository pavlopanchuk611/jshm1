package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderListViewer {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_database";
    private static final String USER = "pavlo";
    private static final String PASSWORD = "pavlopavlo12!";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            String sql = "SELECT o.id AS order_id, c.name AS client_name, s.service_name, o.order_date, os.status_name " +
                    "FROM orders o " +
                    "JOIN clients c ON o.client_id = c.id " +
                    "JOIN services s ON o.service_id = s.id " +
                    "JOIN order_statuses os ON o.status_id = os.id";

            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Список замовлень:");
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                String clientName = resultSet.getString("client_name");
                String serviceName = resultSet.getString("service_name");
                String orderDate = resultSet.getString("order_date");
                String statusName = resultSet.getString("status_name");

                System.out.printf("Замовлення ID: %d, Клієнт: %s, Послуга: %s, Дата замовлення: %s, Статус: %s%n",
                        orderId, clientName, serviceName, orderDate, statusName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
}
