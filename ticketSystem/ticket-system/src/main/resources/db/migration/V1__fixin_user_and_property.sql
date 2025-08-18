-- Update 'role' column in users table
ALTER TABLE users
    MODIFY COLUMN role VARCHAR(31) NOT NULL;

-- Update 'level' column in priorities table
ALTER TABLE priorities
    MODIFY COLUMN level INT NOT NULL;
