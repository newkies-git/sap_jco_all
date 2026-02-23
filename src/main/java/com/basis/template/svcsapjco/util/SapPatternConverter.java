package com.basis.template.svcsapjco.util;

import com.basis.template.svcsapjco.constant.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * SAP 함수 검색 패턴 변환 전담 유틸리티.
 * 단일 책임: 검색 패턴을 SAP ABAP 와일드카드 형식으로 변환
 */
@Slf4j
@Component
public class SapPatternConverter {

    /**
     * 패턴을 SAP ABAP 와일드카드 형식으로 변환 (null/빈 값은 "*" 반환)
     */
    public String convertPattern(String pattern) {
        if (pattern == null) {
            log.debug(MessageConstants.LOG_PATTERN_NULL);
            return "*";
        }
        String trimmed = pattern.trim();
        if (trimmed.isEmpty()) {
            log.debug(MessageConstants.LOG_PATTERN_EMPTY);
            return "*";
        }
        String converted = trimmed.replace("%", "*").replace("?", "_");
        log.debug(MessageConstants.LOG_PATTERN_CONVERSION, pattern, converted);
        return converted;
    }
}
