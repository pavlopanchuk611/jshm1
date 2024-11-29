package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/listInvoices")
public class InvoiceListServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database"; // Змініть на ваш URL бази даних
    private static final String USER = "your_username"; // Змініть на ваше ім'я користувача
    private static final String PASS = "your_password"; // Змініть на ваш пароль

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "SELECT invoiceName, photoFilename FROM invoices"; // Змініть на вашу таблицю
            ResultSet rs = stmt.executeQuery(sql);

            response.getWriter().println("<h1>Список рахунків</h1>");
            response.getWriter().println("<table border='1'><tr><th>Назва рахунка</th><th>Фото</th></tr>");

            while (rs.next()) {
                String invoiceName = rs.getString("invoiceName");
                String photoFilename = rs.getString("photoFilename");
                response.getWriter().println("<tr><td>" + invoiceName + "</td><td><img src='uploads/" + photoFilename + "' width='100' height='100'></td></tr>");
            }
            response.getWriter().println("</table>");

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Помилка при отриманні рахунків: " + e.getMessage());
        }
    }
}
