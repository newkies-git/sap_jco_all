package com.basis.template.svcsapjco.dto;

import com.basis.template.svcsapjco.constant.SapConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SAP 함수 검색 결과를 위한 응답 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SapFunctionSearchResponse {

    /**
     * 검색 패턴
     */
    private String pattern;

    /**
     * 함수 그룹명
     */
    private String groupName;

    /**
     * 언어 코드
     */
    private String language;

    /**
     * 검색된 함수 목록
     */
    private List<FunctionInfo> functions;

    /**
     * 검색된 함수 개수
     */
    private int count;

    /**
     * 데이터 소스
     */
    private String source;

    /**
     * 검색 패턴으로 응답을 생성하는 정적 팩토리 메서드
     */
    public static SapFunctionSearchResponse of(String pattern) {
        SapFunctionSearchResponse response = new SapFunctionSearchResponse();
        response.pattern = pattern;
        response.groupName = null;
        response.language = null;
        response.functions = null;
        response.count = 0;
        response.source = SapConstants.DATA_SOURCE_SAP_SYSTEM;
        return response;
    }

    /**
     * 함수 목록을 설정하는 빌더 메서드
     */
    public SapFunctionSearchResponse withFunctions(List<FunctionInfo> functions) {
        this.functions = functions;
        this.count = functions != null ? functions.size() : 0;
        return this;
    }

    /**
     * 그룹명을 설정하는 빌더 메서드
     */
    public SapFunctionSearchResponse withGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    /**
     * 언어를 설정하는 빌더 메서드
     */
    public SapFunctionSearchResponse withLanguage(String language) {
        this.language = language;
        return this;
    }
}
