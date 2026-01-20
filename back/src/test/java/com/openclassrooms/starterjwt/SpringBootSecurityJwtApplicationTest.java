package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SpringBootSecurityJwtApplicationTest {

    @Test
    void main_shouldStartApplication() {
        System.setProperty("server.port", "0"); // port random
        assertThatCode(() -> SpringBootSecurityJwtApplication.main(new String[]{}))
                .doesNotThrowAnyException();
    }
}
