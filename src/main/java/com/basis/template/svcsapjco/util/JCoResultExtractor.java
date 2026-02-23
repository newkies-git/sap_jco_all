package com.basis.template.svcsapjco.util;

import com.basis.template.svcsapjco.exception.SapResultExtractionException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JCo 함수 실행 결과(Export/Table) 추출 전담 유틸리티.
 * 단일 책임: JCoFunction에서 Export/Table 파라미터를 추출하고, 실패 시 예외 전파.
 */
@Component
public class JCoResultExtractor {

    /**
     * Export 파라미터를 Map으로 추출한다. 추출 중 오류 시 예외를 던진다.
     *
     * @param function JCoFunction
     * @return Export 파라미터 Map (null이 아님)
     * @throws SapResultExtractionException 추출 실패 시
     */
    public Map<String, Object> extractExportParameters(JCoFunction function) {
        Map<String, Object> result = new HashMap<>();
        try {
            JCoParameterList exportParamList = function.getExportParameterList();
            if (exportParamList != null) {
                for (int i = 0; i < exportParamList.getFieldCount(); i++) {
                    var field = exportParamList.getField(i);
                    Object value = SapJcoDataUtil.getSafeValue(field);
                    result.put(field.getName(), value);
                }
            }
            return result;
        } catch (Exception e) {
            throw new SapResultExtractionException("Export 파라미터 추출 실패: " + e.getMessage(), e);
        }
    }

    /**
     * Table 파라미터를 테이블명 → 행 리스트(Map 리스트)로 추출한다. 추출 중 오류 시 예외를 던진다.
     *
     * @param function JCoFunction
     * @return 테이블명 → List&lt;Map&gt; (null이 아님)
     * @throws SapResultExtractionException 추출 실패 시
     */
    public Map<String, List<Map<String, Object>>> extractTableParameters(JCoFunction function) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        try {
            JCoParameterList tableParamList = function.getTableParameterList();
            if (tableParamList != null) {
                for (int i = 0; i < tableParamList.getFieldCount(); i++) {
                    var field = tableParamList.getField(i);
                    Object value = field.getValue();
                    if (value instanceof JCoTable table) {
                        result.put(field.getName(), SapJcoDataUtil.convertTableToList(table));
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new SapResultExtractionException("Table 파라미터 추출 실패: " + e.getMessage(), e);
        }
    }
}
