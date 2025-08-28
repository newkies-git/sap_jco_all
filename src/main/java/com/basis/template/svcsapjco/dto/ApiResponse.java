package com.basis.template.svcsapjco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 일관된 API 응답을 위한 공통 응답 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * 응답 성공 여부
     */
    private boolean success;

    /**
     * 응답 데이터
     */
    private T data;

    /**
     * 오류 메시지 (실패 시에만 사용)
     */
    private String error;

    /**
     * 응답 타임스탬프
     */
    private String timestamp;

    /**
     * 성공 응답을 생성하는 정적 팩토리 메서드
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.error = null;
        response.timestamp = getCurrentTimestamp();
        return response;
    }

    /**
     * 성공 응답을 생성하는 정적 팩토리 메서드 (데이터 없음)
     */
    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = null;
        response.error = null;
        response.timestamp = getCurrentTimestamp();
        return response;
    }

    /**
     * 실패 응답을 생성하는 정적 팩토리 메서드
     */
    public static <T> ApiResponse<T> error(String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.data = null;
        response.error = error;
        response.timestamp = getCurrentTimestamp();
        return response;
    }

    /**
     * 현재 타임스탬프를 ISO 형식으로 반환
     */
    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
