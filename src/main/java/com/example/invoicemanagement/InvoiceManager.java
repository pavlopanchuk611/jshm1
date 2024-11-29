package com.example.invoicemanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvoiceManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_database";
    private static final String USER = "pavlo";
    private static final String PASSWORD = "pavlopavlo12!";

    public static void main(String[] args) {
        saveInvoice("Invoice_001.pdf", "invoice_001_photo.jpg");
    }

    public static void saveInvoice(String invoiceName, String photoFilename) {
        String sql = "INSERT INTO invoices (invoice_name, photo_filename) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, invoiceName);
            preparedStatement.setString(2, photoFilename);
            preparedStatement.executeUpdate();

            System.out.println("Рахунок успішно збережено з назвою фото: " + photoFilename);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
