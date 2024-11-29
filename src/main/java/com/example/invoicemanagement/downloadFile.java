package com.example.invoicemanagement;

@WebServlet("/downloadFile")
public class FileDownloadServlet extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String photoFilename = getPhotoFilenameFromDatabase(id);

        if (photoFilename != null) {
            File file = new File(getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY + File.separator + photoFilename);
            if (file.exists()) {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
                response.setContentLength((int) file.length());

                try (FileInputStream in = new FileInputStream(file);
                     OutputStream out = response.getOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                response.getWriter().println("Файл не знайдено.");
            }
        } else {
            response.getWriter().println("Рахунок не знайдено.");
        }
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
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStack e.printStackTrace();
        }
        return photoFilename;
    }
}