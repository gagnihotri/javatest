package com.application.todoapp.controller;

import com.application.todo.TodoAppApplication;
import com.application.todo.controller.TodoController;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.application.todo.entity.Todo;
import com.application.todo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.context.annotation.Import;
import static org.mockito.ArgumentMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TodoAppApplication.class)
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private Todo todoItem;

    @BeforeEach
    public void setUp() {
        // Initialize the Todo object
        todoItem = new Todo();
        todoItem.setId(1);
        todoItem.setTask("Test Task");
        todoItem.setIsDone(false);
    }

    @Test
    public void testFetchAllTodos() throws Exception {
        // Mock the service method
        when(todoService.fetchAllTodos()).thenReturn(Arrays.asList(todoItem));

        // Perform GET request and assert the response
        mockMvc.perform(get("/api/todoItems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].task").value("Test Task"))
                .andExpect(jsonPath("$[0].isDone").value(false));
    }

    @Test
    public void testCreateNewTodoItem() throws Exception {
        // Mock the service method
        when(todoService.createNewTodoItem()).thenReturn(todoItem);

        // Perform POST request and assert the response
        mockMvc.perform(post("/api/todoItems")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"task\":\"Test Task\",\"isDone\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Test Task"))
                .andExpect(jsonPath("$.isDone").value(false));
    }

    @Test
    public void testUpdateTodoItem() throws Exception {
        // Mock the service method
        when(todoService.updateTodoItem(ArgumentMatchers.eq(1), ArgumentMatchers.any(Todo.class))).thenReturn(todoItem);

        // Perform PUT request and assert the response
        mockMvc.perform(put("/api/todoItems/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"task\":\"Updated Task\",\"isDone\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Updated Task"))
                .andExpect(jsonPath("$.isDone").value(true));
    }

    /*@Test
    public void testDeleteTodoItem() throws Exception {
        // Mock the service method
        doNothing().when(todoService).deleteTodoItem(1);

        // Perform DELETE request and assert the response
        mockMvc.perform(delete("/api/todoItems/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }*/
}
