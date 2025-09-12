CREATE TABLE subscriptions (
    id UUID PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    plan VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    is_active BOOLEAN NOT NULL
);