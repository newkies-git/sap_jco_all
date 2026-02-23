package com.basis.template.svcsapjco.util;

import com.basis.template.svcsapjco.constant.LogConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 구조화된 로깅 유틸리티.
 * 요청 컨텍스트(requestId, startTime)는 RequestContext에서 조회한다.
 */
@Slf4j
@Component
public class StructuredLogger {

    /**
     * 요청 ID 생성 및 설정 (RequestContext 위임). 인터셉터 등에서 호출.
     */
    public static String generateRequestId() {
        return RequestContext.generateRequestId();
    }

    public static String getCurrentRequestId() {
        return RequestContext.getCurrentRequestId();
    }

    public static Long getStartTime() {
        return RequestContext.getStartTime();
    }

    public static long calculateExecutionTime() {
        return RequestContext.calculateExecutionTime();
    }

    /**
     * ThreadLocal 정리 (RequestContext 위임). 요청 종료 시 호출.
     */
    public static void clear() {
        RequestContext.clear();
    }

    public static void logApiRequest(String httpMethod, String requestPath, Map<String, Object> params) {
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, RequestContext.getCurrentRequestId());
        logData.put(LogConstants.LOG_KEY_HTTP_METHOD, httpMethod);
        logData.put(LogConstants.LOG_KEY_REQUEST_PATH, requestPath);
        logData.put(LogConstants.LOG_KEY_REQUEST_PARAMS, params);
        log.info("[{}] API 요청 시작 - {} {}", LogConstants.LOG_CATEGORY_API, httpMethod, requestPath);
        log.debug("API 요청 상세: {}", logData);
    }

    public static void logApiResponse(String httpMethod, String requestPath, int httpStatus, long executionTime) {
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, RequestContext.getCurrentRequestId());
        logData.put(LogConstants.LOG_KEY_HTTP_METHOD, httpMethod);
        logData.put(LogConstants.LOG_KEY_REQUEST_PATH, requestPath);
        logData.put(LogConstants.LOG_KEY_HTTP_STATUS, httpStatus);
        logData.put(LogConstants.LOG_KEY_EXECUTION_TIME, executionTime);
        log.info("[{}] API 응답 완료 - {} {} ({}ms)", LogConstants.LOG_CATEGORY_API, httpMethod, requestPath, executionTime);
        log.debug("API 응답 상세: {}", logData);
    }

    public static void logSapFunctionStart(String functionName) {
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, RequestContext.getCurrentRequestId());
        logData.put(LogConstants.LOG_KEY_FUNCTION_NAME, functionName);
        log.info("[{}] SAP 함수 실행 시작 - {}", LogConstants.LOG_CATEGORY_SAP, functionName);
        log.debug("SAP 함수 실행 시작 상세: {}", logData);
    }

    public static void logSapFunctionComplete(String functionName, long executionTime) {
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, RequestContext.getCurrentRequestId());
        logData.put(LogConstants.LOG_KEY_FUNCTION_NAME, functionName);
        logData.put(LogConstants.LOG_KEY_EXECUTION_TIME, executionTime);
        log.info("[{}] SAP 함수 실행 완료 - {} ({}ms)", LogConstants.LOG_CATEGORY_SAP, functionName, executionTime);
        log.debug("SAP 함수 실행 완료 상세: {}", logData);
        if (executionTime > 1000) {
            log.warn("[{}] 성능 경고 - {} ({}ms)", LogConstants.LOG_CATEGORY_PERFORMANCE, functionName, executionTime);
        }
    }

    public static void logSapFunctionError(String functionName, String errorMessage, Throwable throwable) {
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, RequestContext.getCurrentRequestId());
        logData.put(LogConstants.LOG_KEY_FUNCTION_NAME, functionName);
        logData.put(LogConstants.LOG_KEY_ERROR_MESSAGE, errorMessage);
        log.error("[{}] SAP 함수 실행 실패 - {} - {}", LogConstants.LOG_CATEGORY_SAP, functionName, errorMessage, throwable);
        log.debug("SAP 함수 실행 실패 상세: {}", logData);
    }

    public static void logValidationError(String field, String message) {
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, RequestContext.getCurrentRequestId());
        logData.put(LogConstants.LOG_KEY_ERROR_MESSAGE, message);
        log.warn("[{}] 검증 실패 - {}: {}", LogConstants.LOG_CATEGORY_VALIDATION, field, message);
        log.debug("검증 실패 상세: {}", logData);
    }

    public static void logException(String errorCode, String message, Throwable throwable) {
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, RequestContext.getCurrentRequestId());
        logData.put(LogConstants.LOG_KEY_ERROR_CODE, errorCode);
        logData.put(LogConstants.LOG_KEY_ERROR_MESSAGE, message);
        log.error("[{}] 예외 발생 - {}: {}", LogConstants.LOG_CATEGORY_EXCEPTION, errorCode, message, throwable);
        log.debug("예외 발생 상세: {}", logData);
    }

    public static void logFunctionSearch(String pattern, String groupName, String language, int resultCount) {
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, RequestContext.getCurrentRequestId());
        logData.put(LogConstants.LOG_KEY_PATTERN, pattern);
        logData.put(LogConstants.LOG_KEY_GROUP_NAME, groupName);
        logData.put(LogConstants.LOG_KEY_LANGUAGE, language);
        logData.put(LogConstants.LOG_KEY_RESULT_COUNT, resultCount);
        log.info("함수 검색 완료 - 패턴: {}, 그룹: {}, 언어: {}, 결과: {}개", pattern, groupName, language, resultCount);
        log.debug("함수 검색 상세: {}", logData);
    }

    public static void logFunctionInterface(String functionName) {
        Map<String, Object> logData = new HashMap<>();
        logData.put(LogConstants.LOG_KEY_REQUEST_ID, RequestContext.getCurrentRequestId());
        logData.put(LogConstants.LOG_KEY_FUNCTION_NAME, functionName);
        log.info("함수 인터페이스 조회 완료 - 함수: {}", functionName);
        log.debug("함수 인터페이스 조회 상세: {}", logData);
    }
}
