package com.basis.template.svcsapjco.util;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoStructure;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * SAP JCo 데이터 처리 유틸리티 클래스
 * 순환 참조 방지를 위해 별도 클래스로 분리
 */
@Slf4j
public class SapJcoDataUtil {

    // 상수 정의
    private static final String ERROR_PREFIX = "ERROR: ";

    /**
     * JCoField에서 안전하게 값을 추출하는 메서드
     *
     * @param field JCoField
     * @return 안전한 값
     */
    public static Object getSafeValue(JCoField field) {
        try {
            if (field.isStructure()) {
                return convertStructureToMap(field.getStructure());
            } else if (field.isTable()) {
                return convertTableToList(field.getTable());
            } else {
                return field.getValue();
            }
        } catch (Exception e) {
            log.warn("필드 {} 안전한 값 추출 실패: {}", field.getName(), e.getMessage());
            return ERROR_PREFIX + e.getMessage();
        }
    }

    /**
     * JCoStructure를 Map으로 변환
     */
    public static Map<String, Object> convertStructureToMap(JCoStructure structure) {
        Map<String, Object> structureMap = new HashMap<>();
        for (JCoField structField : structure) {
            try {
                structureMap.put(structField.getName(), structField.getValue());
            } catch (Exception e) {
                structureMap.put(structField.getName(), ERROR_PREFIX + e.getMessage());
            }
        }
        return structureMap;
    }

    /**
     * JCoTable을 Map 리스트로 변환
     */
    public static java.util.List<Map<String, Object>> convertTableToList(com.sap.conn.jco.JCoTable table) {
        java.util.List<Map<String, Object>> result = new java.util.ArrayList<>();

        if (table != null && table.getNumRows() > 0) {
            table.firstRow();
            do {
                Map<String, Object> row = new HashMap<>();
                for (JCoField field : table) {
                    row.put(field.getName(), field.getValue());
                }
                result.add(row);
            } while (table.nextRow());
        }

        return result;
    }
}
