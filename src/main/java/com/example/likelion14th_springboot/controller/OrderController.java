package com.example.likelion14th_springboot.controller;

import com.example.likelion14th_springboot.dto.request.OrderCreateRequestDto;
import com.example.likelion14th_springboot.dto.request.OrderDeleteRequestDto;
import com.example.likelion14th_springboot.dto.request.OrderUpdateRequestDto;
import com.example.likelion14th_springboot.dto.response.OrderResponseDto;
import com.example.likelion14th_springboot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders") // URL 공통 경로 매핑
public class OrderController {

    private final OrderService orderService;

    // [POST] http://localhost:8080/orders -> 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderCreateRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    // 구매자별 주문 목록 조회: GET http://localhost:8080/orders?buyerId=1
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrdersByBuyer(@RequestParam Long buyerId) {
        return ResponseEntity.ok(orderService.getOrdersByBuyer(buyerId));
    }

    // 단건 주문 조회: GET http://localhost:8080/orders/{orderId}
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // 주문 배송정보 수정: PUT http://localhost:8080/orders/{orderId}
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long orderId,
                                                        @RequestBody OrderUpdateRequestDto dto) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, dto));
    }

    // 주문 삭제: DELETE http://localhost:8080/orders/{orderId}
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId,
                                              @RequestBody OrderDeleteRequestDto dto) {
        orderService.deleteOrder(orderId, dto);
        return ResponseEntity.ok("주문이 성공적으로 삭제되었습니다.");
    }
}