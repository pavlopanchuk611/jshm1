package com.example.invoicemanagement.model;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invoiceName;
    private String photoFilename;
}