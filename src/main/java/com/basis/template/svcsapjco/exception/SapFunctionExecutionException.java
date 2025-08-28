package com.basis.template.svcsapjco.exception;

/**
 * SAP 함수 실행 관련 예외 클래스
 */
public class SapFunctionExecutionException extends SapJcoException {

    private final String functionName;

    public SapFunctionExecutionException(String functionName, String message) {
        super(message);
        this.functionName = functionName;
    }

    public SapFunctionExecutionException(String functionName, String message, Throwable cause) {
        super(message, cause);
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }
}
