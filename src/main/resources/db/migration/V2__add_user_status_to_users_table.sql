CREATE TYPE user_status AS ENUM ('ACTIVE', 'INACTIVE', 'DELETED');

ALTER TABLE users ADD COLUMN user_status user_status;