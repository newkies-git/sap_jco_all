package com.basis.template.svcsapjco.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 타입 안전한 SAP 테이블 데이터 제네릭을 활용하여 테이블 행 데이터의 타입 안전성을 보장
 */
@Data
@NoArgsConstructor
public class SapTableData {

    private final List<SapParameterMap> rows = new ArrayList<>();

    /**
     * 행 추가
     */
    public SapTableData addRow(SapParameterMap row) {
        rows.add(row);
        return this;
    }

    /**
     * 행 추가 (Map으로부터)
     */
    public SapTableData addRow(Map<String, Object> rowMap) {
        rows.add(SapParameterMap.from(rowMap));
        return this;
    }

    /**
     * 특정 인덱스의 행 가져오기
     */
    public SapParameterMap getRow(int index) {
        if (index >= 0 && index < rows.size()) {
            return rows.get(index);
        }
        return null;
    }

    /**
     * 모든 행 가져오기
     */
    public List<SapParameterMap> getRows() {
        return new ArrayList<>(rows);
    }

    /**
     * 행 개수
     */
    public int size() {
        return rows.size();
    }

    /**
     * 빈 테이블인지 확인
     */
    public boolean isEmpty() {
        return rows.isEmpty();
    }

    /**
     * 모든 행 제거
     */
    public void clear() {
        rows.clear();
    }

    /**
     * 특정 컬럼의 모든 값 가져오기
     */
    public <T> List<T> getColumnValues(String columnName, Class<T> type) {
        List<T> values = new ArrayList<>();
        for (SapParameterMap row : rows) {
            T value = row.get(columnName, type);
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }

    /**
     * 특정 조건에 맞는 행들 필터링
     */
    public SapTableData filter(String columnName, Object value) {
        SapTableData filtered = new SapTableData();
        for (SapParameterMap row : rows) {
            if (value.equals(row.toMap().get(columnName))) {
                filtered.addRow(row);
            }
        }
        return filtered;
    }

    /**
     * List<Map<String, Object>>로 변환 (호환성을 위해)
     */
    public List<Map<String, Object>> toList() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (SapParameterMap row : rows) {
            result.add(row.toMap());
        }
        return result;
    }

    /**
     * 정적 팩토리 메서드
     */
    public static SapTableData of() {
        return new SapTableData();
    }

    /**
     * List<Map<String, Object>>로부터 생성
     */
    public static SapTableData from(List<Map<String, Object>> list) {
        SapTableData tableData = new SapTableData();
        if (list != null) {
            for (Map<String, Object> row : list) {
                tableData.addRow(row);
            }
        }
        return tableData;
    }
}
