-- V6: Update user role check constraint to include all UserRole enum values
-- This migration fixes the constraint to allow all valid UserRole values

-- Drop the existing constraint
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_user_role_check;

-- Add the updated constraint with all UserRole enum values
ALTER TABLE users ADD CONSTRAINT users_user_role_check 
    CHECK (user_role IN ('USER', 'ADMIN', 'SUPERADMIN', 'EXPERT', 'TALENT', 'SECURITY'));