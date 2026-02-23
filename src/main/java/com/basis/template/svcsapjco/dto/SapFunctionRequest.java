package com.basis.template.svcsapjco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * SAP 함수 실행 요청을 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SapFunctionRequest {

    /**
     * 실행할 SAP 함수명
     */
    private String functionName;

    /**
     * Import 파라미터
     */
    private SapParameterMap importParams = SapParameterMap.of();

    /**
     * Table 파라미터
     */
    private SapParameterMap tables = SapParameterMap.of();

    /**
     * 함수명만으로 요청을 생성하는 정적 팩토리 메서드
     */
    public static SapFunctionRequest of(String functionName) {
        SapFunctionRequest request = new SapFunctionRequest();
        request.functionName = functionName;
        request.importParams = SapParameterMap.of();
        request.tables = SapParameterMap.of();
        return request;
    }

    /**
     * 함수명과 Import 파라미터로 요청을 생성하는 정적 팩토리 메서드
     */
    public static SapFunctionRequest of(String functionName, Map<String, Object> importParams) {
        SapFunctionRequest request = new SapFunctionRequest();
        request.functionName = functionName;
        request.importParams = SapParameterMap.from(importParams);
        request.tables = SapParameterMap.of();
        return request;
    }
}
