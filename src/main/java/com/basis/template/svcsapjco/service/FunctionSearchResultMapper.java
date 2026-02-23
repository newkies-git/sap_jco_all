package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.constant.SapConstants;
import com.basis.template.svcsapjco.dto.FunctionInfo;
import com.sap.conn.jco.JCoTable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * RFC_FUNCTION_SEARCH 결과 테이블(FUNCTIONS)을 List&lt;FunctionInfo&gt;로 매핑
 */
@Component
public class FunctionSearchResultMapper {

    /**
     * FUNCTIONS JCoTable을 List&lt;FunctionInfo&gt;로 변환
     */
    public List<FunctionInfo> mapFromFunctionsTable(JCoTable functionsTable) {
        List<FunctionInfo> list = new ArrayList<>();
        if (functionsTable == null || functionsTable.getNumRows() == 0) {
            return list;
        }
        functionsTable.firstRow();
        do {
            FunctionInfo info = new FunctionInfo();
            info.setFunctionName(functionsTable.getString(SapConstants.FIELD_FUNCNAME));
            info.setGroupName(functionsTable.getString(SapConstants.FIELD_GROUPNAME));
            info.setApplication(functionsTable.getString(SapConstants.FIELD_APPL));
            info.setHost(functionsTable.getString(SapConstants.FIELD_HOST));
            info.setDescription(functionsTable.getString(SapConstants.FIELD_STEXT));
            list.add(info);
        } while (functionsTable.nextRow());
        return list;
    }
}
