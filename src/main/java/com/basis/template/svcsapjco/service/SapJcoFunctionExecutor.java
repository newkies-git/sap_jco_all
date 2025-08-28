package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.dto.SapParameterMap;
import com.basis.template.svcsapjco.exception.SapFunctionExecutionException;
import com.basis.template.svcsapjco.util.StructuredLogger;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * SAP 함수 실행 전용 서비스
 * 단일 책임: SAP 함수의 실제 실행만 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SapJcoFunctionExecutor {

    private final SapJcoFunctionBuilder functionBuilder;
    private final SapJcoConnectionManager connectionManager;

    /**
     * SAP 함수 실행
     */
    public JCoFunction executeFunction(String functionName, SapParameterMap importParams, SapParameterMap tables) {
        try {
            // SAP 함수 실행 시작 로그
            StructuredLogger.logSapFunctionStart(functionName);
            long startTime = System.currentTimeMillis();
            
            // 연결 상태 확인
            connectionManager.checkConnection();

            // 함수 생성 및 설정
            JCoFunction function = functionBuilder.createAndSetupFunction(functionName, importParams, tables);

            // 함수 실행
            executeFunction(function, functionName);
            
            // SAP 함수 실행 완료 로그
            long executionTime = System.currentTimeMillis() - startTime;
            StructuredLogger.logSapFunctionComplete(functionName, executionTime);

            return function;

        } catch (Exception e) {
            StructuredLogger.logSapFunctionError(functionName, e.getMessage(), e);
            throw new SapFunctionExecutionException(functionName, "SAP 함수 실행 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 함수 실행
     */
    private void executeFunction(JCoFunction function, String functionName) throws JCoException {
        log.info("SAP 함수 실행 시작: {}", functionName);
        function.execute(functionBuilder.getDestination());
        log.info("SAP 함수 실행 완료: {}", functionName);
    }
}
