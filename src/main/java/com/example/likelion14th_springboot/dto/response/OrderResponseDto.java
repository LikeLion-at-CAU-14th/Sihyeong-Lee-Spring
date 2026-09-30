package com.example.likelion14th_springboot.dto.response;

import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.domain.ShippingAddress;
import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private Long buyerId;
    private DeliverStatus deliverStatus;

    // 주문 상품
    private Long productId;
    private String productName;
    private Integer quantity;

    // 배송지
    private String recipient;
    private String phoneNumber;
    private String roadAddress;
    private String detailAddress;
    private String zipCode;

    public static OrderResponseDto fromEntity(Orders order, ProductOrders productOrders) {
        ShippingAddress address = order.getShippingAddress();
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .buyerId(order.getBuyer().getId())
                .deliverStatus(order.getDeliverStatus())
                .productId(productOrders.getProduct().getId())
                .productName(productOrders.getProduct().getName())
                .quantity(productOrders.getQuantity())
                .recipient(address.getRecipient())
                .phoneNumber(address.getPhoneNumber())
                .roadAddress(address.getRoadAddress())
                .detailAddress(address.getDetailAddress())
                .zipCode(address.getZipCode())
                .build();
    }
}