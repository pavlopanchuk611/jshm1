package com.example.invoicemanagement;

@WebServlet("/deleteInvoice")
public class InvoiceDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));

        String photoFilename = getPhotoFilenameFromDatabase(id);

        deleteInvoiceFromDatabase(id);

        File photoFile = new File(getServletContext().getRealPath("") + File.separator + "uploads" + File.separator + photoFilename);
        if (photoFile.exists()) {
            photoFile.delete();
        }

        response.getWriter().println("Рахунок успішно видалено!");
    }

    private String getPhotoFilenameFromDatabase(Long id) {
        String photoFilename = null;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT photoFilename FROM invoices WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                photoFilename = rs.getString("photoFilename");
            }
            rs.close();
            pstmt.close ();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photoFilename;
    }

    private void deleteInvoiceFromDatabase(Long id) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "DELETE FROM invoices WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
}
