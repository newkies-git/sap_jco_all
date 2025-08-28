package com.basis.template.svcsapjco.exception;

/**
 * SAP 연결 관련 예외 클래스
 */
public class SapConnectionException extends SapJcoException {

    public SapConnectionException(String message) {
        super(message);
    }

    public SapConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
