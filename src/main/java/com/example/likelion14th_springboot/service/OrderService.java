package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.domain.Product;
import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.dto.request.OrderCreateRequestDto;
import com.example.likelion14th_springboot.dto.response.OrderResponseDto;
import com.example.likelion14th_springboot.repository.MemberRepository;
import com.example.likelion14th_springboot.repository.OrdersRepository;
import com.example.likelion14th_springboot.repository.ProductOrdersRepository;
import com.example.likelion14th_springboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrdersRepository ordersRepository;
    private final ProductOrdersRepository productOrdersRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto dto) {
        // 1. 구매자 조회
        Member buyer = memberRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 2. 상품 조회
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        // 3. 수량 및 재고 검증
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        }
        if (product.getStock() < dto.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        // 4. 재고 차감 -> 변경 감지(Dirty Checking)로 자동 UPDATE
        product.reduceStock(dto.getQuantity());

        // 5. 주문 저장 (배송상태, 배송지 포함)
        Orders order = ordersRepository.save(dto.toEntity(buyer));

        // 6. 주문-상품 연결(ProductOrders) 저장
        ProductOrders productOrders = productOrdersRepository.save(
                ProductOrders.builder()
                        .orders(order)
                        .product(product)
                        .quantity(dto.getQuantity())
                        .build());

        // 7. ResponseDto로 변환하여 반환
        return OrderResponseDto.fromEntity(order, productOrders);
    }
}