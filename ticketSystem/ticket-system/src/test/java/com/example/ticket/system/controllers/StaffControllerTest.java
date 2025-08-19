/*
package com.example.ticket.system.controllers;

import com.example.ticket.system.TicketStatus;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.service.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StaffControllerTest {

    private MockMvc mockMvc;

    private final TicketService ticketService = Mockito.mock(TicketService.class);

    private StaffController buildController() {
        StaffController controller = new StaffController();
        try {
            var f = StaffController.class.getDeclaredField("ticketService");
            f.setAccessible(true);
            f.set(controller, ticketService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    private void setupMockMvc() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(buildController()).build();
    }

    @Test
    @DisplayName("PUT /api/staff/tickets/{ticketId}/assign/{staffId} assigns and returns ticket")
    void assignTicket_success() throws Exception {
        Ticket t = new Ticket();
        t.setId(100L);
        given(ticketService.assignTicket(100L, 50L)).willReturn(t);

        setupMockMvc();
        mockMvc.perform(put("/api/staff/tickets/{ticketId}/assign/{staffId}", 100L, 50L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)));

        verify(ticketService).assignTicket(100L, 50L);
    }

    @Test
    @DisplayName("PUT /api/staff/tickets/{ticketId}/status updates and returns ticket")
    void updateStatus_success() throws Exception {
        Ticket t = new Ticket();
        t.setId(101L);
        t.setStatus(TicketStatus.IN_PROGRESS);
        given(ticketService.updateStatus(101L, TicketStatus.IN_PROGRESS)).willReturn(t);

        setupMockMvc();
        mockMvc.perform(put("/api/staff/tickets/{ticketId}/status", 101L)
                        .param("status", "IN_PROGRESS")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(101)));

        verify(ticketService).updateStatus(101L, TicketStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("POST /api/staff/tickets/{ticketId}/reply triggers staffReply and returns 200")
    void reply_success() throws Exception {
        setupMockMvc();
        mockMvc.perform(post("/api/staff/tickets/{ticketId}/reply", 200L)
                        .param("staffId", "77")
                        .param("message", "On it!"))
                .andExpect(status().isOk());

        verify(ticketService).staffReply(200L, 77L, "On it!");
    }
}
*/
