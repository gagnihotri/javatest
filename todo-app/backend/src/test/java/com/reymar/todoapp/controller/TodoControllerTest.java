package com.application.todoapp.controller;

import com.application.todo.controller.TodoController;
import org.springframework.boot.test.context.SpringBootTest;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(classes = TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private Todo todoItem;

    @BeforeEach
    public void setup() {
        todoItem = new Todo();
        todoItem.setId(1);
        todoItem.setTask("Test Task");
        todoItem.setIsDone(false);
    }

    @Test
    public void testFetchAllTodos() throws Exception {
        List<Todo> todoList = Arrays.asList(todoItem);

        // Mock the service call to return a list of todos
        when(todoService.fetchAllTodos()).thenReturn(todoList);

        mockMvc.perform(get("/api/todoItems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].task").value("Test Task"))
                .andExpect(jsonPath("$[0].isDone").value(false));
    }

    @Test
    public void testCreateNewTodoItem() throws Exception {
        // Mock the service call to return the created todo item
        when(todoService.createNewTodoItem()).thenReturn(todoItem);

        mockMvc.perform(post("/api/todoItems")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.task").value("Test Task"))
                .andExpect(jsonPath("$.isDone").value(false));
    }

    @Test
    public void testUpdateTodoItem() throws Exception {
        Todo updatedTodo = new Todo();
        updatedTodo.setId(1);
        updatedTodo.setTask("Updated Task");
        updatedTodo.setIsDone(true);

        // Mock the service call to return the updated todo item
        when(todoService.updateTodoItem(1, updatedTodo)).thenReturn(updatedTodo);

        mockMvc.perform(put("/api/todoItems/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"task\":\"Updated Task\",\"isDone\":true}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.task").value("Updated Task"))
                .andExpect(jsonPath("$.isDone").value(true));
    }

    @Test
    public void testDeleteTodoItem() throws Exception {
        // Mock the service call to do nothing for deletion
        mockMvc.perform(delete("/api/todoItems/{id}", 1))
                .andExpect(status().isOk());
    }
}
