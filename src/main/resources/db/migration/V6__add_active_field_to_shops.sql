-- Add active field to shops table
ALTER TABLE shops ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE;

-- Add index for faster lookups by active status
CREATE INDEX IF NOT EXISTS idx_shops_active ON shops(active);
