package com.basis.template.svcsapjco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SAP 함수 파라미터 정보를 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterInfo {

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
