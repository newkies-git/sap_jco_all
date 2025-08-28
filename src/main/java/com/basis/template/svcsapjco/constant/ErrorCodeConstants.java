package com.basis.template.svcsapjco.constant;

/**
 * 에러 코드 상수 클래스
 */
public final class ErrorCodeConstants {

    private ErrorCodeConstants() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }

    // SAP 관련 에러 코드
    public static final String SAP_CONNECTION_ERROR = "SAP_CONNECTION_ERROR";
    public static final String SAP_FUNCTION_EXECUTION_ERROR = "SAP_FUNCTION_EXECUTION_ERROR";
    public static final String SAP_FUNCTION_DISCOVERY_ERROR = "SAP_FUNCTION_DISCOVERY_ERROR";

    // 검증 관련 에러 코드
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String CONSTRAINT_VIOLATION_ERROR = "CONSTRAINT_VIOLATION_ERROR";

    // 인자 관련 에러 코드
    public static final String INVALID_ARGUMENT_TYPE_ERROR = "INVALID_ARGUMENT_TYPE_ERROR";
    public static final String INVALID_ARGUMENT_ERROR = "INVALID_ARGUMENT_ERROR";

    // HTTP 관련 에러 코드
    public static final String ENDPOINT_NOT_FOUND_ERROR = "ENDPOINT_NOT_FOUND_ERROR";
    public static final String METHOD_NOT_ALLOWED_ERROR = "METHOD_NOT_ALLOWED_ERROR";

    // 기타 에러 코드
    public static final String UNEXPECTED_ERROR = "UNEXPECTED_ERROR";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
}
