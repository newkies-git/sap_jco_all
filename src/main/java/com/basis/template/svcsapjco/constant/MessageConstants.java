package com.basis.template.svcsapjco.constant;

/**
 * 메시지 관련 상수 클래스
 */
public final class MessageConstants {

    private MessageConstants() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }

    // 로그 메시지 상수
    public static final String LOG_FUNCTION_SEARCH_START = "패턴 '{}', 그룹 '{}', 언어 '{}'으로 함수 검색 시작";
    public static final String LOG_FUNCTION_SEARCH_COMPLETE = "패턴 '{}'으로 {} 개의 함수를 검색했습니다";
    public static final String LOG_FUNCTION_SEARCH_FAILED = "패턴 '{}'으로 함수 검색 실패: {}";
    public static final String LOG_FUNCTION_INTERFACE_START = "함수 '{}'의 인터페이스 정보 조회 시작";
    public static final String LOG_FUNCTION_INTERFACE_COMPLETE = "함수 '{}'의 인터페이스 정보 조회 완료";
    public static final String LOG_FUNCTION_INTERFACE_FAILED = "함수 '{}'의 인터페이스 정보 조회 실패: {}";
    public static final String LOG_FUNCTION_EXECUTION_REQUEST = "SAP 함수 호출 요청: {}";
    public static final String LOG_FUNCTION_EXECUTION_COMPLETE = "SAP 함수 호출 완료: {}";
    public static final String LOG_FUNCTION_EXECUTION_FAILED = "SAP 함수 호출 중 오류 발생: {} - {}";
    public static final String LOG_SAP_CONNECTION_PING_START = "SAP 연결 상태 확인 시작";
    public static final String LOG_SAP_CONNECTION_PING_SUCCESS = "SAP 연결 ping 성공";
    public static final String LOG_SAP_CONNECTION_PING_FAILED = "SAP 연결 ping 실패: {}";
    public static final String LOG_SAP_CONNECTION_PROPERTIES_COMPLETE = "SAP 연결 속성 생성 완료: ashost={}, sysnr={}, client={}, user={}, lang={}";
    public static final String LOG_PATTERN_TOO_SHORT = "검색 패턴이 2자리 미만입니다: '{}'";
    public static final String LOG_PATTERN_CONVERSION = "패턴 변환: '{}' -> '{}' (SAP ABAP 와일드카드)";
    public static final String LOG_PATTERN_NULL = "패턴이 null입니다. 기본값 '*' 반환";
    public static final String LOG_PATTERN_EMPTY = "패턴이 공백만 포함되어 있습니다. 기본값 '*' 반환";
    public static final String LOG_PARAMS_COUNT = "PARAMS 구조체에서 {} 개의 파라미터 정보를 조회합니다";
    public static final String LOG_PARAM_DETAILS = "파라미터 {}: CLASS={}, NAME={}, TABLE={}, FIELD={}, EXID={}, POS={}, OFFSET={}, LENGTH={}, DEC={}, DEFAULT={}, TEXT={}, OPT={}";
    public static final String LOG_UNKNOWN_PARAM_CLASS = "알 수 없는 파라미터 클래스: {} (파라미터: {})";

    // API 응답 메시지 상수
    public static final String API_FUNCTION_SEARCH_REQUEST = "패턴 '{}', 그룹 '{}', 언어 '{}'으로 함수 검색 요청";
    public static final String API_FUNCTION_SEARCH_COMPLETE = "패턴 '{}'으로 함수 검색 완료: {} 개";
    public static final String API_FUNCTION_SEARCH_ERROR = "패턴 '{}'으로 함수 검색 중 오류 발생: {}";
    public static final String API_FUNCTION_INTERFACE_REQUEST = "함수 '{}'의 인터페이스 정보 조회 요청";
    public static final String API_FUNCTION_INTERFACE_COMPLETE = "함수 '{}'의 인터페이스 정보 조회 완료";
    public static final String API_FUNCTION_INTERFACE_ERROR = "함수 '{}'의 인터페이스 정보 조회 중 오류 발생: {}";

    // 에러 메시지 상수
    public static final String ERROR_FUNCTION_SEARCH_FAILED = "함수 검색 실패: ";
    public static final String ERROR_FUNCTION_INTERFACE_FAILED = "함수 인터페이스 정보 조회 실패: ";
    public static final String ERROR_SAP_FUNCTION_EXECUTION_FAILED = "SAP 함수 호출 실패: ";
    public static final String ERROR_INPUT_VALIDATION_FAILED = "입력값 검증 실패";
    public static final String ERROR_CONSTRAINT_VIOLATION = "제약 조건 위반: ";
    public static final String ERROR_INVALID_ARGUMENT_TYPE = "잘못된 인자 타입: ";
    public static final String ERROR_INVALID_ARGUMENT = "잘못된 인자: ";
    public static final String ERROR_INTERNAL_SERVER_ERROR = "서버 내부 오류가 발생했습니다.";

    // 검증 메시지 상수
    public static final String MSG_FUNCTION_NAME_EMPTY = "함수명이 비어있습니다.";
    public static final String MSG_REQUEST_NULL = "요청 데이터가 null입니다.";
    public static final String MSG_PATTERN_TOO_SHORT = "검색 패턴은 2자리 이상이어야 합니다.";
}
