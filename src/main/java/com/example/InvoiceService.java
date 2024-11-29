package com.example;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    private static final String UPLOAD_DIRECTORY = "uploads";

    public void updateInvoice(Long id, MultipartFile file) throws IOException {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (invoice.getPhotoFilename() != null) {
            File oldFile = new File(UPLOAD_DIRECTORY + File.separator + invoice.getPhotoFilename());
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        String newFilename = file.getOriginalFilename();
        File newFile = new File(UPLOAD_DIRECTORY + File.separator + newFilename);
        file.transferTo(newFile);

        invoice.setPhotoFilename(newFilename);
        invoiceRepository.save(invoice);
    }
}
