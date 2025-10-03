-- V3: Update Notifications Table Structure

-- Drop the existing notifications table to avoid conflicts
DROP TABLE IF EXISTS notifications CASCADE;

-- Recreate the notifications table with the updated structure
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    message VARCHAR(255),
    notification_type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    sent_at TIMESTAMP,
    user_id BIGINT NOT NULL,
    reservation_id BIGINT NOT NULL,
    CONSTRAINT fk_notifications_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_notifications_reservations FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

-- Change the user_role column type to VARCHAR to align with EnumType.STRING in JPA
ALTER TABLE users ALTER COLUMN user_role TYPE VARCHAR(255) USING user_role::text;

-- Change the user_status column type to VARCHAR to align with EnumType.STRING in JPA
ALTER TABLE users ALTER COLUMN user_status TYPE VARCHAR(255) USING user_status::text;