package com.basis.template.svcsapjco.constant;

/**
 * SAP 관련 상수 클래스
 */
public final class SapConstants {
    
    private SapConstants() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }
    
    // SAP 함수명 상수
    public static final String RFC_FUNCTION_SEARCH = "RFC_FUNCTION_SEARCH";
    public static final String RFC_GET_FUNCTION_INTERFACE = "RFC_GET_FUNCTION_INTERFACE";
    
    // SAP 파라미터명 상수
    public static final String PARAM_FUNCNAME = "FUNCNAME";
    public static final String PARAM_GROUPNAME = "GROUPNAME";
    public static final String PARAM_LANGUAGE = "LANGUAGE";
    
    // SAP 테이블명 상수
    public static final String TABLE_FUNCTIONS = "FUNCTIONS";
    public static final String TABLE_PARAMS = "PARAMS";
    
    // SAP 필드명 상수
    public static final String FIELD_FUNCNAME = "FUNCNAME";
    public static final String FIELD_GROUPNAME = "GROUPNAME";
    public static final String FIELD_APPL = "APPL";
    public static final String FIELD_HOST = "HOST";
    public static final String FIELD_STEXT = "STEXT";
    
    // SAP 파라미터 클래스 상수
    public static final String PARAM_CLASS_IMPORT = "I";
    public static final String PARAM_CLASS_EXPORT = "E";
    public static final String PARAM_CLASS_STRUCTURE = "S";
    public static final String PARAM_CLASS_TABLE = "T";
    public static final String PARAM_CLASS_CHANGING = "C";
    
    // SAP 파라미터 상세 필드 상수
    public static final String FIELD_PARAMCLASS = "PARAMCLASS";
    public static final String FIELD_PARAMETER = "PARAMETER";
    public static final String FIELD_TABNAME = "TABNAME";
    public static final String FIELD_FIELDNAME = "FIELDNAME";
    public static final String FIELD_EXID = "EXID";
    public static final String FIELD_POSITION = "POSITION";
    public static final String FIELD_OFFSET = "OFFSET";
    public static final String FIELD_INTLENGTH = "INTLENGTH";
    public static final String FIELD_DECIMALS = "DECIMALS";
    public static final String FIELD_DEFAULT = "DEFAULT";
    public static final String FIELD_PARAMTEXT = "PARAMTEXT";
    public static final String FIELD_OPTIONAL = "OPTIONAL";
    
    // SAP 기본값 상수
    public static final String DEFAULT_GROUP_NAME = "";
    public static final String DEFAULT_LANGUAGE = "3"; // 한글
    
    // SAP 데이터 소스 상수
    public static final String DATA_SOURCE_SAP_SYSTEM = "SAP_SYSTEM";
    
    // SAP 연결 설정 상수
    public static final String DESTINATION_NAME = "SAP_DESTINATION";
}
