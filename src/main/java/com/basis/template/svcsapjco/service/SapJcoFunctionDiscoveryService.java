package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.constant.ErrorCodeConstants;
import com.basis.template.svcsapjco.constant.MessageConstants;
import com.basis.template.svcsapjco.constant.SapConstants;
import com.basis.template.svcsapjco.dto.FunctionInfo;
import com.basis.template.svcsapjco.dto.FunctionInterfaceInfo;
import com.basis.template.svcsapjco.exception.SapFunctionDiscoveryException;
import com.basis.template.svcsapjco.util.SapPatternConverter;
import com.basis.template.svcsapjco.util.StructuredLogger;
import com.sap.conn.jco.JCoTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SAP 함수 검색 및 메타데이터 조회 오케스트레이션 서비스.
 * 패턴 변환/ RFC 실행/ 결과 매핑은 각 전담 컴포넌트에 위임.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SapJcoFunctionDiscoveryService {

    private final SapPatternConverter patternConverter;
    private final RfcFunctionSearchExecutor searchExecutor;
    private final FunctionSearchResultMapper searchResultMapper;
    private final RfcFunctionInterfaceExecutor interfaceExecutor;
    private final ParamsTableMapper paramsTableMapper;

    public List<FunctionInfo> searchFunctionsByPattern(String pattern, String groupName, String language) {
        try {
            log.debug("패턴 '{}', 그룹 '{}', 언어 '{}'으로 함수 검색 시작", pattern, groupName, language);
            String converted = patternConverter.convertPattern(pattern);
            String group = groupName != null ? groupName : SapConstants.DEFAULT_GROUP_NAME;
            String lang = language != null ? language : SapConstants.DEFAULT_LANGUAGE;

            JCoTable functionsTable = searchExecutor.executeSearch(converted, group, lang);
            List<FunctionInfo> functions = searchResultMapper.mapFromFunctionsTable(functionsTable);

            log.debug("패턴 '{}'으로 {} 개의 함수를 검색했습니다", pattern, functions.size());
            return functions;
        } catch (Exception e) {
            StructuredLogger.logException(ErrorCodeConstants.SAP_FUNCTION_DISCOVERY_ERROR, e.getMessage(), e);
            throw new SapFunctionDiscoveryException(pattern,
                    MessageConstants.ERROR_FUNCTION_SEARCH_FAILED + e.getMessage(), e);
        }
    }

    public FunctionInterfaceInfo getFunctionInterface(String functionName) {
        try {
            log.debug("함수 '{}'의 인터페이스 정보 조회 시작", functionName);
            JCoTable params = interfaceExecutor.executeGetInterface(functionName);
            FunctionInterfaceInfo interfaceInfo = paramsTableMapper.mapToInterfaceInfo(functionName, params);
            log.debug("함수 '{}'의 인터페이스 정보 조회 완료", functionName);
            return interfaceInfo;
        } catch (Exception e) {
            StructuredLogger.logException(ErrorCodeConstants.SAP_FUNCTION_DISCOVERY_ERROR, e.getMessage(), e);
            throw new SapFunctionDiscoveryException(functionName,
                    MessageConstants.ERROR_FUNCTION_INTERFACE_FAILED + e.getMessage(), e);
        }
    }
}
