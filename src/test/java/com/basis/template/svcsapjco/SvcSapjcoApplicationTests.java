package com.basis.template.svcsapjco;

import com.sap.conn.jco.JCoDestination;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = SvcSapjcoApplication.class)
@ActiveProfiles("test")
class SvcSapjcoApplicationTests {

    @MockBean
    private JCoDestination jcoDestination;

    @Test
    void contextLoads() {
        // 기본 Spring Boot 컨텍스트 로드 테스트 (test 프로필에서 SapJcoConfig 미로드)
    }
}
