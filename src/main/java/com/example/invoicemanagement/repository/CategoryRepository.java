package com.example.invoicemanagement.repository;

import com.example.invoicemanagement.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}