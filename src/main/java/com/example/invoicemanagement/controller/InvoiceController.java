package com.example.invoicemanagement.controller;

import com.example.invoicemanagement.service.InvoiceService;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/{id}/update")
    public ResponseEntity<String> updateInvoice(@PathVariable Long id,
                                                @RequestParam("file") MultipartFile file) {
        try {
            invoiceService.updateInvoice(id, file);
            return ResponseEntity.ok("Invoice updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating invoice: " + e.getMessage());
        }
    }
}
