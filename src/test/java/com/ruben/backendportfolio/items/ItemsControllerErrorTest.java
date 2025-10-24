package com.ruben.backendportfolio.items;

import com.ruben.backendportfolio.errors.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemsController.class)
@Import(GlobalExceptionHandler.class)
class ItemsControllerErrorTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    ItemService service;

    @Test
    void get_unknownId_returns_404_not_found() throws Exception {
        when(service.get(999L)).thenThrow(new ItemNotFoundException(999L));

        mvc.perform(get("/items/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.message", containsString("Item 999 not found")))
                .andExpect(jsonPath("$.path", is("/items/999")));
    }

    @Test
    void delete_unknownId_returns_404_not_found() throws Exception {
        doThrow(new ItemNotFoundException(999L)).when(service).delete(999L);

        mvc.perform(delete("/items/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.message", containsString("Item 999 not found")))
                .andExpect(jsonPath("$.path", is("/items/999")));
    }

    @Test
    void create_invalidName_returns_400_validation_error() throws Exception {
        String body = """
                { "name": "" }
                """;

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("validation_error")))
                .andExpect(jsonPath("$.details", notNullValue()))
                .andExpect(jsonPath("$.path", is("/items")));
    }
}
