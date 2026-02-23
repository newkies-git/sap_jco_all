package com.basis.template.svcsapjco.util;

import java.util.UUID;

/**
 * 요청 단위 컨텍스트 보관 (requestId, startTime).
 * ThreadLocal로 스레드별 값을 유지하며, 인터셉터에서 설정·정리한다.
 */
public final class RequestContext {

    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    private RequestContext() {
    }

    /**
     * 요청 ID 생성 후 설정하고, 시작 시간을 기록한다.
     *
     * @return 생성된 requestId (8자)
     */
    public static String generateRequestId() {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        REQUEST_ID.set(requestId);
        START_TIME.set(System.currentTimeMillis());
        return requestId;
    }

    public static String getCurrentRequestId() {
        return REQUEST_ID.get();
    }

    public static Long getStartTime() {
        return START_TIME.get();
    }

    /**
     * 시작 시간 기준 경과 시간(ms). 설정되지 않았으면 0.
     */
    public static long calculateExecutionTime() {
        Long start = START_TIME.get();
        return start != null ? System.currentTimeMillis() - start : 0L;
    }

    /**
     * 현재 스레드의 컨텍스트 제거 (요청 종료 시 호출)
     */
    public static void clear() {
        REQUEST_ID.remove();
        START_TIME.remove();
    }
}
