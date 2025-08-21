package com.example.ticket.system.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.ticket.system.TicketStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "assigned_staff_id")
    private Staff assignedStaff;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "priority_id", nullable = false)
    private Priority priority;

    @Column(name = "subject", nullable = false, length = 200)
    private String subject;

    @Column(name = "description", nullable = false, columnDefinition = "mediumtext")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status = TicketStatus.OPEN;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timestamp ASC")
    private List<Message> messages = new ArrayList<>();

    @Transient
    private List<String> internalNotes = new ArrayList<>();

    // domain helpers
    public void addMessage(Message message) {
        if (message == null) return;
        message.setTicket(this);
        this.messages.add(message);
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void assignStaff(Staff staff) {
        this.assignedStaff = staff;
    }

    public void addInternalNote(String note) {
        if (note == null) return;
        this.internalNotes.add(note);
    }

    public void setAssignedStaff(Staff staff) {
        this.assignedStaff = staff;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Staff getAssignedStaff() {
        return assignedStaff;
    }

    public Long getId() {
        return this.id;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Category getCategory() {
        return category;
    }

    public Priority getPriority() {
        return priority;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public String getSubject() {
        return this.subject;
    }
}
