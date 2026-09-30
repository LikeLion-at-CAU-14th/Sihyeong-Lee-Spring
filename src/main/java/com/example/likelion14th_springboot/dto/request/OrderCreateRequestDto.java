package com.example.likelion14th_springboot.dto.request;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.domain.ShippingAddress;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import lombok.Getter;

@Getter
public class OrderCreateRequestDto {
    private Long buyerId;
    private Long productId;
    private Integer quantity;
    private String recipient;
    private String phoneNumber;
    private String roadAddress;
    private String detailAddress;
    private String zipCode;


    // DTO를 DB에 저장할 실제 Entity 객체로 변환하는 메서드
    public Orders toEntity(Member buyer) {
        return Orders.builder()
                .buyer(buyer)
                .deliverStatus(DeliverStatus.PREPARATION) // 주문 생성 시 배송준비
                .shippingAddress(new ShippingAddress(
                        recipient,
                        phoneNumber,
                        roadAddress,
                        detailAddress,
                        zipCode))
                .build();
    }
}
