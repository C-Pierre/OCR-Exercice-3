package com.openclassrooms.starterjwt.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "oc.app.frontUrl=http://localhost:3000")
class AppConfigTest {

    @Autowired
    private WebMvcConfigurer corsConfigurer;

    @Autowired
    private AppConfig appConfig;

    @Test
    void webMvcConfigurerBean_shouldBeCreated() {
        assertThat(corsConfigurer).isNotNull();
    }

    @Test
    void propertySourcesPlaceholderConfigurer_shouldBeCreated() {
        assertThat(AppConfig.propertySourcesPlaceholderConfigurer()).isNotNull();
    }
}
