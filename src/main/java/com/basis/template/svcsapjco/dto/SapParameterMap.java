package com.basis.template.svcsapjco.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 타입 안전한 SAP 파라미터 맵 제네릭을 활용하여 타입 안전성을 보장
 * JSON 직렬화 시 flat 형태({ "KEY": "value", ... })로 읽고 씁니다.
 */
@Data
@NoArgsConstructor
public class SapParameterMap {

    private final Map<String, Object> parameters = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @JsonAnySetter
    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }

    /**
     * 파라미터 추가
     */
    public <T> SapParameterMap add(String key, T value) {
        parameters.put(key, value);
        return this;
    }

    /**
     * 파라미터 가져오기 (타입 캐스팅)
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = parameters.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * 파라미터 가져오기 (기본값과 함께)
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type, T defaultValue) {
        T value = get(key, type);
        return value != null ? value : defaultValue;
    }

    /**
     * String 파라미터 가져오기
     */
    public String getString(String key) {
        return get(key, String.class);
    }

    /**
     * String 파라미터 가져오기 (기본값과 함께)
     */
    public String getString(String key, String defaultValue) {
        return get(key, String.class, defaultValue);
    }

    /**
     * Integer 파라미터 가져오기
     */
    public Integer getInteger(String key) {
        return get(key, Integer.class);
    }

    /**
     * Integer 파라미터 가져오기 (기본값과 함께)
     */
    public Integer getInteger(String key, Integer defaultValue) {
        return get(key, Integer.class, defaultValue);
    }

    /**
     * Long 파라미터 가져오기
     */
    public Long getLong(String key) {
        return get(key, Long.class);
    }

    /**
     * Long 파라미터 가져오기 (기본값과 함께)
     */
    public Long getLong(String key, Long defaultValue) {
        return get(key, Long.class, defaultValue);
    }

    /**
     * Double 파라미터 가져오기
     */
    public Double getDouble(String key) {
        return get(key, Double.class);
    }

    /**
     * Double 파라미터 가져오기 (기본값과 함께)
     */
    public Double getDouble(String key, Double defaultValue) {
        return get(key, Double.class, defaultValue);
    }

    /**
     * Boolean 파라미터 가져오기
     */
    public Boolean getBoolean(String key) {
        return get(key, Boolean.class);
    }

    /**
     * Boolean 파라미터 가져오기 (기본값과 함께)
     */
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return get(key, Boolean.class, defaultValue);
    }

    /**
     * 파라미터 존재 여부 확인
     */
    public boolean containsKey(String key) {
        return parameters.containsKey(key);
    }

    /**
     * 파라미터 개수
     */
    public int size() {
        return parameters.size();
    }

    /**
     * 빈 맵인지 확인
     */
    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    /**
     * 모든 파라미터 제거
     */
    public void clear() {
        parameters.clear();
    }

    /**
     * 내부 Map 반환 (호환성을 위해)
     */
    public Map<String, Object> toMap() {
        return new HashMap<>(parameters);
    }

    /**
     * 정적 팩토리 메서드
     */
    public static SapParameterMap of() {
        return new SapParameterMap();
    }

    /**
     * 정적 팩토리 메서드 (키-값 쌍으로 생성)
     */
    public static SapParameterMap of(String key, Object value) {
        return new SapParameterMap().add(key, value);
    }

    /**
     * Map으로부터 생성
     */
    public static SapParameterMap from(Map<String, Object> map) {
        SapParameterMap parameterMap = new SapParameterMap();
        if (map != null) {
            parameterMap.parameters.putAll(map);
        }
        return parameterMap;
    }
}
