package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.constant.MessageConstants;
import com.basis.template.svcsapjco.constant.SapConstants;
import com.basis.template.svcsapjco.dto.FunctionInterfaceInfo;
import com.sap.conn.jco.JCoTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * RFC_GET_FUNCTION_INTERFACE 결과 테이블(PARAMS)을 FunctionInterfaceInfo로 매핑
 */
@Slf4j
@Component
public class ParamsTableMapper {

    public FunctionInterfaceInfo mapToInterfaceInfo(String functionName, JCoTable params) {
        FunctionInterfaceInfo interfaceInfo = new FunctionInterfaceInfo();
        interfaceInfo.setFunctionName(functionName);
        if (params == null || params.getNumRows() == 0) {
            return interfaceInfo;
        }
        log.debug(MessageConstants.LOG_PARAMS_COUNT, params.getNumRows());
        params.firstRow();
        do {
            String paramName = params.getString(SapConstants.FIELD_PARAMETER);
            String paramClass = params.getString(SapConstants.FIELD_PARAMCLASS);
            String paramText = params.getString(SapConstants.FIELD_PARAMTEXT);
            String tabName = params.getString(SapConstants.FIELD_TABNAME);

            log.debug(MessageConstants.LOG_PARAM_DETAILS,
                    paramName, paramClass, tabName,
                    params.getString(SapConstants.FIELD_FIELDNAME),
                    params.getString(SapConstants.FIELD_EXID),
                    params.getInt(SapConstants.FIELD_POSITION),
                    params.getInt(SapConstants.FIELD_OFFSET),
                    params.getInt(SapConstants.FIELD_INTLENGTH),
                    params.getInt(SapConstants.FIELD_DECIMALS),
                    params.getString(SapConstants.FIELD_DEFAULT),
                    paramText,
                    params.getString(SapConstants.FIELD_OPTIONAL));

            switch (paramClass) {
                case SapConstants.PARAM_CLASS_IMPORT -> interfaceInfo.addImportParameter(paramName, tabName, paramText);
                case SapConstants.PARAM_CLASS_EXPORT -> interfaceInfo.addExportParameter(paramName, tabName, paramText);
                case SapConstants.PARAM_CLASS_TABLE -> interfaceInfo.addTableParameter(paramName, tabName, paramText);
                case SapConstants.PARAM_CLASS_CHANGING -> interfaceInfo.addChangeParameter(paramName, tabName, paramText);
                default -> log.debug(MessageConstants.LOG_UNKNOWN_PARAM_CLASS, paramClass, paramName);
            }
        } while (params.nextRow());
        return interfaceInfo;
    }
}
