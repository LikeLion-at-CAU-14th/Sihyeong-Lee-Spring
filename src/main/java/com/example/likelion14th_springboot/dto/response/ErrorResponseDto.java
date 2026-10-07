package com.example.likelion14th_springboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private int status;     // HTTP 상태 코드
    private String message; // 에러 메시지
}
