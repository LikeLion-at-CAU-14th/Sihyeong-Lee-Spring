package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.domain.Product;
import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.dto.request.OrderCreateRequestDto;
import com.example.likelion14th_springboot.dto.request.OrderDeleteRequestDto;
import com.example.likelion14th_springboot.dto.request.OrderUpdateRequestDto;
import com.example.likelion14th_springboot.dto.response.OrderResponseDto;
import com.example.likelion14th_springboot.repository.MemberRepository;
import com.example.likelion14th_springboot.repository.OrdersRepository;
import com.example.likelion14th_springboot.repository.ProductOrdersRepository;
import com.example.likelion14th_springboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    //구매자별 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByBuyer(Long buyerId) {
        // 1. 구매자 존재 확인
        if (!memberRepository.existsById(buyerId)) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        // 2. 주문 목록 조회 후 DTO로 변환
        return ordersRepository.findAllByBuyer_IdAndDeletedFalseOrderByCreatedAtDesc(buyerId).stream()
                .map(order -> OrderResponseDto.fromEntity(order, order.getProductOrders().get(0)))
                .toList();
    }

    // 단건 주문 조회
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long orderId) {
        Orders order = ordersRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        return OrderResponseDto.fromEntity(order, order.getProductOrders().get(0));
    }

    // 주문 배송정보 수정
    @Transactional
    public OrderResponseDto updateOrder(Long orderId, OrderUpdateRequestDto dto) {
        // 1. 주문 조회
        Orders order = ordersRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        // 2. 본인 주문인지 권한 검증
        if (!order.getBuyer().getId().equals(dto.getBuyerId())) {
            throw new IllegalArgumentException("본인의 주문만 수정할 수 있습니다.");
        }

        // 3. 배송정보 수정 (PREPARATION이 아니면 엔티티에서 예외 발생)
        //    -> @Transactional 내부이므로 변경 감지(Dirty Checking)로 자동 UPDATE
        order.updateShippingAddress(dto.toShippingAddress());

        return OrderResponseDto.fromEntity(order, order.getProductOrders().get(0));
    }

    // 주문 삭제 (Soft Delete)
    @Transactional
    public void deleteOrder(Long orderId, OrderDeleteRequestDto dto) {
        // 1. 삭제되지 않은 주문 조회
        Orders order = ordersRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        // 2. 본인 주문인지 권한 검증
        if (!order.getBuyer().getId().equals(dto.getBuyerId())) {
            throw new IllegalArgumentException("본인의 주문만 삭제할 수 있습니다.");
        }

        // 3. Soft Delete (COMPLETED가 아니면 엔티티에서 예외 발생)
        //    -> delete() 쿼리가 아니라 변경 감지로 deleted = true UPDATE
        order.delete();
    }
}