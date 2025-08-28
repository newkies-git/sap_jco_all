package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.dto.SapParameterMap;
import com.sap.conn.jco.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * SAP 함수 생성 및 설정 전용 서비스 단일 책임: SAP 함수 템플릿 생성 및 파라미터 설정만 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SapJcoFunctionBuilder {

    private final SapJcoConnectionManager connectionManager;

    /**
     * 함수 생성 및 설정
     */
    public JCoFunction createAndSetupFunction(String functionName, SapParameterMap importParams, SapParameterMap tables)
        throws JCoException {
        // 함수 템플릿 가져오기
        JCoFunction function = getFunctionTemplate(functionName);

        // Import 파라미터 설정
        setupImportParameters(function, importParams);

        // Tables 파라미터 설정
        setupTableParameters(function, tables);

        return function;
    }

    /**
     * 함수 템플릿 가져오기
     */
    private JCoFunction getFunctionTemplate(String functionName) throws JCoException {
        log.debug("SAP Repository 가져오기 시작");
        JCoRepository repository = connectionManager.getDestination().getRepository();
        log.debug("SAP Repository 가져오기 완료");

        log.debug("함수 템플릿 가져오기 시작: {}", functionName);
        JCoFunction function = repository.getFunctionTemplate(functionName).getFunction();
        log.debug("함수 템플릿 가져오기 완료: {}", functionName);

        return function;
    }

    /**
     * Import 파라미터 설정
     */
    private void setupImportParameters(JCoFunction function, SapParameterMap importParams) {
        if (importParams != null && !importParams.isEmpty()) {
            log.debug("Import 파라미터 설정 시작: {}", importParams.toMap());
            JCoParameterList importParamList = function.getImportParameterList();
            setParameters(importParamList, importParams.toMap());
            log.debug("Import 파라미터 설정 완료");
        }
    }

    /**
     * Tables 파라미터 설정
     */
    private void setupTableParameters(JCoFunction function, SapParameterMap tables) {
        if (tables != null && !tables.isEmpty()) {
            log.debug("Tables 파라미터 설정 시작: {}", tables.toMap());
            JCoParameterList tableParamList = function.getTableParameterList();
            setParameters(tableParamList, tables.toMap());
            log.debug("Tables 파라미터 설정 완료");
        }
    }

    /**
     * JCoParameterList에 파라미터 값을 설정하는 헬퍼 메서드
     */
    private void setParameters(JCoParameterList paramList, Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String paramName = entry.getKey();
            Object paramValue = entry.getValue();

            if (paramValue != null) {
                try {
                    paramList.setValue(paramName, paramValue);
                } catch (Exception e) {
                    log.warn("파라미터 설정 실패: {} = {} - {}", paramName, paramValue, e.getMessage());
                }
            }
        }
    }

    /**
     * Destination 반환
     */
    public JCoDestination getDestination() {
        return connectionManager.getDestination();
    }
}
