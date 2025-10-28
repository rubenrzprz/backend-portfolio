package com.ruben.backendportfolio.health;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void health_returns_ok() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.service", is("backend-portfolio")));
    }
}
