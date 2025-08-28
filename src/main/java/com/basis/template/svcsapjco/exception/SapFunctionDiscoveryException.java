package com.basis.template.svcsapjco.exception;

/**
 * SAP 함수 검색 관련 예외 클래스
 */
public class SapFunctionDiscoveryException extends SapJcoException {

    private final String pattern;

    public SapFunctionDiscoveryException(String pattern, String message) {
        super(message);
        this.pattern = pattern;
    }

    public SapFunctionDiscoveryException(String pattern, String message, Throwable cause) {
        super(message, cause);
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
