package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.dto.SapParameterMap;
import com.basis.template.svcsapjco.util.JCoResultExtractor;
import com.basis.template.svcsapjco.util.SapJcoDataUtil;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SAP 함수 실행 오케스트레이션 전용 서비스.
 * 단일 책임: 검증 + 실행 요청 + (필요 시) 결과 추출 위임.
 */
@Service
@RequiredArgsConstructor
public class SapJcoService {

    private final SapJcoFunctionExecutor functionExecutor;
    private final SapJcoValidationService validationService;
    private final JCoResultExtractor resultExtractor;

    public JCoFunction executeFunction(String functionName, SapParameterMap importParams,
            SapParameterMap tables) {
        validationService.validateFunctionName(functionName);
        return functionExecutor.executeFunction(functionName, importParams, tables);
    }

    public JCoFunction executeFunction(String functionName, SapParameterMap importParams) {
        return executeFunction(functionName, importParams, null);
    }

    public JCoFunction executeFunction(String functionName, Map<String, Object> importParams) {
        return executeFunction(functionName, SapParameterMap.from(importParams), null);
    }

    public JCoFunction executeFunction(String functionName) {
        return executeFunction(functionName, null, null);
    }

    /**
     * JCoFunction의 Export 파라미터를 Map으로 변환 (추출 실패 시 예외 전파)
     */
    public Map<String, Object> getExportParameters(JCoFunction function) {
        return resultExtractor.extractExportParameters(function);
    }

    /**
     * JCoFunction의 Table 파라미터를 Map으로 변환 (추출 실패 시 예외 전파)
     * 값은 List&lt;Map&lt;String, Object&gt;&gt; (테이블 행 리스트)
     */
    public Map<String, Object> getTableParameters(JCoFunction function) {
        Map<String, List<Map<String, Object>>> extracted = resultExtractor.extractTableParameters(function);
        return new java.util.HashMap<>(extracted);
    }

    /**
     * JCoTable을 Map 리스트로 변환 (SapJcoDataUtil 위임)
     */
    public List<Map<String, Object>> tableToList(JCoTable table) {
        return SapJcoDataUtil.convertTableToList(table);
    }
}
