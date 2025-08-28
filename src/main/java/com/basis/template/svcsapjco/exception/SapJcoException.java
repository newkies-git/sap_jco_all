package com.basis.template.svcsapjco.exception;

import com.basis.template.svcsapjco.constant.ErrorCodeConstants;

/**
 * SAP JCo 관련 예외의 기본 클래스
 */
public abstract class SapJcoException extends RuntimeException {

    private final String errorCode;

    public SapJcoException(String message) {
        super(message);
        this.errorCode = ErrorCodeConstants.SAP_FUNCTION_EXECUTION_ERROR;
    }

    public SapJcoException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCodeConstants.SAP_FUNCTION_EXECUTION_ERROR;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
