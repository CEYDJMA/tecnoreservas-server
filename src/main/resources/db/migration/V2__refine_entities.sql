-- Refinamiento de la tabla equipment_history
ALTER TABLE equipment_history RENAME COLUMN maintenance_date TO event_date;
ALTER TABLE equipment_history ALTER COLUMN event_date SET NOT NULL;

-- Se añade la nueva columna event_type y se elimina la antigua event_description
ALTER TABLE equipment_history ADD COLUMN event_type VARCHAR(255);
UPDATE equipment_history SET event_type = 'REPORTE_DE_INCIDENTE' WHERE event_type IS NULL;
ALTER TABLE equipment_history ALTER COLUMN event_type SET NOT NULL;
ALTER TABLE equipment_history DROP COLUMN IF EXISTS event_description;

-- Se renombra la columna de detalles y se ajusta su tipo
ALTER TABLE equipment_history RENAME COLUMN incident_reported TO details;
ALTER TABLE equipment_history ALTER COLUMN details TYPE VARCHAR(1000);

-- Refinamiento de la tabla resources (preparación para la herencia)
-- Se añaden las nuevas columnas comunes definidas en la clase abstracta Resource.
ALTER TABLE resources ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE resources ADD COLUMN IF NOT EXISTS model VARCHAR(255);
ALTER TABLE resources ADD COLUMN IF NOT EXISTS location VARCHAR(255);

-- Se añade la columna para el tipo de recurso (discriminador de herencia)
-- y se elimina la columna 'type' que es un remanente.
ALTER TABLE resources ADD COLUMN IF NOT EXISTS resource_type VARCHAR(255);
ALTER TABLE resources DROP COLUMN IF EXISTS type;

-- Se eliminan columnas que ya no pertenecen a la entidad base 'Resource'.
ALTER TABLE resources DROP COLUMN IF EXISTS brand;
ALTER TABLE resources DROP COLUMN IF EXISTS available_for_booking;

-- Se elimina la columna 'serviceline_id' redundante (sin guion bajo)
ALTER TABLE resources DROP COLUMN IF EXISTS serviceline_id;

-- Creación de la tabla para la entidad hija GenericResource
CREATE TABLE generic_resource (
    id BIGINT NOT NULL,
    CONSTRAINT pk_generic_resource PRIMARY KEY (id)
);

ALTER TABLE generic_resource ADD CONSTRAINT FK_GENERIC_RESOURCE_ON_ID FOREIGN KEY (id) REFERENCES resources (id);

-- Creación de las tablas para las líneas de recursos específicas
CREATE TABLE biotechnology_resource (
    id BIGINT NOT NULL,
    CONSTRAINT pk_biotechnology_resource PRIMARY KEY (id)
);
ALTER TABLE biotechnology_resource ADD CONSTRAINT FK_BIOTECHNOLOGY_RESOURCE_ON_ID FOREIGN KEY (id) REFERENCES resources (id);

CREATE TABLE electronics_resource (
    id BIGINT NOT NULL,
    CONSTRAINT pk_electronics_resource PRIMARY KEY (id)
);
ALTER TABLE electronics_resource ADD CONSTRAINT FK_ELECTRONICS_RESOURCE_ON_ID FOREIGN KEY (id) REFERENCES resources (id);

CREATE TABLE engineering_resource (
    id BIGINT NOT NULL,
    CONSTRAINT pk_engineering_resource PRIMARY KEY (id)
);
ALTER TABLE engineering_resource ADD CONSTRAINT FK_ENGINEERING_RESOURCE_ON_ID FOREIGN KEY (id) REFERENCES resources (id);

CREATE TABLE virtual_technology_resource (
    id BIGINT NOT NULL,
    CONSTRAINT pk_virtual_technology_resource PRIMARY KEY (id)
);
ALTER TABLE virtual_technology_resource ADD CONSTRAINT FK_VIRTUAL_TECHNOLOGY_RESOURCE_ON_ID FOREIGN KEY (id) REFERENCES resources (id);
