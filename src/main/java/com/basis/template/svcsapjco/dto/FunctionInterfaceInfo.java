package com.basis.template.svcsapjco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * SAP 함수 인터페이스 정보를 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionInterfaceInfo {

    /**
     * 함수명
     */
    private String functionName;

    /**
     * Import 파라미터 목록
     */
    private List<ParameterInfo> importParameters = new ArrayList<>();

    /**
     * Export 파라미터 목록
     */
    private List<ParameterInfo> exportParameters = new ArrayList<>();

    /**
     * Table 파라미터 목록
     */
    private List<ParameterInfo> tableParameters = new ArrayList<>();

    /**
     * Changing 파라미터 목록
     */
    private List<ParameterInfo> changeParameters = new ArrayList<>();

    /**
     * Import 파라미터 추가
     */
    public void addImportParameter(String name, String type, String text) {
        importParameters.add(new ParameterInfo(name, type, text));
    }

    /**
     * Export 파라미터 추가
     */
    public void addExportParameter(String name, String type, String text) {
        exportParameters.add(new ParameterInfo(name, type, text));
    }

    /**
     * Table 파라미터 추가
     */
    public void addTableParameter(String name, String type, String text) {
        tableParameters.add(new ParameterInfo(name, type, text));
    }

    /**
     * Changing 파라미터 추가
     */
    public void addChangeParameter(String name, String type, String text) {
        changeParameters.add(new ParameterInfo(name, type, text));
    }

    /**
     * 파라미터 정보를 담는 내부 클래스
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParameterInfo {

        /**
         * 파라미터명
         */
        private String name;

        /**
         * 파라미터 타입
         */
        private String type;

        /**
         * 파라미터 설명
         */
        private String text;
    }
}
