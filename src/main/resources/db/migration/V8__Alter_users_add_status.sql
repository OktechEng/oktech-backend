-- Primeiro, remove a coluna antiga
ALTER TABLE users DROP COLUMN is_active;

-- Depois, adiciona a nova coluna de status
ALTER TABLE users ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE';