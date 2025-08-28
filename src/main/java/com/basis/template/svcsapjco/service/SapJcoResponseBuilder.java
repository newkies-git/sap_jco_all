package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.dto.SapFunctionResponse;
import com.basis.template.svcsapjco.dto.SapTableData;
import com.basis.template.svcsapjco.util.SapJcoDataUtil;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SAP JCo 응답 생성 전용 서비스
 * 단일 책임: SAP 함수 실행 결과를 응답 형태로 변환하는 것만 담당
 */
@Slf4j
@Service
public class SapJcoResponseBuilder {

    /**
     * 성공 응답 생성
     */
    public SapFunctionResponse createSuccessResponse(String functionName, JCoFunction function) {
        // Export 파라미터 추출
        Map<String, Object> exportParams = extractExportParameters(function);
        
        // Table 파라미터 추출
        Map<String, SapTableData> tableResults = extractTableParameters(function);
        
        return SapFunctionResponse.of(functionName)
                .withExportParamsFromMap(exportParams)
                .withTableParams(tableResults);
    }

    /**
     * Export 파라미터 추출
     */
    private Map<String, Object> extractExportParameters(JCoFunction function) {
        Map<String, Object> result = new HashMap<>();

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
     * Table 파라미터 추출
     */
    private Map<String, SapTableData> extractTableParameters(JCoFunction function) {
        Map<String, Object> tableParams = extractTableParametersRaw(function);
        Map<String, SapTableData> tableResults = new HashMap<>();
        
        for (Map.Entry<String, Object> entry : tableParams.entrySet()) {
            if (entry.getValue() instanceof JCoTable) {
                JCoTable table = (JCoTable) entry.getValue();
                List<Map<String, Object>> tableData = SapJcoDataUtil.convertTableToList(table);
                tableResults.put(entry.getKey(), SapTableData.from(tableData));
            }
        }
        
        return tableResults;
    }

    /**
     * Table 파라미터 원시 데이터 추출
     */
    private Map<String, Object> extractTableParametersRaw(JCoFunction function) {
        Map<String, Object> result = new HashMap<>();

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
