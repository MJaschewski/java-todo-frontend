package de.neuefische.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.backend.model.ToDo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class KanbanControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DirtiesContext
    void getToDos_returnStatus200Ok_returnEmptyListToDos() throws Exception{
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));

    }

    @Test
    @DirtiesContext
    void getToDos_returnStatus200Ok_returnsListWithToDos() throws Exception {
        //Given
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"description":"Hello World!","status":"OPEN"}
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("""
                                {"description":"Hello World!","status":"OPEN"}
                                """))
                .andExpect(jsonPath("$.id").isNotEmpty()).andReturn();
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                                    [
                                                                        {"description":"Hello World!","status":"OPEN"}
                                                                    ]
                                                                """));


    }

    @Test
    @DirtiesContext
    void postToDo_returnsStatus200Ok_returnPostedToDo() throws Exception{
        //GIVEN
        String requestBody = """
                                {"description":"Hello World!","status":"OPEN"}
                                """;
        //When & Then
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(requestBody))
                .andExpect(jsonPath("$.id").isNotEmpty()).andReturn();
    }

    @Test
    @DirtiesContext
    void postToDoNoBody_returnStatus400() throws Exception {
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo"))
                .andExpect(status().isBadRequest());

    }



    @Test
    @DirtiesContext
    void getToDo_whenWrongID_returnsStatus404() throws Exception{
        //Given
        String toDoId = "FalseID";
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/"+toDoId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void getToDo_returnsStatus200Ok_returnToDoWithRightID() throws Exception{
        //Given
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"description":"Hello World!","status":"OPEN"}
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("""
                                {"description":"Hello World!","status":"OPEN"}
                                """))
                .andExpect(jsonPath("$.id").isNotEmpty()).andReturn();

        String content = response.getResponse().getContentAsString();
        ToDo toDo = new ObjectMapper().readValue(content, ToDo.class);
        String toDoId = toDo.getId();

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/"+toDoId))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                            {"description":"Hello World!","status":"OPEN"}
                                            """))
                .andExpect(jsonPath("$.id").value(toDoId));

    }

    @Test
    @DirtiesContext
    void editToDo_RightIdWithBody_returnsStatus200Ok_returnsChangedToDo() throws Exception {
        //Given
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"description":"Hello World!","status":"OPEN"}
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("""
                                {"description":"Hello World!","status":"OPEN"}
                                """))
                .andExpect(jsonPath("$.id").isNotEmpty()).andReturn();

        String content = response.getResponse().getContentAsString();
        ToDo toDo = new ObjectMapper().readValue(content, ToDo.class);
        String toDoId = toDo.getId();
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/"+toDoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"description":"Hello World! World says No!","status":"OPEN"}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                                {"description":"Hello World! World says No!","status":"OPEN"}
                                            """))
                .andExpect(jsonPath("$.id").value(toDoId));
    }

    @Test
    @DirtiesContext
    void editToDo_wrongIDCorrectBody_returnStatus404() throws Exception {
        //Given
        String toDoId = "FalseId";
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/"+toDoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"description":"Hello World! World says No!","status":"OPEN"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void editToDo_rightIDNoBody_returnStatus400() throws Exception {
        //Given
        String toDoId = "FalseId";
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/"+toDoId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void removeToDo_returnStatus200Ok_returnRemovedToDo_andGetToDoRightID_returns404() throws Exception {
        //Given
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"description":"Hello World!","status":"OPEN"}
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("""
                                {"description":"Hello World!","status":"OPEN"}
                                """))
                .andExpect(jsonPath("$.id").isNotEmpty()).andReturn();

        String content = response.getResponse().getContentAsString();
        ToDo toDo = new ObjectMapper().readValue(content, ToDo.class);
        String toDoId = toDo.getId();

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/"+toDoId))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                            {"description":"Hello World!","status":"OPEN"}
                                            """))
                .andExpect(jsonPath("$.id").value(toDoId));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/"+toDoId))
                .andExpect(status().isNotFound());
    }
    @Test
    @DirtiesContext
    void removeToDo_wrongID_returnStatus404() throws Exception {
        //Given
        String toDoId = "FalseId";
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/"+toDoId))
                .andExpect(status().isNotFound());
    }



}