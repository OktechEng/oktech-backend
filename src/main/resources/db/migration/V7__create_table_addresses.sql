CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    zip_code VARCHAR(9) NOT NULL, -- zip_code é um nome melhor que cep
    street VARCHAR(255) NOT NULL,
    number VARCHAR(20) NOT NULL,
    complement VARCHAR(100), -- Complemento pode ser opcional
    neighborhood VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,
    address_type VARCHAR(50) NOT NULL, -- Ex: 'RESIDENCIAL', 'COMERCIAL'
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);