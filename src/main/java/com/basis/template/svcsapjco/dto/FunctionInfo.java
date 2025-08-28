package com.basis.template.svcsapjco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SAP 함수 정보를 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionInfo {

    /**
     * 함수명
     */
    private String functionName;

    /**
     * 함수 그룹명
     */
    private String groupName;

    /**
     * 애플리케이션
     */
    private String application;

    /**
     * 호스트
     */
    private String host;

    /**
     * 함수 설명
     */
    private String description;
}
