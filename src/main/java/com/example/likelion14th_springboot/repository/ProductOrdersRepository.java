package com.example.likelion14th_springboot.repository;

import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrdersRepository extends JpaRepository<ProductOrders, Long> {
}