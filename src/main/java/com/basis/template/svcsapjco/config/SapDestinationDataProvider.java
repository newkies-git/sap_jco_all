package com.basis.template.svcsapjco.config;

import com.basis.template.svcsapjco.constant.SapConstants;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.util.Properties;

/**
 * SAP JCo Destination 프로퍼티 제공 전담.
 * 단일 책임: Destination 이름별 Properties 보관 및 반환
 */
public class SapDestinationDataProvider implements DestinationDataProvider {

    private final Properties destinationProperties = new Properties();

    @Override
    public Properties getDestinationProperties(String destinationName) {
        if (SapConstants.DESTINATION_NAME.equals(destinationName)) {
            return destinationProperties;
        }
        return null;
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
        // 필요 시 구현
    }

    @Override
    public boolean supportsEvents() {
        return false;
    }

    /**
     * 지정한 Destination 이름에 프로퍼티를 등록한다.
     */
    public void addDestinationProperties(String destinationName, Properties properties) {
        if (SapConstants.DESTINATION_NAME.equals(destinationName)) {
            destinationProperties.putAll(properties);
        }
    }
}
