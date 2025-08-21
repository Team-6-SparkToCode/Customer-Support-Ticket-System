package com.example.ticket.system.controllers;

import com.example.ticket.system.Role;
import com.example.ticket.system.TicketStatus;
import com.example.ticket.system.entities.Message;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.entities.User;
import com.example.ticket.system.repositories.MessageRepository;
import com.example.ticket.system.repositories.TicketRepository;
import com.example.ticket.system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TicketMessageController {

    private final MessageRepository messageRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    // Injected from application.yml (with a sensible default if missing)
    private final String uploadDir;

    public TicketMessageController(MessageRepository messageRepository,
                                   TicketRepository ticketRepository,
                                   UserRepository userRepository,
                                   @Value("${app.upload-dir:uploads/messages}") String uploadDir) {
        this.messageRepository = messageRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.uploadDir = uploadDir;
    }

    // ---------- GET: list messages ----------
    @GetMapping("/tickets/{ticketId}/messages")
    public List<Message> getMessages(@PathVariable Long ticketId,
                                     @RequestParam("userId") Long userId) {
        User current = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        boolean isAdmin = current.getRole() == Role.ADMIN;
        boolean isOwner = ticket.getCustomer() != null && ticket.getCustomer().getId().equals(current.getId());
        boolean isAssignedStaff = ticket.getAssignedStaff() != null && ticket.getAssignedStaff().getId().equals(current.getId());
        if (!(isAdmin || isOwner || isAssignedStaff)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to view this ticket's messages");
        }

        return messageRepository.findByTicketIdOrderByTimestampAsc(ticketId);
    }

    // ---------- POST: create message (text + optional file) ----------
    @PostMapping(value = "/tickets/{ticketId}/messages", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public Message postMessage(@PathVariable Long ticketId,
                               @RequestParam("userId") Long userId,
                               @RequestParam("content") String content,
                               @RequestParam(value = "file", required = false) MultipartFile file) {

        User current = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        boolean isAdmin = current.getRole() == Role.ADMIN;
        boolean isOwner = ticket.getCustomer() != null && ticket.getCustomer().getId().equals(current.getId());
        boolean isAssignedStaff = ticket.getAssignedStaff() != null && ticket.getAssignedStaff().getId().equals(current.getId());
        if (!(isAdmin || isOwner || isAssignedStaff)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to reply on this ticket");
        }

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ticket is closed; replies are not allowed");
        }

        if (content == null || content.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message content cannot be empty");
        }

        Message msg = new Message();
        msg.setTicket(ticket);
        msg.setSender(current);
        msg.setContent(content.trim());

        // ---- Optional file handling ----
        if (file != null && !file.isEmpty()) {
            // (Optional) basic size/type guards — tweak as you like
            long maxBytes = 10L * 1024 * 1024; // 10MB
            if (file.getSize() > maxBytes) {
                throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File too large (max 10MB)");
            }

            // Sanitize filename & add a unique prefix to avoid collisions
            String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "upload.bin" : file.getOriginalFilename());
            String safeName = originalName.replaceAll("[\\s]+", "_").replaceAll("[^A-Za-z0-9._-]", "");
            String uniqueName = UUID.randomUUID() + "_" + Instant.now().toEpochMilli() + "_" + safeName;

            // Make sure directory exists
            Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create upload directory", e);
            }

            // Save the file
            Path dest = root.resolve(uniqueName).normalize();
            try {
                file.transferTo(dest.toFile());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save file", e);
            }

            // Store relative path (or build a public URL if you serve static files)
            msg.setAttachmentUrl(dest.toString());
        }

        return messageRepository.save(msg);
    }
}
