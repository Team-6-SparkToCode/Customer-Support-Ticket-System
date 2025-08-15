-- Delete old DB if exists (WARNING: this wipes it)
DROP DATABASE IF EXISTS support_app;

-- Create database fresh
CREATE DATABASE support_app
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE support_app;

-- USERS
CREATE TABLE users (
  id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  email         VARCHAR(191) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  name          VARCHAR(191) NOT NULL,
  role          ENUM('CUSTOMER','STAFF','ADMIN') NOT NULL,
  phone         VARCHAR(50),
  department    VARCHAR(191),
  created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- CATEGORIES
CREATE TABLE categories (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name        VARCHAR(100) NOT NULL UNIQUE,
  description VARCHAR(255),
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- PRIORITIES
CREATE TABLE priorities (
  id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name   VARCHAR(50) NOT NULL UNIQUE,
  level  TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT chk_priorities_level CHECK (level BETWEEN 1 AND 4)
) ENGINE=InnoDB;

-- TICKETS
CREATE TABLE tickets (
  id                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  customer_id        BIGINT UNSIGNED NOT NULL,
  assigned_staff_id  BIGINT UNSIGNED NULL,
  category_id        BIGINT UNSIGNED NOT NULL,
  priority_id        BIGINT UNSIGNED NOT NULL,
  subject            VARCHAR(200) NOT NULL,
  description        MEDIUMTEXT NOT NULL,
  status             ENUM('OPEN','IN_PROGRESS','RESOLVED','CLOSED') NOT NULL DEFAULT 'OPEN',
  created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_tickets_customer (customer_id),
  KEY idx_tickets_assigned (assigned_staff_id),
  KEY idx_tickets_status (status),
  KEY idx_tickets_priority (priority_id),
  KEY idx_tickets_category (category_id),
  CONSTRAINT fk_tickets_customer FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_tickets_assigned FOREIGN KEY (assigned_staff_id) REFERENCES users(id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_tickets_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_tickets_priority FOREIGN KEY (priority_id) REFERENCES priorities(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- TICKET MESSAGES
CREATE TABLE ticket_messages (
  id             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  ticket_id      BIGINT UNSIGNED NOT NULL,
  sender_id      BIGINT UNSIGNED NOT NULL,
  message        MEDIUMTEXT NOT NULL,
  attachment_url VARCHAR(512),
  created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_msgs_ticket_created (ticket_id, created_at),
  CONSTRAINT fk_msgs_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_msgs_sender FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- NOTIFICATIONS
CREATE TABLE notifications (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id    BIGINT UNSIGNED NOT NULL,
  ticket_id  BIGINT UNSIGNED NULL,
  type       VARCHAR(50) NOT NULL,
  message    VARCHAR(500) NOT NULL,
  sent_at    TIMESTAMP NULL,
  PRIMARY KEY (id),
  KEY idx_notifications_user (user_id),
  KEY idx_notifications_ticket (ticket_id),
  CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_notif_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- SEED DATA
INSERT INTO categories (name, description) VALUES
 ('Technical Issue','Bugs, access, errors'),
 ('Billing','Invoices, refunds, payments'),
 ('General Inquiry','Questions and requests');

INSERT INTO priorities (name, level) VALUES
 ('Low',1), ('Medium',2), ('High',3), ('Urgent',4);

INSERT INTO users (email, password_hash, name, role, phone, department) VALUES
 ('customer1@example.com', '$2b$12$replace_me_customer', 'Customer One', 'CUSTOMER','70000000', NULL),
 ('staff1@example.com',    '$2b$12$replace_me_staff',    'Staff One',    'STAFF',   '70000001','Support'),
 ('admin1@example.com',    '$2b$12$replace_me_admin',    'Admin One',    'ADMIN',   '70000002','Support');

USE support_app;

-- Assume: customer id=1, staff id=2 (from your users table)
-- Create a ticket
INSERT INTO tickets (customer_id, category_id, priority_id, subject, description)
VALUES (1, 1, 2, 'Login not working', 'I can’t log in after password reset.');
SET @ticket_id := LAST_INSERT_ID();

-- Customer adds message
INSERT INTO ticket_messages (ticket_id, sender_id, message)
VALUES (@ticket_id, 1, 'Tried again. Still says invalid token.');

-- Assign to staff (id=2) and set IN_PROGRESS
UPDATE tickets SET assigned_staff_id = 2, status = 'IN_PROGRESS'
WHERE id = @ticket_id;

-- Staff replies
INSERT INTO ticket_messages (ticket_id, sender_id, message)
VALUES (@ticket_id, 2, 'We reset your session. Please try again now.');

-- Mark resolved
UPDATE tickets SET status = 'RESOLVED' WHERE id = @ticket_id;

-- Read: ticket header
SELECT t.id, t.subject, t.status, c.name AS category, p.name AS priority,
       t.customer_id, t.assigned_staff_id, t.created_at, t.updated_at
FROM tickets t
JOIN categories c ON c.id = t.category_id
JOIN priorities p ON p.id = t.priority_id
WHERE t.id = @ticket_id;

-- Read: conversation thread
SELECT m.id, m.ticket_id, m.sender_id, u.name AS sender_name,
       m.message, m.attachment_url, m.created_at
FROM ticket_messages m
JOIN users u ON u.id = m.sender_id
WHERE m.ticket_id = @ticket_id
ORDER BY m.created_at ASC;

-- Replace :customer_id with the logged-in customer’s id
SET @customer_id := 1;

SELECT
  t.id, t.subject, t.status,
  p.name AS priority, c.name AS category,
  t.created_at,
  GREATEST(
    t.updated_at,
    COALESCE((SELECT MAX(m.created_at) FROM ticket_messages m WHERE m.ticket_id = t.id), t.updated_at)
  ) AS last_activity_at
FROM tickets t
JOIN priorities p ON p.id = t.priority_id
JOIN categories  c ON c.id = t.category_id
WHERE t.customer_id = @customer_id
ORDER BY last_activity_at DESC
LIMIT 50 OFFSET 0;

-- Example filters (set to NULL to ignore)
SET @status      := 'IN_PROGRESS';   -- or 'OPEN','RESOLVED','CLOSED' or NULL
SET @category_id := NULL;            -- e.g., 1 or NULL
SET @priority_id := NULL;            -- e.g., 3 or NULL
SET @assignee_id := 2;               -- staff user id, or NULL for "unassigned + all"

SELECT
  t.id, t.subject,
  c.name AS category, p.name AS priority,
  t.status, t.assigned_staff_id,
  t.created_at, t.updated_at
FROM tickets t
JOIN categories c ON c.id = t.category_id
JOIN priorities p ON p.id = t.priority_id
WHERE
  (@status      IS NULL OR t.status = @status) AND
  (@category_id IS NULL OR t.category_id = @category_id) AND
  (@priority_id IS NULL OR t.priority_id = @priority_id) AND
  (@assignee_id IS NULL OR t.assigned_staff_id = @assignee_id)
ORDER BY t.updated_at DESC
LIMIT 100 OFFSET 0;

-- Example filters (set to NULL to ignore)
SET @status      := 'IN_PROGRESS';   -- or 'OPEN','RESOLVED','CLOSED' or NULL
SET @category_id := NULL;            -- e.g., 1 or NULL
SET @priority_id := NULL;            -- e.g., 3 or NULL
SET @assignee_id := 2;               -- staff user id, or NULL for "unassigned + all"

SELECT
  t.id, t.subject,
  c.name AS category, p.name AS priority,
  t.status, t.assigned_staff_id,
  t.created_at, t.updated_at
FROM tickets t
JOIN categories c ON c.id = t.category_id
JOIN priorities p ON p.id = t.priority_id
WHERE
  (@status      IS NULL OR t.status = @status) AND
  (@category_id IS NULL OR t.category_id = @category_id) AND
  (@priority_id IS NULL OR t.priority_id = @priority_id) AND
  (@assignee_id IS NULL OR t.assigned_staff_id = @assignee_id)
ORDER BY t.updated_at DESC
LIMIT 100 OFFSET 0;

-- Change the password!
CREATE USER IF NOT EXISTS 'support_user'@'localhost' IDENTIFIED BY 'Jusfaded';
GRANT ALL PRIVILEGES ON support_app.* TO 'support_user'@'localhost';
FLUSH PRIVILEGES;

-- Customer-facing flat list
CREATE OR REPLACE VIEW v_customer_tickets AS
SELECT t.id, t.customer_id, t.assigned_staff_id, t.subject,
       c.name AS category, p.name AS priority, t.status,
       t.created_at, t.updated_at
FROM tickets t
JOIN categories c ON c.id = t.category_id
JOIN priorities p ON p.id = t.priority_id;

-- Staff dashboard counts
CREATE OR REPLACE VIEW v_staff_dashboard AS
SELECT
  SUM(t.status = 'OPEN')         AS open_count,
  SUM(t.status = 'IN_PROGRESS')  AS in_progress_count,
  SUM(t.status = 'RESOLVED')     AS resolved_count,
  SUM(t.status = 'CLOSED')       AS closed_count
FROM tickets t;
