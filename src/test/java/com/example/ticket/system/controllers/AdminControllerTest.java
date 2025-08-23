/*
package com.example.ticket.system.controllers;

import com.example.ticket.system.entities.Category;
import com.example.ticket.system.entities.Priority;
import com.example.ticket.system.entities.Staff;
import com.example.ticket.system.repositories.CategoryRepository;
import com.example.ticket.system.repositories.PriorityRepository;
import com.example.ticket.system.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserService userService = Mockito.mock(UserService.class);
    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    private final PriorityRepository priorityRepository = Mockito.mock(PriorityRepository.class);

    private AdminController buildController() {
        AdminController controller = new AdminController();
        // inject fields via reflection since controller uses @Autowired fields
        try {
            var usField = AdminController.class.getDeclaredField("userService");
            usField.setAccessible(true);
            usField.set(controller, userService);
            var catField = AdminController.class.getDeclaredField("categoryRepository");
            catField.setAccessible(true);
            catField.set(controller, categoryRepository);
            var prField = AdminController.class.getDeclaredField("priorityRepository");
            prField.setAccessible(true);
            prField.set(controller, priorityRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    private void setupMockMvc() {
        this.mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup(buildController()).build();
    }

    @Test
    @DisplayName("PUT /api/admin/users/{id}/promote/staff calls service and returns user")
    void promoteToStaff_success() throws Exception {
        Staff staff = new Staff();
        staff.setId(10L);
        staff.setEmail("staff@example.com");
        staff.setName("Staffy");
        given(userService.promoteToStaff(10L)).willReturn(staff);

        setupMockMvc();
                mockMvc.perform(put("/api/admin/users/{id}/promote/staff", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.email", is("staff@example.com")));

        verify(userService).promoteToStaff(10L);
    }

    @Test
    @DisplayName("PUT /api/admin/users/{id}/promote/admin calls service and returns user")
    void promoteToAdmin_success() throws Exception {
        Staff admin = new Staff(); // returning concrete subclass for simplicity
        admin.setId(2L);
        admin.setEmail("admin@example.com");
        admin.setName("Adminy");
        given(userService.promoteToAdmin(2L)).willReturn(admin);

        setupMockMvc();
                mockMvc.perform(put("/api/admin/users/{id}/promote/admin", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.email", is("admin@example.com")));

        verify(userService).promoteToAdmin(2L);
    }

    @Test
    @DisplayName("POST /api/admin/categories saves and returns category")
    void createCategory_success() throws Exception {
        Category input = new Category();
        input.setName("Billing");
        input.setDescription("Billing issues");

        Category saved = new Category();
        saved.setId(1L);
        saved.setName(input.getName());
        saved.setDescription(input.getDescription());

        given(categoryRepository.save(any(Category.class))).willReturn(saved);

        setupMockMvc();
                mockMvc.perform(post("/api/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Billing")))
                .andExpect(jsonPath("$.description", is("Billing issues")));

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("GET /api/admin/categories returns list")
    void getAllCategories_success() throws Exception {
        Category c1 = new Category(); c1.setId(1L); c1.setName("Billing");
        Category c2 = new Category(); c2.setId(2L); c2.setName("Tech");
        given(categoryRepository.findAll()).willReturn(List.of(c1, c2));

        setupMockMvc();
                mockMvc.perform(get("/api/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Billing")))
                .andExpect(jsonPath("$[1].name", is("Tech")));

        verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName("POST /api/admin/priorities saves and returns priority")
    void createPriority_success() throws Exception {
        Priority input = new Priority();
        input.setName("High");
        input.setLevel(3);

        Priority saved = new Priority();
        saved.setId(5L);
        saved.setName(input.getName());
        saved.setLevel(input.getLevel());

        given(priorityRepository.save(any(Priority.class))).willReturn(saved);

        setupMockMvc();
                mockMvc.perform(post("/api/admin/priorities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("High")))
                .andExpect(jsonPath("$.level", is(3)));

        verify(priorityRepository).save(any(Priority.class));
    }

    @Test
    @DisplayName("GET /api/admin/priorities returns list")
    void getAllPriorities_success() throws Exception {
        Priority p1 = new Priority(); p1.setId(1L); p1.setName("Low"); p1.setLevel(1);
        Priority p2 = new Priority(); p2.setId(2L); p2.setName("High"); p2.setLevel(3);
        given(priorityRepository.findAll()).willReturn(List.of(p1, p2));

        setupMockMvc();
                mockMvc.perform(get("/api/admin/priorities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Low")))
                .andExpect(jsonPath("$[1].name", is("High")));

        verify(priorityRepository).findAll();
    }
}
*/
