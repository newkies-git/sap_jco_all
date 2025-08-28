package com.basis.template.svcsapjco;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = SvcSapjcoApplication.class)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=com.basis.template.svcsapjco.config.SapJcoConfig"
})
class SvcSapjcoApplicationTests {

    @Test
    void contextLoads() {
        // 기본 Spring Boot 컨텍스트 로드 테스트
    }

}
