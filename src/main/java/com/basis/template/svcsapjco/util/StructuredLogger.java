package com.basis.template.svcsapjco.util;

import com.basis.template.svcsapjco.constant.LogConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 구조화된 로깅을 위한 유틸리티 클래스
 */
@Slf4j
@Component
public class StructuredLogger {

    private static final ThreadLocal<String> requestIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<Long> startTimeHolder = new ThreadLocal<>();

    /**
     * 요청 ID 생성 및 설정
     */
    public static String generateRequestId() {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        requestIdHolder.set(requestId);
        startTimeHolder.set(System.currentTimeMillis());
        return requestId;
    }

    /**
     * 현재 요청 ID 반환
     */
    public static String getCurrentRequestId() {
        return requestIdHolder.get();
    }

    /**
     * 요청 시작 시간 반환
     */
    public static Long getStartTime() {
        return startTimeHolder.get();
    }

    /**
     * 실행 시간 계산 (밀리초)
     */
    public static long calculateExecutionTime() {
        Long startTime = startTimeHolder.get();
        return startTime != null ? System.currentTimeMillis() - startTime : 0;
    }

    /**
     * ThreadLocal 정리
     */
    public static void clear() {
        requestIdHolder.remove();
        startTimeHolder.remove();
    }

    /**
     * API 요청 로그
     */
    public static void logApiRequest(String httpMethod, String requestPath, Map<String, Object> params) {
        String requestId = getCurrentRequestId();
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, requestId);
        logData.put(LogConstants.LOG_KEY_HTTP_METHOD, httpMethod);
        logData.put(LogConstants.LOG_KEY_REQUEST_PATH, requestPath);
        logData.put(LogConstants.LOG_KEY_REQUEST_PARAMS, params);

        log.info("[{}] API 요청 시작 - {} {}", LogConstants.LOG_CATEGORY_API, httpMethod, requestPath);
        log.debug("API 요청 상세: {}", logData);
    }

    /**
     * API 응답 로그
     */
    public static void logApiResponse(String httpMethod, String requestPath, int httpStatus, long executionTime) {
        String requestId = getCurrentRequestId();
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, requestId);
        logData.put(LogConstants.LOG_KEY_HTTP_METHOD, httpMethod);
        logData.put(LogConstants.LOG_KEY_REQUEST_PATH, requestPath);
        logData.put(LogConstants.LOG_KEY_HTTP_STATUS, httpStatus);
        logData.put(LogConstants.LOG_KEY_EXECUTION_TIME, executionTime);

        log.info("[{}] API 응답 완료 - {} {} ({}ms)", LogConstants.LOG_CATEGORY_API, httpMethod, requestPath,
            executionTime);
        log.debug("API 응답 상세: {}", logData);
    }

    /**
     * SAP 함수 실행 시작 로그
     */
    public static void logSapFunctionStart(String functionName) {
        String requestId = getCurrentRequestId();
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, requestId);
        logData.put(LogConstants.LOG_KEY_FUNCTION_NAME, functionName);

        log.info("[{}] SAP 함수 실행 시작 - {}", LogConstants.LOG_CATEGORY_SAP, functionName);
        log.debug("SAP 함수 실행 시작 상세: {}", logData);
    }

    /**
     * SAP 함수 실행 완료 로그
     */
    public static void logSapFunctionComplete(String functionName, long executionTime) {
        String requestId = getCurrentRequestId();
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, requestId);
        logData.put(LogConstants.LOG_KEY_FUNCTION_NAME, functionName);
        logData.put(LogConstants.LOG_KEY_EXECUTION_TIME, executionTime);

        log.info("[{}] SAP 함수 실행 완료 - {} ({}ms)", LogConstants.LOG_CATEGORY_SAP, functionName,
            executionTime);
        log.debug("SAP 함수 실행 완료 상세: {}", logData);

        // 성능 경고 (1초 이상)
        if (executionTime > 1000) {
            log.warn("[{}] 성능 경고 - {} ({}ms)", LogConstants.LOG_CATEGORY_PERFORMANCE, functionName,
                executionTime);
        }
    }

    /**
     * SAP 함수 실행 실패 로그
     */
    public static void logSapFunctionError(String functionName, String errorMessage, Throwable throwable) {
        String requestId = getCurrentRequestId();
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, requestId);
        logData.put(LogConstants.LOG_KEY_FUNCTION_NAME, functionName);
        logData.put(LogConstants.LOG_KEY_ERROR_MESSAGE, errorMessage);

        log.error("[{}] SAP 함수 실행 실패 - {} - {}", LogConstants.LOG_CATEGORY_SAP, functionName,
            errorMessage, throwable);
        log.debug("SAP 함수 실행 실패 상세: {}", logData);
    }

    /**
     * 검증 실패 로그
     */
    public static void logValidationError(String field, String message) {
        String requestId = getCurrentRequestId();
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, requestId);
        logData.put(LogConstants.LOG_KEY_ERROR_MESSAGE, message);

        log.warn("[{}] 검증 실패 - {}: {}", LogConstants.LOG_CATEGORY_VALIDATION, field, message);
        log.debug("검증 실패 상세: {}", logData);
    }

    /**
     * 예외 발생 로그
     */
    public static void logException(String errorCode, String message, Throwable throwable) {
        String requestId = getCurrentRequestId();
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, requestId);
        logData.put(LogConstants.LOG_KEY_ERROR_CODE, errorCode);
        logData.put(LogConstants.LOG_KEY_ERROR_MESSAGE, message);

        log.error("[{}] 예외 발생 - {}: {}", LogConstants.LOG_CATEGORY_EXCEPTION, errorCode, message,
            throwable);
        log.debug("예외 발생 상세: {}", logData);
    }

    /**
     * 함수 검색 로그
     */
    public static void logFunctionSearch(String pattern, String groupName, String language, int resultCount) {
        String requestId = getCurrentRequestId();
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, requestId);
        logData.put(LogConstants.LOG_KEY_PATTERN, pattern);
        logData.put(LogConstants.LOG_KEY_GROUP_NAME, groupName);
        logData.put(LogConstants.LOG_KEY_LANGUAGE, language);
        logData.put(LogConstants.LOG_KEY_RESULT_COUNT, resultCount);

        log.info("함수 검색 완료 - 패턴: {}, 그룹: {}, 언어: {}, 결과: {}개", pattern, groupName, language, resultCount);
        log.debug("함수 검색 상세: {}", logData);
    }

    /**
     * 함수 인터페이스 조회 로그
     */
    public static void logFunctionInterface(String functionName) {
        String requestId = getCurrentRequestId();
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, requestId);
        logData.put(LogConstants.LOG_KEY_FUNCTION_NAME, functionName);

        log.info("함수 인터페이스 조회 완료 - 함수: {}", functionName);
        log.debug("함수 인터페이스 조회 상세: {}", logData);
    }
}
