package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.constant.SapConstants;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * RFC_GET_FUNCTION_INTERFACE 호출 전담: 함수 생성, 파라미터 설정, 실행 후 PARAMS 테이블 반환
 */
@Component
@RequiredArgsConstructor
public class RfcFunctionInterfaceExecutor {

    private final SapJcoConnectionManager connectionManager;

    public JCoTable executeGetInterface(String functionName) throws JCoException {
        JCoDestination dest = connectionManager.getDestination();
        JCoFunction function = dest.getRepository()
                .getFunctionTemplate(SapConstants.RFC_GET_FUNCTION_INTERFACE)
                .getFunction();
        function.getImportParameterList().setValue(SapConstants.PARAM_FUNCNAME, functionName);
        function.execute(dest);
        return function.getTableParameterList().getTable(SapConstants.TABLE_PARAMS);
    }
}
