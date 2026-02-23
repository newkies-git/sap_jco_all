package com.basis.template.svcsapjco.exception;

import com.basis.template.svcsapjco.constant.ErrorCodeConstants;
import com.basis.template.svcsapjco.constant.MessageConstants;
import com.basis.template.svcsapjco.dto.ApiResponse;
import com.basis.template.svcsapjco.util.StructuredLogger;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 전역 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * SAP JCo 관련 예외 처리
     */
    /**
     * SAP 결과 추출 예외 처리
     */
    @ExceptionHandler(SapResultExtractionException.class)
    public ResponseEntity<ApiResponse<Void>> handleSapResultExtractionException(SapResultExtractionException e) {
        StructuredLogger.logException(e.getErrorCode(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("SAP 결과 추출 실패: " + e.getMessage()));
    }

    /**
     * SAP 파라미터 설정 예외 처리
     */
    @ExceptionHandler(SapParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleSapParameterException(SapParameterException e) {
        StructuredLogger.logException(e.getErrorCode(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("SAP 파라미터 설정 실패: " + e.getMessage()));
    }

    @ExceptionHandler(SapJcoException.class)
    public ResponseEntity<ApiResponse<Void>> handleSapJcoException(SapJcoException e) {
        StructuredLogger.logException(e.getErrorCode(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("SAP 함수 호출 실패: " + e.getMessage()));
    }

    /**
     * SAP 연결 예외 처리
     */
    @ExceptionHandler(SapConnectionException.class)
    public ResponseEntity<ApiResponse<Void>> handleSapConnectionException(SapConnectionException e) {
        StructuredLogger.logException(ErrorCodeConstants.SAP_CONNECTION_ERROR, e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error("SAP 연결 실패: " + e.getMessage()));
    }

    /**
     * SAP 함수 실행 예외 처리
     * (실제 오류 로그는 SapJcoFunctionExecutor.logSapFunctionError에서 이미 출력하므로 여기서는 중복 로그 생략)
     */
    @ExceptionHandler(SapFunctionExecutionException.class)
    public ResponseEntity<ApiResponse<Void>> handleSapFunctionExecutionException(SapFunctionExecutionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("SAP 함수 실행 실패: " + e.getMessage()));
    }

    /**
     * SAP 함수 검색 예외 처리
     */
    @ExceptionHandler(SapFunctionDiscoveryException.class)
    public ResponseEntity<ApiResponse<Void>> handleSapFunctionDiscoveryException(SapFunctionDiscoveryException e) {
        StructuredLogger.logException(ErrorCodeConstants.SAP_FUNCTION_DISCOVERY_ERROR, e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("SAP 함수 검색/인터페이스 조회 실패: " + e.getMessage()));
    }

    /**
     * 검증 예외 처리
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(ValidationException e) {
        StructuredLogger.logValidationError("VALIDATION_ERROR", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("입력값 검증 실패: " + e.getMessage()));
    }

    /**
     * 제약 조건 위반 예외 처리
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        StructuredLogger.logValidationError("CONSTRAINT_VIOLATION", "제약 조건 위반: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("제약 조건 위반: " + e.getMessage()));
    }

    /**
     * 잘못된 인자 타입 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        StructuredLogger.logException(ErrorCodeConstants.INVALID_ARGUMENT_TYPE_ERROR, "잘못된 인자 타입: " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("잘못된 인자 타입: " + e.getName() + " - " + e.getValue()));
    }

    /**
     * 404 에러 처리 (잘못된 엔드포인트)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        StructuredLogger.logException(ErrorCodeConstants.ENDPOINT_NOT_FOUND_ERROR, 
                "잘못된 엔드포인트: " + e.getHttpMethod() + " " + e.getRequestURL(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("요청한 엔드포인트를 찾을 수 없습니다: " + e.getHttpMethod() + " " + e.getRequestURL()));
    }

    /**
     * 기타 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        StructuredLogger.logException(ErrorCodeConstants.UNEXPECTED_ERROR, e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(MessageConstants.ERROR_INTERNAL_SERVER_ERROR));
    }
}
