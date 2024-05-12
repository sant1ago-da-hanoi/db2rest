package com.homihq.db2rest.rest.oracle;

import com.homihq.db2rest.OracleBaseIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.homihq.db2rest.jdbc.rest.RdbmsRestApi.VERSION;
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(201)
class OracleReadControllerTest extends OracleBaseIntegrationTest {
    @Test
    @DisplayName("Test find all films - all columns.")
    void findAllFilms() throws Exception {

        mockMvc.perform(get(VERSION + "/oradb/FILM")
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                //.andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(jsonPath("$.*", anyOf(hasSize(4),hasSize(9), hasSize(8) )))
                .andExpect(jsonPath("$[0].*", hasSize(14)))
                .andDo(document("oracle-get-all-films-all-columns"));
    }

    @Test
    @DisplayName("Test find all films - 3 columns")
    void findAllFilmsWithThreeCols() throws Exception {
        mockMvc.perform(get(VERSION + "/oradb/FILM")
                        .contentType(APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("fields", "title,description,release_year")
                        )
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.*", anyOf(hasSize(4),hasSize(9),hasSize(8))))
                .andExpect(jsonPath("$[0].*", hasSize(3)))
                .andDo(document("oracle-find-all-films-3-columns"));
    }

    @Test
    @DisplayName("Test find all films - with column alias")
    void findAllFilmsWithColumnAlias() throws Exception {
        mockMvc.perform(get(VERSION + "/oradb/FILM")
                        .contentType(APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("fields", "title,description,release_year:releaseYear")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.*", anyOf(hasSize(4),hasSize(9), hasSize(8))))
                .andExpect(jsonPath("$[0].TITLE", notNullValue()))
                .andExpect(jsonPath("$[0].DESCRIPTION", notNullValue()))
                .andExpect(jsonPath("$[0].releaseYear", notNullValue()))
                .andDo(document("oracle-find-all-films-with-column-alias"));
    }


    @Test
    @DisplayName("Get one")
    void findOneFilm() throws Exception {
        mockMvc.perform(get(VERSION + "/oradb/FILM/one")
                        .accept(MediaType.APPLICATION_JSON)
                .param("fields", "title")
                .param("filter", "film_id==1"))
                .andExpect(status().isOk())
                //.andDo(print())
                .andDo(document("oracle-get-on-film"));
    }
}
