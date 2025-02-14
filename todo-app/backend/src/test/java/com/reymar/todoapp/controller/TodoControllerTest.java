package com.reymar.todoapp.controller;

import com.application.todo.controller.TodoController;
import com.reymar.todoapp.service.TodoService;
import com.reymar.todoapp.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TodoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    public void testFetchAllTodos() throws Exception {
        Todo todo1 = new Todo(1, "Todo 1", "Description 1");
        Todo todo2 = new Todo(2, "Todo 2", "Description 2");
        List<Todo> todos = Arrays.asList(todo1, todo2);

        when(todoService.fetchAllTodos()).thenReturn(todos);

        mockMvc.perform(get("/api/todoItems")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Todo 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Todo 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"));
    }

    @Test
    public void testCreateNewTodoItem() throws Exception {
        Todo newTodo = new Todo(3, "Todo 3", "Description 3");

        when(todoService.createNewTodoItem()).thenReturn(newTodo);

        mockMvc.perform(post("/api/todoItems")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Todo 3\", \"description\": \"Description 3\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Todo 3"))
                .andExpect(jsonPath("$.description").value("Description 3"));
    }

    @Test
    public void testUpdateTodoItem() throws Exception {
        Todo updatedTodo = new Todo(1, "Updated Todo", "Updated Description");

        when(todoService.updateTodoItem(1, updatedTodo)).thenReturn(updatedTodo);

        mockMvc.perform(put("/api/todoItems/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Updated Todo\", \"description\": \"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Todo"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    public void testDeleteTodoItem() throws Exception {
        mockMvc.perform(delete("/api/todoItems/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}
