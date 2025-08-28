package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.dto.SapFunctionRequest;
import com.basis.template.svcsapjco.exception.ValidationException;
import com.basis.template.svcsapjco.util.StructuredLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * SAP JCo 검증 전용 서비스
 * 단일 책임: 입력값 검증만 담당
 */
@Slf4j
@Service
public class SapJcoValidationService {

    /**
     * 함수명 검증
     */
    public void validateFunctionName(String functionName) {
        if (functionName == null || functionName.trim().isEmpty()) {
            throw new ValidationException("functionName", "함수명이 비어있습니다.");
        }
    }

    /**
     * 요청 데이터 검증
     */
    public void validateRequest(SapFunctionRequest request) {
        if (request == null) {
            StructuredLogger.logValidationError("request", "요청 데이터가 null입니다.");
            throw new ValidationException("request", "요청 데이터가 null입니다.");
        }
        if (request.getFunctionName() == null || request.getFunctionName().trim().isEmpty()) {
            StructuredLogger.logValidationError("functionName", "함수명이 비어있습니다.");
            throw new ValidationException("functionName", "함수명이 비어있습니다.");
        }
    }

    /**
     * 검색 패턴 검증
     */
    public void validateSearchPattern(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new ValidationException("pattern", "검색 패턴이 비어있습니다.");
        }
        if (pattern.trim().length() < 2) {
            StructuredLogger.logValidationError("pattern", "검색 패턴은 2자리 이상이어야 합니다.");
            throw new ValidationException("pattern", "검색 패턴은 2자리 이상이어야 합니다.");
        }
    }
}
