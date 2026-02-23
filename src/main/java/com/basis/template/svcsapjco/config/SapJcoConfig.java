package com.basis.template.svcsapjco.config;

import com.basis.template.svcsapjco.constant.MessageConstants;
import com.basis.template.svcsapjco.constant.SapConstants;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

/**
 * SAP JCo 설정 클래스 (test 프로필에서는 로드되지 않음)
 */
@Slf4j
@Configuration
@Profile("!test")
public class SapJcoConfig {

    @Value("${sap.jco.ashost}")
    private String ashost;

    @Value("${sap.jco.sysnr}")
    private String sysnr;

    @Value("${sap.jco.client}")
    private String client;

    @Value("${sap.jco.user}")
    private String user;

    @Value("${sap.jco.passwd}")
    private String passwd;

    @Value("${sap.jco.lang}")
    private String lang;

    @Value("${sap.jco.pool.capacity}")
    private String poolCapacity;

    @Value("${sap.jco.pool.peak-limit}")
    private String peakLimit;

    @Value("${sap.jco.trace.enabled}")
    private String trace;

    @Value("${sap.jco.timeout.connection}")
    private String lcheck;

    /**
     * SAP JCo Destination Bean 생성
     */
    @Bean
    public JCoDestination jcoDestination() {
        log.info("SAP JCo Destination 설정 시작: {}", ashost);

        // DestinationDataProvider 설정
        setupDestinationDataProvider();

        // Destination 생성 및 반환
        return createDestination();
    }

    /**
     * DestinationDataProvider 설정
     */
    private void setupDestinationDataProvider() {
        try {
            SapDestinationDataProvider provider = new SapDestinationDataProvider();
            Properties connectProperties = createConnectionProperties();
            provider.addDestinationProperties(SapConstants.DESTINATION_NAME, connectProperties);
            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(provider);
            log.debug("SAP DestinationDataProvider 등록 완료: {}", SapConstants.DESTINATION_NAME);
        } catch (Exception e) {
            log.error("SAP DestinationDataProvider 설정 실패: {}", e.getMessage(), e);
            throw new RuntimeException("SAP DestinationDataProvider 설정 실패", e);
        }
    }

    /**
     * 연결 속성 생성
     */
    private Properties createConnectionProperties() {
        Properties properties = new Properties();

        properties.setProperty(DestinationDataProvider.JCO_ASHOST, ashost);
        properties.setProperty(DestinationDataProvider.JCO_SYSNR, sysnr);
        properties.setProperty(DestinationDataProvider.JCO_CLIENT, client);
        properties.setProperty(DestinationDataProvider.JCO_USER, user);
        properties.setProperty(DestinationDataProvider.JCO_PASSWD, passwd);
        properties.setProperty(DestinationDataProvider.JCO_LANG, lang);
        properties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, poolCapacity);
        properties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, peakLimit);
        properties.setProperty(DestinationDataProvider.JCO_TRACE, trace);
        properties.setProperty(DestinationDataProvider.JCO_LCHECK, lcheck);

        log.debug(MessageConstants.LOG_SAP_CONNECTION_PROPERTIES_COMPLETE, ashost, sysnr, client, user, lang);

        return properties;
    }

    /**
     * Destination 생성
     */
    private JCoDestination createDestination() {
        try {
            JCoDestination destination = JCoDestinationManager.getDestination(SapConstants.DESTINATION_NAME);
            log.info("SAP Destination 생성 성공: {}", SapConstants.DESTINATION_NAME);
            return destination;
        } catch (JCoException e) {
            log.error("SAP Destination 생성 실패: {} - {}", SapConstants.DESTINATION_NAME, e.getMessage(), e);
            throw new RuntimeException("SAP Destination 생성 실패", e);
        }
    }
}
