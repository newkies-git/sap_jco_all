package com.basis.template.svcsapjco.controller;

import com.basis.template.svcsapjco.dto.ApiResponse;
import com.basis.template.svcsapjco.dto.SapFunctionRequest;
import com.basis.template.svcsapjco.dto.SapFunctionResponse;
import com.basis.template.svcsapjco.service.SapJcoResponseBuilder;
import com.basis.template.svcsapjco.service.SapJcoService;
import com.basis.template.svcsapjco.service.SapJcoValidationService;
import com.sap.conn.jco.JCoFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/sap/execute")
@RequiredArgsConstructor
public class SapJcoExecutionController {

    private final SapJcoService sapJcoService;
    private final SapJcoValidationService validationService;
    private final SapJcoResponseBuilder responseBuilder;

    /**
     * RFC/BAPI 함수를 호출하는 공통 API
     *
     * @param request 요청 데이터
     * @return 실행 결과
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SapFunctionResponse>> executeFunction(@RequestBody SapFunctionRequest request) {
        validationService.validateRequest(request);

        // SAP 함수 실행
        JCoFunction function =
            sapJcoService.executeFunction(request.getFunctionName(), request.getImportParams(), request.getTables());

        // 결과 생성 및 반환
        SapFunctionResponse response = responseBuilder.createSuccessResponse(request.getFunctionName(), function);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Import 파라미터만으로 RFC/BAPI 함수를 호출하는 API
     *
     * @param functionName 함수명
     * @param importParams Import 파라미터
     * @return 실행 결과
     */
    @PostMapping("/{functionName}")
    public ResponseEntity<ApiResponse<SapFunctionResponse>> executeFunctionWithImport(@PathVariable String functionName,
        @RequestBody(required = false) Map<String, Object> importParams) {

        SapFunctionRequest request = SapFunctionRequest.of(functionName, importParams);

        return executeFunction(request);
    }

    /**
     * 파라미터 없이 RFC/BAPI 함수를 호출하는 API
     *
     * @param functionName 함수명
     * @return 실행 결과
     */
    @GetMapping("/{functionName}")
    public ResponseEntity<ApiResponse<SapFunctionResponse>> executeFunctionWithoutParams(
        @PathVariable String functionName) {
        SapFunctionRequest request = SapFunctionRequest.of(functionName);

        return executeFunction(request);
    }
}
