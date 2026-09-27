package com.example.likelion14th_springboot.dto.request;

import lombok.Getter;

@Getter
public class OrderDeleteRequestDto {
    private Long buyerId; // 본인 주문인지 검증하기 위한 회원 ID
}