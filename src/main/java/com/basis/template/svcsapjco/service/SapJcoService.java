package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.dto.SapParameterMap;
import com.basis.template.svcsapjco.util.SapJcoDataUtil;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SapJcoService {

    private final SapJcoFunctionExecutor functionExecutor;
    private final SapJcoValidationService validationService;

    /**
     * RFC/BAPI 함수를 호출하는 공통 메서드
     *
     * @param functionName RFC/BAPI 함수명
     * @param importParams Import 파라미터
     * @param tables       Tables 파라미터
     * @return JCoFunction 실행 결과
     */
    public JCoFunction executeFunction(String functionName, SapParameterMap importParams,
        SapParameterMap tables) {
        validationService.validateFunctionName(functionName);
        return functionExecutor.executeFunction(functionName, importParams, tables);
    }

    /**
     * Import 파라미터만으로 RFC/BAPI 함수를 호출하는 메서드
     *
     * @param functionName RFC/BAPI 함수명
     * @param importParams Import 파라미터
     * @return JCoFunction 실행 결과
     */
    public JCoFunction executeFunction(String functionName, SapParameterMap importParams) {
        return executeFunction(functionName, importParams, null);
    }
    
    /**
     * Import 파라미터만으로 RFC/BAPI 함수를 호출하는 메서드 (Map으로부터)
     *
     * @param functionName RFC/BAPI 함수명
     * @param importParams Import 파라미터
     * @return JCoFunction 실행 결과
     */
    public JCoFunction executeFunction(String functionName, Map<String, Object> importParams) {
        return executeFunction(functionName, SapParameterMap.from(importParams), null);
    }

    /**
     * 파라미터 없이 RFC/BAPI 함수를 호출하는 메서드
     *
     * @param functionName RFC/BAPI 함수명
     * @return JCoFunction 실행 결과
     */
    public JCoFunction executeFunction(String functionName) {
        return executeFunction(functionName, null, null);
    }

    /**
     * JCoFunction의 Export 파라미터를 Map으로 변환
     *
     * @param function JCoFunction
     * @return Export 파라미터 Map
     */
    public Map<String, Object> getExportParameters(JCoFunction function) {
        return extractExportParameters(function);
    }

    /**
     * JCoFunction의 Table 파라미터를 Map으로 변환
     *
     * @param function JCoFunction
     * @return Table 파라미터 Map
     */
    public Map<String, Object> getTableParameters(JCoFunction function) {
        return extractTableParameters(function);
    }

    /**
     * JCoTable을 Map 리스트로 변환
     *
     * @param table JCoTable
     * @return Map 리스트
     */
    public List<Map<String, Object>> tableToList(JCoTable table) {
        return SapJcoDataUtil.convertTableToList(table);
    }

    /**
     * Export 파라미터 직접 추출
     */
    private Map<String, Object> extractExportParameters(JCoFunction function) {
        Map<String, Object> result = new java.util.HashMap<>();

        try {
            com.sap.conn.jco.JCoParameterList exportParamList = function.getExportParameterList();
            if (exportParamList != null) {
                for (int i = 0; i < exportParamList.getFieldCount(); i++) {
                    com.sap.conn.jco.JCoField field = exportParamList.getField(i);
                    Object value = SapJcoDataUtil.getSafeValue(field);
                    result.put(field.getName(), value);
                }
            }
        } catch (Exception e) {
            log.error("Export 파라미터 추출 중 오류: {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * Table 파라미터 직접 추출
     */
    private Map<String, Object> extractTableParameters(JCoFunction function) {
        Map<String, Object> result = new java.util.HashMap<>();

        try {
            com.sap.conn.jco.JCoParameterList tableParamList = function.getTableParameterList();
            if (tableParamList != null) {
                for (int i = 0; i < tableParamList.getFieldCount(); i++) {
                    com.sap.conn.jco.JCoField field = tableParamList.getField(i);
                    Object value = SapJcoDataUtil.getSafeValue(field);
                    result.put(field.getName(), value);
                }
            }
        } catch (Exception e) {
            log.error("Table 파라미터 추출 중 오류: {}", e.getMessage(), e);
        }

        return result;
    }
}
