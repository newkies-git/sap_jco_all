package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.dto.SapFunctionResponse;
import com.basis.template.svcsapjco.dto.SapTableData;
import com.basis.template.svcsapjco.util.JCoResultExtractor;
import com.sap.conn.jco.JCoFunction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SAP JCo 응답 생성 전용 서비스
 * 단일 책임: 추출기 결과를 SapFunctionResponse DTO로 조립
 */
@Service
public class SapJcoResponseBuilder {

    private final JCoResultExtractor resultExtractor;
    private final boolean parallelTableConversion;

    public SapJcoResponseBuilder(JCoResultExtractor resultExtractor,
                                 @Value("${sap.jco.response.parallel-table-conversion:false}") boolean parallelTableConversion) {
        this.resultExtractor = resultExtractor;
        this.parallelTableConversion = parallelTableConversion;
    }

    /**
     * 성공 응답 생성 (추출 실패 시 예외 전파)
     */
    public SapFunctionResponse createSuccessResponse(String functionName, JCoFunction function) {
        Map<String, Object> exportParams = resultExtractor.extractExportParameters(function);
        Map<String, List<Map<String, Object>>> tableParams = resultExtractor.extractTableParameters(function);
        Stream<Map.Entry<String, List<Map<String, Object>>>> entryStream = tableParams.entrySet().stream();
        if (parallelTableConversion) {
            entryStream = entryStream.parallel();
        }
        Map<String, SapTableData> tableResults = entryStream
                .collect(Collectors.toMap(Map.Entry::getKey, e -> SapTableData.from(e.getValue())));

        return SapFunctionResponse.of(functionName)
                .withExportParamsFromMap(exportParams)
                .withTableParams(tableResults);
    }
}
