-- V5: Renombrar columna last_name a lastname en tabla users
-- Fecha: 2025-09-03
-- Descripción: Renombra last_name a lastname para coincidir con el modelo User.java

-- Renombrar la columna existente last_name a lastname
ALTER TABLE users RENAME COLUMN last_name TO lastname;

-- Comentario: Se renombra la columna para mantener consistencia con la entidad User.java
