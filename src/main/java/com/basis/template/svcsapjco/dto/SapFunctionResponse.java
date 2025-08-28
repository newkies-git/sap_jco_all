package com.basis.template.svcsapjco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SAP 함수 실행 결과를 위한 응답 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SapFunctionResponse {

    /**
     * 실행된 함수명
     */
    private String functionName;

    /**
     * Export 파라미터
     */
    private SapParameterMap exportParams;

    /**
     * Table 파라미터
     */
    private Map<String, SapTableData> tableParams;

    /**
     * 함수명으로 응답을 생성하는 정적 팩토리 메서드
     */
    public static SapFunctionResponse of(String functionName) {
        SapFunctionResponse response = new SapFunctionResponse();
        response.functionName = functionName;
        response.exportParams = SapParameterMap.of();
        response.tableParams = new HashMap<>();
        return response;
    }

    /**
     * Export 파라미터를 설정하는 빌더 메서드
     */
    public SapFunctionResponse withExportParams(SapParameterMap exportParams) {
        this.exportParams = exportParams;
        return this;
    }

    /**
     * Export 파라미터를 설정하는 빌더 메서드 (Map으로부터)
     */
    public SapFunctionResponse withExportParamsFromMap(Map<String, Object> exportParams) {
        this.exportParams = SapParameterMap.from(exportParams);
        return this;
    }

    /**
     * Table 파라미터를 설정하는 빌더 메서드
     */
    public SapFunctionResponse withTableParams(Map<String, SapTableData> tableParams) {
        this.tableParams = tableParams;
        return this;
    }

    /**
     * Table 파라미터를 설정하는 빌더 메서드 (Map으로부터)
     */
    public SapFunctionResponse withTableParamsFromMap(Map<String, List<Map<String, Object>>> tableParams) {
        Map<String, SapTableData> convertedTableParams = new HashMap<>();
        if (tableParams != null) {
            for (Map.Entry<String, List<Map<String, Object>>> entry : tableParams.entrySet()) {
                convertedTableParams.put(entry.getKey(), SapTableData.from(entry.getValue()));
            }
        }
        this.tableParams = convertedTableParams;
        return this;
    }
}
