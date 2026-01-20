package com.openclassrooms.starterjwt.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebSecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void apiShouldBeSecured() throws Exception {
        SecurityContextHolder.clearContext();
        mockMvc.perform(get("/api/session"))
            .andExpect(status().isUnauthorized());
    }
}
