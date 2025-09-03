-- V4: Agregar campos de auditoría a la tabla resources
-- Fecha: 2025-09-01
-- Descripción: Añade created_date y updated_date para auditoría automática de recursos

-- Agregar campos de auditoría a la tabla resources
ALTER TABLE resources
    ADD COLUMN created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Crear índices para mejorar el rendimiento en consultas de auditoría
CREATE INDEX idx_resources_created_date ON resources(created_date);
CREATE INDEX idx_resources_updated_date ON resources(updated_date);

-- Actualizar registros existentes con timestamp actual
-- Esto es necesario para que los registros existentes tengan valores válidos
UPDATE resources
SET
    created_date = CURRENT_TIMESTAMP,
    updated_date = CURRENT_TIMESTAMP
WHERE created_date IS NULL OR updated_date IS NULL;

-- Comentarios sobre los campos agregados:
-- created_date: Se establece automáticamente al crear un registro (updatable = false en JPA)
-- updated_date: Se actualiza automáticamente en cada modificación del registro
-- Los índices mejoran el rendimiento para consultas de ordenamiento por fechas
