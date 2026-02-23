package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.constant.SapConstants;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * RFC_FUNCTION_SEARCH 호출 전담: 함수 생성, 파라미터 설정, 실행 후 FUNCTIONS 테이블 반환
 */
@Component
@RequiredArgsConstructor
public class RfcFunctionSearchExecutor {

    private final SapJcoConnectionManager connectionManager;

    /**
     * 패턴/그룹/언어로 검색 실행 후 FUNCTIONS 테이블 반환
     */
    public JCoTable executeSearch(String convertedPattern, String groupName, String language) throws JCoException {
        JCoDestination dest = connectionManager.getDestination();
        JCoFunction function = dest.getRepository()
                .getFunctionTemplate(SapConstants.RFC_FUNCTION_SEARCH)
                .getFunction();
        function.getImportParameterList().setValue(SapConstants.PARAM_FUNCNAME, convertedPattern);
        function.getImportParameterList().setValue(SapConstants.PARAM_GROUPNAME, groupName);
        function.getImportParameterList().setValue(SapConstants.PARAM_LANGUAGE, language);
        function.execute(dest);
        return function.getTableParameterList().getTable(SapConstants.TABLE_FUNCTIONS);
    }
}
