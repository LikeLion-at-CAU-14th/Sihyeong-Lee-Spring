package com.example.likelion14th_springboot.dto.request;

import com.example.likelion14th_springboot.domain.ShippingAddress;
import lombok.Getter;

@Getter
public class OrderUpdateRequestDto {
    private Long buyerId; // 본인 확인
    private String recipient;
    private String phoneNumber;
    private String roadAddress;
    private String detailAddress;
    private String zipCode;

    // DTO → 새 배송지 값 타입으로 변환
    public ShippingAddress toShippingAddress() {
        return new ShippingAddress(recipient, phoneNumber, roadAddress, detailAddress, zipCode);
    }
}