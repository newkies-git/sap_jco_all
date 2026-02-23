package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.dto.SapParameterMap;
import com.sap.conn.jco.JCoFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * SAP 함수 실행 오케스트레이션 전용 서비스.
 * 단일 책임: 검증 + 실행 요청 위임.
 */
@Service
@RequiredArgsConstructor
public class SapJcoService {

    private final SapJcoFunctionExecutor functionExecutor;
    private final SapJcoValidationService validationService;

    public JCoFunction executeFunction(String functionName, SapParameterMap importParams,
            SapParameterMap tables) {
        validationService.validateFunctionName(functionName);
        return functionExecutor.executeFunction(functionName, importParams, tables);
    }
}
