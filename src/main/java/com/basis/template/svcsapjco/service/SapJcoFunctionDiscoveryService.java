package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.constant.ErrorCodeConstants;
import com.basis.template.svcsapjco.constant.MessageConstants;
import com.basis.template.svcsapjco.constant.SapConstants;
import com.basis.template.svcsapjco.dto.FunctionInfo;
import com.basis.template.svcsapjco.dto.FunctionInterfaceInfo;
import com.basis.template.svcsapjco.exception.SapFunctionDiscoveryException;
import com.basis.template.svcsapjco.util.StructuredLogger;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * SAP 함수 검색 및 메타데이터 조회 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SapJcoFunctionDiscoveryService {

    private final SapJcoConnectionManager connectionManager;

    /**
     * 패턴으로 함수 검색
     */
    public List<FunctionInfo> searchFunctionsByPattern(String pattern, String groupName, String language) {
        try {
            log.debug("패턴 '{}', 그룹 '{}', 언어 '{}'으로 함수 검색 시작", pattern, groupName, language);

            // 패턴 변환
            String convertedPattern = convertPattern(pattern);

            // 기본값 설정
            String defaultGroupName = groupName != null ? groupName : SapConstants.DEFAULT_GROUP_NAME;
            String defaultLanguage = language != null ? language : SapConstants.DEFAULT_LANGUAGE;

            // SAP 함수 호출
            JCoFunction function =
                connectionManager.getDestination().getRepository().getFunctionTemplate(SapConstants.RFC_FUNCTION_SEARCH)
                    .getFunction();

            // Import 파라미터 설정
            function.getImportParameterList().setValue(SapConstants.PARAM_FUNCNAME, convertedPattern);
            function.getImportParameterList().setValue(SapConstants.PARAM_GROUPNAME, defaultGroupName);
            function.getImportParameterList().setValue(SapConstants.PARAM_LANGUAGE, defaultLanguage);

            // 함수 실행
            function.execute(connectionManager.getDestination());

            // 결과 처리
            List<FunctionInfo> functions = new ArrayList<>();
            JCoTable functionsTable = function.getTableParameterList().getTable(SapConstants.TABLE_FUNCTIONS);

            if (functionsTable != null && functionsTable.getNumRows() > 0) {
                functionsTable.firstRow();
                do {
                    FunctionInfo functionInfo = new FunctionInfo();
                    functionInfo.setFunctionName(functionsTable.getString(SapConstants.FIELD_FUNCNAME));
                    functionInfo.setGroupName(functionsTable.getString(SapConstants.FIELD_GROUPNAME));
                    functionInfo.setApplication(functionsTable.getString(SapConstants.FIELD_APPL));
                    functionInfo.setHost(functionsTable.getString(SapConstants.FIELD_HOST));
                    functionInfo.setDescription(functionsTable.getString(SapConstants.FIELD_STEXT));
                    functions.add(functionInfo);
                } while (functionsTable.nextRow());
            }

            log.debug("패턴 '{}'으로 {} 개의 함수를 검색했습니다", pattern, functions.size());
            return functions;

        } catch (Exception e) {
            StructuredLogger.logException(ErrorCodeConstants.SAP_FUNCTION_DISCOVERY_ERROR, e.getMessage(), e);
            throw new SapFunctionDiscoveryException(pattern,
                MessageConstants.ERROR_FUNCTION_SEARCH_FAILED + e.getMessage(), e);
        }
    }

    /**
     * 함수 인터페이스 정보 조회
     */
    public FunctionInterfaceInfo getFunctionInterface(String functionName) {
        try {
            FunctionInterfaceInfo interfaceInfo = new FunctionInterfaceInfo();
            interfaceInfo.setFunctionName(functionName);

            log.debug("함수 '{}'의 인터페이스 정보 조회 시작", functionName);

            // SAP 함수 호출
            JCoFunction function = connectionManager.getDestination().getRepository()
                .getFunctionTemplate(SapConstants.RFC_GET_FUNCTION_INTERFACE).getFunction();

            // Import 파라미터 설정
            function.getImportParameterList().setValue(SapConstants.PARAM_FUNCNAME, functionName);

            // 함수 실행
            function.execute(connectionManager.getDestination());

            // 결과 처리
            JCoTable params = function.getTableParameterList().getTable(SapConstants.TABLE_PARAMS);

            if (params != null && params.getNumRows() > 0) {
                log.debug(MessageConstants.LOG_PARAMS_COUNT, params.getNumRows());

                params.firstRow();
                do {
                    String paramName = params.getString(SapConstants.FIELD_PARAMETER);
                    String paramClass = params.getString(SapConstants.FIELD_PARAMCLASS);
                    String paramText = params.getString(SapConstants.FIELD_PARAMTEXT);
                    String tabName = params.getString(SapConstants.FIELD_TABNAME);
                    String fieldName = params.getString(SapConstants.FIELD_FIELDNAME);
                    String exid = params.getString(SapConstants.FIELD_EXID);
                    int position = params.getInt(SapConstants.FIELD_POSITION);
                    int offset = params.getInt(SapConstants.FIELD_OFFSET);
                    int intLength = params.getInt(SapConstants.FIELD_INTLENGTH);
                    int decimals = params.getInt(SapConstants.FIELD_DECIMALS);
                    String defaultValue = params.getString(SapConstants.FIELD_DEFAULT);
                    String optional = params.getString(SapConstants.FIELD_OPTIONAL);

                    log.debug(MessageConstants.LOG_PARAM_DETAILS, paramName, paramClass, tabName, fieldName, exid,
                        position, offset, intLength, decimals, defaultValue, paramText, optional);

                    // 파라미터 클래스에 따라 분류
                    switch (paramClass) {
                        case SapConstants.PARAM_CLASS_IMPORT:
                            interfaceInfo.addImportParameter(paramName, tabName, paramText);
                            break;
                        case SapConstants.PARAM_CLASS_EXPORT:
                            interfaceInfo.addExportParameter(paramName, tabName, paramText);
                            break;
                        case SapConstants.PARAM_CLASS_TABLE:
                            interfaceInfo.addTableParameter(paramName, tabName, paramText);
                            break;
                        case SapConstants.PARAM_CLASS_CHANGING:
                            interfaceInfo.addChangeParameter(paramName, tabName, paramText);
                            break;
                        default:
                            log.debug(MessageConstants.LOG_UNKNOWN_PARAM_CLASS, paramClass, paramName);
                            break;
                    }
                } while (params.nextRow());
            }

            log.debug("함수 '{}'의 인터페이스 정보 조회 완료", functionName);
            return interfaceInfo;

        } catch (Exception e) {
            StructuredLogger.logException(ErrorCodeConstants.SAP_FUNCTION_DISCOVERY_ERROR, e.getMessage(), e);
            throw new SapFunctionDiscoveryException(functionName,
                MessageConstants.ERROR_FUNCTION_INTERFACE_FAILED + e.getMessage(), e);
        }
    }

    /**
     * 패턴을 SAP ABAP 와일드카드 형식으로 변환
     */
    private String convertPattern(String pattern) {
        if (pattern == null) {
            log.debug(MessageConstants.LOG_PATTERN_NULL);
            return "*";
        }

        String trimmedPattern = pattern.trim();
        if (trimmedPattern.isEmpty()) {
            log.debug(MessageConstants.LOG_PATTERN_EMPTY);
            return "*";
        }

        // Java 와일드카드를 SAP ABAP 와일드카드로 변환
        String convertedPattern = trimmedPattern.replace("%", "*").replace("?", "_");
        log.debug(MessageConstants.LOG_PATTERN_CONVERSION, pattern, convertedPattern);

        return convertedPattern;
    }
}
