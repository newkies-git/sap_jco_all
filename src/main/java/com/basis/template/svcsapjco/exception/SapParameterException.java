package com.basis.template.svcsapjco.exception;

import com.basis.template.svcsapjco.constant.ErrorCodeConstants;

/**
 * SAP 함수 파라미터 설정 실패 시 사용하는 예외
 */
public class SapParameterException extends SapJcoException {

    private final String parameterName;
    private final Object parameterValue;

    public SapParameterException(String parameterName, Object parameterValue, String message) {
        super(message);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public SapParameterException(String parameterName, Object parameterValue, String message, Throwable cause) {
        super(message, cause);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    @Override
    public String getErrorCode() {
        return ErrorCodeConstants.SAP_PARAMETER_ERROR;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Object getParameterValue() {
        return parameterValue;
    }
}
