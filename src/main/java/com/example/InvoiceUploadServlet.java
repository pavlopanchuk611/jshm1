package com.example;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/uploadInvoice")
@MultipartConfig
public class InvoiceUploadServlet extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "uploads"; // Директорія для збереження фото

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> formItems = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                String invoiceName = null;
                String photoFilename = null;

                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        // Обробка файлу
                        photoFilename = item.getName();
                        File uploadFile = new File(getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY + File.separator + photoFilename);
                        item.write(uploadFile);
                    } else {
                        // Обробка поля з назвою рахунка
                        if (item.getFieldName().equals("invoiceName")) {
                            invoiceName = item.getString();
                        }
                    }
                }

                // Збереження інформації про рахунок у базі даних
                saveInvoiceToDatabase(invoiceName, photoFilename);

                response.getWriter().println("Рахунок успішно створено з фото: " + photoFilename);
            } catch (Exception ex) {
                ex.printStackTrace();
                response.getWriter().println("Помилка при завантаженні рахунка: " + ex.getMessage());
            }
        }
    }

    private void saveInvoiceToDatabase(String invoiceName, String photoFilename) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO invoices (invoiceName, photoFilename) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, invoiceName);
            pstmt.setString(2, photoFilename);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
