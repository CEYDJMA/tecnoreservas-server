-- V3: Actualizaciones consolidadas del esquema

-- Paso 1: Eliminar tablas de recursos hijas que ya no se necesitan.
DROP TABLE IF EXISTS electronics_resource;
DROP TABLE IF EXISTS engineering_resource;
DROP TABLE IF EXISTS virtual_technology_resource;

-- Paso 2: Limpiar y refactorizar la tabla 'resources'.
-- Se eliminan columnas obsoletas y se añade la columna 'brand' de nuevo.
ALTER TABLE resources
    DROP COLUMN IF EXISTS internal_id,
    DROP COLUMN IF EXISTS location,
    ADD COLUMN brand VARCHAR(255);

-- Se establece un valor por defecto temporal para 'brand' en registros existentes antes de hacerlo NOT NULL.
UPDATE resources SET brand = 'Indefinida' WHERE brand IS NULL;

ALTER TABLE resources
    ALTER COLUMN brand SET NOT NULL;

-- CORRECCIÓN: Se renombra 'serial_number' a 'plate' para alinear con la entidad.
ALTER TABLE resources RENAME COLUMN serial_number TO plate;


-- Paso 3: Añadir los campos específicos a la tabla 'biotechnology_resource'.
ALTER TABLE biotechnology_resource
    ADD COLUMN max_usuarios_simultaneos INTEGER,
    ADD COLUMN condiciones_de_uso JSONB;

-- Se actualizan los valores nulos a los valores por defecto discutidos antes de añadir las restricciones NOT NULL.
UPDATE biotechnology_resource SET max_usuarios_simultaneos = 3 WHERE max_usuarios_simultaneos IS NULL;
UPDATE biotechnology_resource SET condiciones_de_uso = '{}'::jsonb WHERE condiciones_de_uso IS NULL;

-- Se aplican las restricciones NOT NULL y los valores por defecto.
ALTER TABLE biotechnology_resource
    ALTER COLUMN max_usuarios_simultaneos SET DEFAULT 3,
    ALTER COLUMN max_usuarios_simultaneos SET NOT NULL,
    ALTER COLUMN condiciones_de_uso SET DEFAULT '{}'::jsonb,
    ALTER COLUMN condiciones_de_uso SET NOT NULL;