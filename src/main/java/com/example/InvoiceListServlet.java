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

@WebServlet("/updateInvoice")
@MultipartConfig
public class InvoiceUpdateServlet extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String invoiceName = request.getParameter("invoiceName");
        Long id = Long.parseLong(request.getParameter("id"));
        String photoFilename = null;

        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> formItems = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        photoFilename = item.getName();
                        File uploadFile = new File(getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY + File.separator + photoFilename);
                        item.write(uploadFile);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        updateInvoiceInDatabase(id, invoiceName, photoFilename);

        response.getWriter().println("Рахунок успішно оновлено!");
    }

    private void updateInvoiceInDatabase(Long id, String invoiceName, String photoFilename) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "UPDATE invoices SET invoiceName = ?, photoFilename = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, invoiceName);
            pstmt.setString(2, photoFilename);
            pstmt.setLong(3, id);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @WebServlet("/listInvoices")
    public class InvoiceListServlet extends HttpServlet {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/my_database";
        private static final String USER = "pavlo";
        private static final String PASSWORD = "pavlopavlo12!";

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            try {
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                String sql = "SELECT id, invoiceName, photoFilename FROM invoices";
                ResultSet rs = stmt.executeQuery(sql);

                response.getWriter().println("<h1>Список рахунків</h1>");
                response.getWriter().println("<table border='1'><tr><th>Назва рахунка</th><th>Фото</th><th>Дія</th></tr>");

                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String invoiceName = rs.getString("invoiceName");
                    String photoFilename = rs.getString("photoFilename");
                    response.getWriter().println("<tr><td>" + invoiceName + "</td><td><img src='uploads/" + photoFilename + "' width='100' height='100'></td>" +
                            "<td><a href='downloadFile?id=" + id + "'>Завантажити файл</a></td></tr>");
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
}