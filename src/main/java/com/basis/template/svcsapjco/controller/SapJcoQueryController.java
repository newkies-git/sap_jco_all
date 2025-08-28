package com.basis.template.svcsapjco.controller;

import com.basis.template.svcsapjco.dto.ApiResponse;
import com.basis.template.svcsapjco.dto.FunctionInfo;
import com.basis.template.svcsapjco.dto.SapFunctionSearchResponse;
import com.basis.template.svcsapjco.dto.SapParameterMap;
import com.basis.template.svcsapjco.service.SapJcoFunctionDiscoveryService;
import com.basis.template.svcsapjco.service.SapJcoValidationService;
import com.basis.template.svcsapjco.util.StructuredLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sap/functions")
@RequiredArgsConstructor
public class SapJcoQueryController {

    private final SapJcoFunctionDiscoveryService sapJcoFunctionDiscoveryService;
    private final SapJcoValidationService validationService;


    /**
     * 패턴으로 함수 검색 API (RFC_FUNCTION_SEARCH 사용)
     *
     * @param pattern   검색 패턴 (예: "BAPI_*", "Z*", "RFC_*") - 2자리 이상 필요
     * @param groupName 함수 그룹명 (예: "SPACE", "BAPI", "Z*") - 선택사항
     * @param language  언어 코드 (예: "KO", "EN", "DE") - 선택사항
     * @return 검색된 함수 정보 목록
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<SapFunctionSearchResponse>> searchFunctions(@RequestParam String pattern,
        @RequestParam(required = false) String groupName, @RequestParam(required = false) String language) {

        // 패턴 검증: 2자리 이상 필요
        validationService.validateSearchPattern(pattern);

        List<FunctionInfo> functions =
            sapJcoFunctionDiscoveryService.searchFunctionsByPattern(pattern, groupName, language);

        SapFunctionSearchResponse searchResponse =
            SapFunctionSearchResponse.of(pattern).withGroupName(groupName).withLanguage(language)
                .withFunctions(functions);

        // 함수 검색 완료 로그
        StructuredLogger.logFunctionSearch(pattern, groupName, language, functions.size());

        return ResponseEntity.ok(ApiResponse.success(searchResponse));
    }

    /**
     * 특정 함수의 상세 인터페이스 정보 조회 API (RFC_GET_FUNCTION_INTERFACE 사용)
     *
     * @param functionName 조회할 함수명
     * @return 함수 인터페이스 정보
     */
    @GetMapping("/{functionName}/interface")
    public ResponseEntity<ApiResponse<Object>> getFunctionInterface(@PathVariable String functionName) {

        var interfaceInfo = sapJcoFunctionDiscoveryService.getFunctionInterface(functionName);

        SapParameterMap result = SapParameterMap.of().add("functionName", functionName).add("interface", interfaceInfo);

        // 함수 인터페이스 조회 완료 로그
        StructuredLogger.logFunctionInterface(functionName);

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
