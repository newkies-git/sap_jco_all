package com.basis.template.svcsapjco.constant;

/**
 * 로깅 관련 상수 클래스
 */
public final class LogConstants {
    
    private LogConstants() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }
    
    // 로그 레벨 상수
    public static final String LOG_LEVEL_DEBUG = "DEBUG";
    public static final String LOG_LEVEL_INFO = "INFO";
    public static final String LOG_LEVEL_WARN = "WARN";
    public static final String LOG_LEVEL_ERROR = "ERROR";
    
    // 로그 카테고리 상수
    public static final String LOG_CATEGORY_SAP = "SAP";
    public static final String LOG_CATEGORY_API = "API";
    public static final String LOG_CATEGORY_VALIDATION = "VALIDATION";
    public static final String LOG_CATEGORY_EXCEPTION = "EXCEPTION";
    public static final String LOG_CATEGORY_PERFORMANCE = "PERFORMANCE";
    
    // 로그 키 상수
    public static final String LOG_KEY_FUNCTION_NAME = "functionName";
    public static final String LOG_KEY_PATTERN = "pattern";
    public static final String LOG_KEY_GROUP_NAME = "groupName";
    public static final String LOG_KEY_LANGUAGE = "language";
    public static final String LOG_KEY_RESULT_COUNT = "resultCount";
    public static final String LOG_KEY_EXECUTION_TIME = "executionTime";
    public static final String LOG_KEY_ERROR_CODE = "errorCode";
    public static final String LOG_KEY_ERROR_MESSAGE = "errorMessage";
    public static final String LOG_KEY_REQUEST_ID = "requestId";
    public static final String LOG_KEY_USER_ID = "userId";
    public static final String LOG_KEY_CLIENT_IP = "clientIp";
    public static final String LOG_KEY_HTTP_METHOD = "httpMethod";
    public static final String LOG_KEY_HTTP_STATUS = "httpStatus";
    public static final String LOG_KEY_REQUEST_PATH = "requestPath";
    public static final String LOG_KEY_REQUEST_PARAMS = "requestParams";
    public static final String LOG_KEY_RESPONSE_SIZE = "responseSize";
    
    // 로그 포맷 상수
    public static final String LOG_FORMAT_STRUCTURED = "{} | {} | {} | {} | {}";
    public static final String LOG_FORMAT_SIMPLE = "{} | {}";
    public static final String LOG_FORMAT_DETAILED = "{} | {} | {} | {} | {} | {} | {}";
}
