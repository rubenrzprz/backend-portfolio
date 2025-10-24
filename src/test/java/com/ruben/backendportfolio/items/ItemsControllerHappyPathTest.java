package com.ruben.backendportfolio.items;

import com.ruben.backendportfolio.errors.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ItemsController.class)
@Import(GlobalExceptionHandler.class)
class ItemsControllerHappyPathTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ItemService service;

    @Test
    void create_returns201_and_location_and_body() throws Exception {
        when(service.create(any(ItemCreateRequest.class))).thenReturn(new Item(1L, "alpha"));
        String body = """
                { "name": "alpha" }
                """;
        mvc.perform(post("/items").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated()).andExpect(header().string("Location", endsWith("/items/1"))).andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("alpha")));
    }

    @Test
    void list_returns200_and_items() throws Exception {
        when(service.list()).thenReturn(List.of(new Item(1L, "alpha"), new Item(2L, "beta")));

        mvc.perform(get("/items")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].name", is("alpha"))).andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].name", is("beta")));
    }

    @Test
    void get_returns200_with_item() throws Exception {
        when(service.replace(eq(1L), any(ItemUpdateRequest.class))).thenReturn(new Item(1L, "alpha-2"));

        String body = """
                { "name": "alpha-2" }
                """;
        mvc.perform(put("/items/1").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("alpha-2")));
    }

    @Test
    void patch_returns200_with_updated_item() throws Exception {
        when(service.patchName(eq(1L), any(ItemUpdateRequest.class)))
                .thenReturn(new Item(1L, "alpha-3"));

        String body = """
      { "name": "alpha-3" }
      """;

        mvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("alpha-3")));
    }

    @Test
    void delete_returns204_noContent() throws Exception {
        doNothing().when(service).delete(1L);

        mvc.perform(delete("/items/1"))
                .andExpect(status().isNoContent());
    }
}
