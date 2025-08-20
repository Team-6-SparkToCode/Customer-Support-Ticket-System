create table categories
(
    id          bigint unsigned auto_increment
        primary key,
    name        varchar(100) not null,
    description varchar(255) null,
    constraint name
        unique (name)
);

create table priorities
(
    id    bigint unsigned auto_increment
        primary key,
    name  varchar(50)      not null,
    level tinyint unsigned not null,
    constraint name
        unique (name),
    constraint chk_priorities_level
        check (`level` between 1 and 4)
);

create table users
(
    id            bigint unsigned auto_increment
        primary key,
    email         varchar(191)                        not null,
    password_hash varchar(255)                        not null,
    name          varchar(191)                        not null,
    role          enum ('CUSTOMER', 'STAFF', 'ADMIN') not null,
    phone         varchar(50)                         null,
    department    varchar(191)                        null,
    created_at    timestamp default CURRENT_TIMESTAMP not null,
    constraint email
        unique (email)
);

create table tickets
(
    id                bigint unsigned auto_increment
        primary key,
    customer_id       bigint unsigned                                                              not null,
    assigned_staff_id bigint unsigned                                                              null,
    category_id       bigint unsigned                                                              not null,
    priority_id       bigint unsigned                                                              not null,
    subject           varchar(200)                                                                 not null,
    description       mediumtext                                                                   not null,
    status            enum ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') default 'OPEN'            not null,
    created_at        timestamp                                          default CURRENT_TIMESTAMP not null,
    updated_at        timestamp                                          default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint fk_tickets_assigned
        foreign key (assigned_staff_id) references users (id)
            on update cascade on delete set null,
    constraint fk_tickets_category
        foreign key (category_id) references categories (id)
            on update cascade,
    constraint fk_tickets_customer
        foreign key (customer_id) references users (id)
            on update cascade,
    constraint fk_tickets_priority
        foreign key (priority_id) references priorities (id)
            on update cascade
);

create table notifications
(
    id        bigint unsigned auto_increment
        primary key,
    user_id   bigint unsigned not null,
    ticket_id bigint unsigned null,
    type      varchar(50)     not null,
    message   varchar(500)    not null,
    sent_at   timestamp       null,
    constraint fk_notif_ticket
        foreign key (ticket_id) references tickets (id)
            on update cascade on delete cascade,
    constraint fk_notif_user
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);

create index idx_notifications_ticket
    on notifications (ticket_id);

create index idx_notifications_user
    on notifications (user_id);

create table ticket_messages
(
    id             bigint unsigned auto_increment
        primary key,
    ticket_id      bigint unsigned                     not null,
    sender_id      bigint unsigned                     not null,
    message        mediumtext                          not null,
    attachment_url varchar(512)                        null,
    created_at     timestamp default CURRENT_TIMESTAMP not null,
    constraint fk_msgs_sender
        foreign key (sender_id) references users (id)
            on update cascade,
    constraint fk_msgs_ticket
        foreign key (ticket_id) references tickets (id)
            on update cascade on delete cascade
);

create index idx_msgs_ticket_created
    on ticket_messages (ticket_id, created_at);

create index idx_tickets_assigned
    on tickets (assigned_staff_id);

create index idx_tickets_category
    on tickets (category_id);

create index idx_tickets_customer
    on tickets (customer_id);

create index idx_tickets_priority
    on tickets (priority_id);

create index idx_tickets_status
    on tickets (status);

