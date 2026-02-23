package com.basis.template.svcsapjco.exception;

import com.basis.template.svcsapjco.constant.ErrorCodeConstants;

/**
 * SAP 함수 결과(Export/Table) 추출 실패 시 사용하는 예외
 */
public class SapResultExtractionException extends SapJcoException {

    public SapResultExtractionException(String message) {
        super(message);
    }

    public SapResultExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return ErrorCodeConstants.SAP_RESULT_EXTRACTION_ERROR;
    }
}
