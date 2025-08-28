package com.basis.template.svcsapjco.interceptor;

import com.basis.template.svcsapjco.util.StructuredLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * 요청/응답 로깅을 위한 인터셉터
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        // 요청 ID 생성
        String requestId = StructuredLogger.generateRequestId();

        // 요청 파라미터 수집
        Map<String, Object> requestParams = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values.length == 1) {
                requestParams.put(key, values[0]);
            } else {
                requestParams.put(key, values);
            }
        });

        // API 요청 로그
        StructuredLogger.logApiRequest(request.getMethod(), request.getRequestURI(), requestParams);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        try {
            // 실행 시간 계산
            long executionTime = StructuredLogger.calculateExecutionTime();

            // API 응답 로그
            StructuredLogger.logApiResponse(request.getMethod(), request.getRequestURI(), response.getStatus(),
                executionTime);
        } finally {
            // ThreadLocal 정리
            StructuredLogger.clear();
        }
    }
}
