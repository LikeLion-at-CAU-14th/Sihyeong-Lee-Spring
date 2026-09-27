package com.example.likelion14th_springboot.repository;

import com.example.likelion14th_springboot.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    // 삭제되지 않은 주문만 구매자별 조회 (최신순)
    List<Orders> findAllByBuyer_IdAndDeletedFalseOrderByCreatedAtDesc(Long buyerId);

    // 삭제되지 않은 주문 단건 조회
    Optional<Orders> findByIdAndDeletedFalse(Long id);
}