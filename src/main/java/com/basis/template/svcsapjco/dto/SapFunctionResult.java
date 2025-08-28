package com.basis.template.svcsapjco.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 타입 안전한 SAP 함수 실행 결과
 * 제네릭을 활용하여 함수 실행 결과의 타입 안전성을 보장
 */
@Data
@NoArgsConstructor
public class SapFunctionResult {
    
    private String functionName;
    private SapParameterMap exportParams;
    private Map<String, SapTableData> tableParams;
    
    public SapFunctionResult(String functionName) {
        this.functionName = functionName;
        this.exportParams = SapParameterMap.of();
        this.tableParams = new HashMap<>();
    }
    
    /**
     * Export 파라미터 설정
     */
    public SapFunctionResult withExportParams(SapParameterMap exportParams) {
        this.exportParams = exportParams != null ? exportParams : SapParameterMap.of();
        return this;
    }
    
    /**
     * Export 파라미터 설정 (Map으로부터)
     */
    public SapFunctionResult withExportParams(Map<String, Object> exportParams) {
        this.exportParams = SapParameterMap.from(exportParams);
        return this;
    }
    
    /**
     * Table 파라미터 추가
     */
    public SapFunctionResult withTableParam(String tableName, SapTableData tableData) {
        this.tableParams.put(tableName, tableData);
        return this;
    }
    
    /**
     * Table 파라미터 추가 (List<Map>으로부터)
     */
    public SapFunctionResult withTableParam(String tableName, java.util.List<Map<String, Object>> tableList) {
        this.tableParams.put(tableName, SapTableData.from(tableList));
        return this;
    }
    
    /**
     * 특정 Export 파라미터 가져오기
     */
    public <T> T getExportParam(String key, Class<T> type) {
        return exportParams != null ? exportParams.get(key, type) : null;
    }
    
    /**
     * 특정 Export 파라미터 가져오기 (기본값과 함께)
     */
    public <T> T getExportParam(String key, Class<T> type, T defaultValue) {
        return exportParams != null ? exportParams.get(key, type, defaultValue) : defaultValue;
    }
    
    /**
     * 특정 Table 파라미터 가져오기
     */
    public SapTableData getTableParam(String tableName) {
        return tableParams.get(tableName);
    }
    
    /**
     * Table 파라미터 존재 여부 확인
     */
    public boolean hasTableParam(String tableName) {
        return tableParams.containsKey(tableName);
    }
    
    /**
     * 모든 Table 파라미터 이름 가져오기
     */
    public java.util.Set<String> getTableParamNames() {
        return tableParams.keySet();
    }
    
    /**
     * 정적 팩토리 메서드
     */
    public static SapFunctionResult of(String functionName) {
        return new SapFunctionResult(functionName);
    }
    
    /**
     * Map<String, List<Map<String, Object>>>로 변환 (호환성을 위해)
     */
    public Map<String, java.util.List<Map<String, Object>>> getTableParamsAsMap() {
        Map<String, java.util.List<Map<String, Object>>> result = new HashMap<>();
        for (Map.Entry<String, SapTableData> entry : tableParams.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toList());
        }
        return result;
    }
}
