-- V1: Initialize Full Schema based on JPA Entities

-- Drop existing types and tables if they exist, to ensure a clean slate.
-- This is useful for development but should be used with caution.
DROP TABLE IF EXISTS talent_project_lines CASCADE;
DROP TABLE IF EXISTS sessions_logs CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS digital_records CASCADE;
DROP TABLE IF EXISTS reservations_resources CASCADE;
DROP TABLE IF EXISTS equipment_history CASCADE;
DROP TABLE IF EXISTS biotechnology_resource CASCADE;
DROP TABLE IF EXISTS generic_resource CASCADE;
DROP TABLE IF EXISTS resources CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS experts CASCADE;
DROP TABLE IF EXISTS talents CASCADE;
DROP TABLE IF EXISTS service_lines CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP TYPE IF EXISTS user_role;
DROP TYPE IF EXISTS history_event_type;
DROP TYPE IF EXISTS project_line;
DROP TYPE IF EXISTS reservation_status;
DROP TYPE IF EXISTS resource_status;

-- Create ENUM types
CREATE TYPE user_role AS ENUM ('USER', 'ADMIN', 'SUPERADMIN', 'EXPERT', 'TALENT', 'SECURITY');
CREATE TYPE history_event_type AS ENUM ('MANTENIMIENTO_PREVENTIVO', 'MANTENIMIENTO_CORRECTIVO', 'CALIBRACION', 'ACTUALIZACION', 'REPORTE_DE_INCIDENTE', 'DE_BAJA');
CREATE TYPE project_line AS ENUM ('Tics_e_Inteligencia_artificial', 'Diseno_de_Productos', 'Produccion_y_Transformacion', 'Materiales_y_Biotecnologia');
CREATE TYPE reservation_status AS ENUM ('SOLICITADA', 'CONFIRMADA', 'CANCELADA', 'CUMPLIDA', 'INCUMPLIDA');
CREATE TYPE resource_status AS ENUM ('DISPONIBLE', 'NO_DISPONIBLE', 'EN_USO_COMPARTIDO');

-- Create Tables
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    lastname VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    email VARCHAR(255),
    user_role user_role
);

CREATE TABLE service_lines (
    id BIGSERIAL PRIMARY KEY,
    service_line_name VARCHAR(255) UNIQUE
);

CREATE TABLE talents (
    id BIGINT PRIMARY KEY,
    associated_project VARCHAR(255) UNIQUE,
    CONSTRAINT fk_talents_users FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE experts (
    id BIGINT PRIMARY KEY,
    service_line_id BIGINT NOT NULL,
    CONSTRAINT fk_experts_users FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_experts_service_lines FOREIGN KEY (service_line_id) REFERENCES service_lines(id)
);

CREATE TABLE resources (
    id BIGSERIAL PRIMARY KEY,
    resource_type VARCHAR(31) NOT NULL, -- Discriminator column
    name VARCHAR(255) NOT NULL,
    description TEXT,
    plate VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    status resource_status NOT NULL,
    model VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    service_line_id BIGINT NOT NULL,
    CONSTRAINT fk_resources_service_lines FOREIGN KEY (service_line_id) REFERENCES service_lines(id)
);

CREATE TABLE biotechnology_resource (
    id BIGINT PRIMARY KEY,
    max_usuarios_simultaneos INT NOT NULL,
    condiciones_de_uso JSONB,
    CONSTRAINT fk_biotechnology_resource_resources FOREIGN KEY (id) REFERENCES resources(id) ON DELETE CASCADE
);

CREATE TABLE generic_resource (
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_generic_resource_resources FOREIGN KEY (id) REFERENCES resources(id) ON DELETE CASCADE
);

CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    date_time_start TIMESTAMP,
    end_date_time TIMESTAMP,
    reservation_status reservation_status,
    creation_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    expert_id BIGINT NOT NULL,
    talent_id BIGINT NOT NULL,
    CONSTRAINT fk_reservations_experts FOREIGN KEY (expert_id) REFERENCES experts(id),
    CONSTRAINT fk_reservations_talents FOREIGN KEY (talent_id) REFERENCES talents(id)
);

CREATE TABLE equipment_history (
    id BIGSERIAL PRIMARY KEY,
    event_date TIMESTAMP NOT NULL,
    event_type history_event_type NOT NULL,
    details VARCHAR(1000),
    resource_id BIGINT NOT NULL,
    CONSTRAINT fk_equipment_history_resources FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE
);

CREATE TABLE reservations_resources (
    id BIGSERIAL PRIMARY KEY,
    resource_id BIGINT NOT NULL,
    reservation_id BIGINT NOT NULL,
    CONSTRAINT fk_reservations_resources_resources FOREIGN KEY (resource_id) REFERENCES resources(id),
    CONSTRAINT fk_reservations_resources_reservations FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

CREATE TABLE digital_records (
    id BIGSERIAL PRIMARY KEY,
    record_type VARCHAR(255),
    creation_date TIMESTAMP,
    damage_control VARCHAR(255),
    observations VARCHAR(255),
    expert_id BIGINT NOT NULL,
    talent_id BIGINT NOT NULL,
    reservation_id BIGINT NOT NULL,
    CONSTRAINT fk_digital_records_experts FOREIGN KEY (expert_id) REFERENCES experts(id),
    CONSTRAINT fk_digital_records_talents FOREIGN KEY (talent_id) REFERENCES talents(id),
    CONSTRAINT fk_digital_records_reservations FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    id_user BIGINT,
    message VARCHAR(255),
    notification_type VARCHAR(255) NOT NULL,
    sent_at TIMESTAMP,
    status VARCHAR(255),
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_notifications_users FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE sessions_logs (
    id BIGSERIAL PRIMARY KEY,
    login_at TIMESTAMP NOT NULL,
    ip_address VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_sessions_logs_users FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE talent_project_lines (
    talent_id BIGINT NOT NULL,
    project_line project_line NOT NULL,
    PRIMARY KEY (talent_id, project_line),
    CONSTRAINT fk_talent_project_lines_talents FOREIGN KEY (talent_id) REFERENCES talents(id)
);