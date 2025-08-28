package com.basis.template.svcsapjco.service;

import com.basis.template.svcsapjco.exception.SapConnectionException;
import com.basis.template.svcsapjco.util.StructuredLogger;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * SAP 연결 관리 전용 서비스 단일 책임: SAP 연결 상태 확인 및 관리만 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SapJcoConnectionManager {

    private final JCoDestination destination;

    /**
     * SAP 연결 상태 확인
     */
    public void checkConnection() {
        log.debug("SAP 연결 상태 확인 시작");
        try {
            destination.ping();
            log.debug("SAP 연결 ping 성공");
        } catch (JCoException e) {
            StructuredLogger.logException("SAP_CONNECTION_ERROR", e.getMessage(), e);
            throw new SapConnectionException("SAP 연결 실패: " + e.getMessage(), e);
        }
    }

    /**
     * Destination 반환
     */
    public JCoDestination getDestination() {
        return destination;
    }
}
