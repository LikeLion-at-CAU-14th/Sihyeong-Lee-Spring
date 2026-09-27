package com.example.likelion14th_springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShippingAddress {

    @Column(nullable = false)
    private String recipient;     // 수령인
    @Column(nullable = false)
    private String phoneNumber;   // 전화번호
    @Column(nullable = false)
    private String roadAddress;   // 도로명주소
    private String detailAddress; // 상세주소
    @Column(nullable = false, length = 5)
    private String zipCode;       // 우편번호
}