package com.bootcampjava.startwars.controller;

import com.bootcampjava.startwars.model.Jedi;
import com.bootcampjava.startwars.service.JediService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.util.Optional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JediControllerTest {

    @MockBean
    private JediService jediService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("GET /jedi/1 - SUCCESS")
    public void testGetJediByIdWithSuccess() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "HanSolo", 10, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1);

        // execucao
        mockMvc.perform(get("/jedi/{id}", 1))

                // asserts
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1"))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("HanSolo")))
                .andExpect(jsonPath("$.strength", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("GET /jedi/1 - Not Found")
    public void testGetJediByIdNotFound() throws Exception {

        Mockito.doReturn(Optional.empty()).when(jediService).findById(1);

        mockMvc.perform(get("/jedi/{1}", 1))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("GET /all - SUCCESS")
    public void testGetAllJedi() throws Exception {
        mockMvc.perform(get("/jedis"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    // TODO: Teste do POST com sucesso (criar novo)
    @Test
    @DisplayName("POST /jedi SUCCESS")
    public void testPostJediWithSuccess() throws Exception {

        // cenario
        Jedi jediNew = new Jedi("Conde Dookan",150);
        Jedi mockJedi = new Jedi(5, "Conde Dookan", 150, 1);
        Mockito.doReturn(mockJedi).when(jediService).save(any());

        // execução
        mockMvc.perform(post("/jedi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(jediNew)))
                //assert
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("Conde Dookan")))
                .andExpect(jsonPath("$.strength", is(150)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    // TODO: Teste do PUT com sucesso (update)
    @Test
    @DisplayName("PUT /jedi/1 SUCCESS")
    public void testPutJediWithSuccess() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "Conde Dookan", 150, 1);
        Jedi updateJedi = new Jedi("Conde Dookan", 150);

        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1);
        Mockito.doReturn(true).when(jediService).update(any());

        // execução
        mockMvc.perform(put("/jedi/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(updateJedi)))
                //assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1"))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Conde Dookan")))
                .andExpect(jsonPath("$.strength", is(150)))
                .andExpect(jsonPath("$.version", is(2)));
    }

    // TODO: Teste do PUT com uma versao igual da ja existente - deve retornar um conflito
    @Test
    @DisplayName("PUT /jedi/1 Conflict")
    public void testPutJediWithConflict() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "Conde Dookan", 150, 2);
        Jedi updateJedi = new Jedi("Conde Dookan", 150);

        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1);
        Mockito.doReturn(true).when(jediService).update(any());

        // execução
        mockMvc.perform(put("/jedi/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(updateJedi)))

                //assert
                .andExpect(status().isConflict());
    }

    // TODO: Teste do PUT com erro - not found
    @Test
    @DisplayName("PUT /jedi/2 Not Found")
    public void testPutJediWithInsuccessNotFound() throws Exception {

        // cenario
        Jedi updateJedi = new Jedi("Conde Dookan", 150);

        Mockito.doReturn(Optional.empty()).when(jediService).findById(2);
        Mockito.doReturn(false).when(jediService).update(any());

        // execução
        mockMvc.perform(put("/jedi/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(updateJedi)))
                //assert
                .andExpect(status().isNotFound());
    }

    // TODO: Teste do delete com sucesso
    @Test
    @DisplayName("DELETE /jedi/1 SUCCESS")
    public void testDeleteJediWithSuccess() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "Conde Dookan", 150, 1);

        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1);
        Mockito.doReturn(true).when(jediService).delete(1);

        // execução
        mockMvc.perform(delete("/jedi/{id}", 1))
                //assert
                .andExpect(status().isOk());

    }

    // TODO: Teste do delete com erro - deletar um id ja deletado
    @Test
    @DisplayName("DELETE /jedi/1 - Not Found")
    void testJediDeleteNotFound() throws Exception {

        doReturn(Optional.empty()).when(jediService).findById(1);

        mockMvc.perform(delete("/jedi/{id}", 1))
                .andExpect(status().isNotFound());
    }

    // TODO: Teste do delete com erro  - internal server error
    @Test
    @DisplayName("DELETE /jedi/1 - Internal error")
    void testJediDeleteFailure() throws Exception {

        Jedi mockJedi = new Jedi(1, "Conde Dookan", 150, 1);

        doReturn(Optional.of(mockJedi)).when(jediService).findById(1);
        doReturn(false).when(jediService).delete(1);

        mockMvc.perform(delete("/jedi/{id}", 1))
                .andExpect(status().isInternalServerError());
    }



}
