-- Create UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create bill status enum
CREATE TYPE bill_status AS ENUM (
    'PENDING',
    'PAID',
    'OVERDUE',
    'CANCELLED'
);

-- Create bills table
CREATE TABLE bills (
                       id BIGSERIAL PRIMARY KEY,
                       due_date DATE NOT NULL,
                       payment_date DATE,
                       amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
                       description VARCHAR(255) NOT NULL CHECK (LENGTH(TRIM(description)) > 0),
                       status bill_status NOT NULL DEFAULT 'PENDING',
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT payment_date_not_future CHECK (payment_date IS NULL OR payment_date <= CURRENT_DATE),
                       CONSTRAINT payment_date_requires_paid_status CHECK (
                           (status = 'PAID' AND payment_date IS NOT NULL) OR
                           (status != 'PAID' AND payment_date IS NULL)
                           )
);

-- Create indexes
CREATE INDEX idx_bills_status ON bills(status);
CREATE INDEX idx_bills_due_date ON bills(due_date);
CREATE INDEX idx_bills_payment_date ON bills(payment_date);
CREATE INDEX idx_bills_description_pattern ON bills(description varchar_pattern_ops);

-- Add updated_at trigger
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_bills_updated_at
    BEFORE UPDATE ON bills
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Add comments
COMMENT ON TABLE bills IS 'Stores bill payment information';
COMMENT ON COLUMN bills.id IS 'Unique identifier for the bill';
COMMENT ON COLUMN bills.due_date IS 'Date when the bill is due';
COMMENT ON COLUMN bills.payment_date IS 'Date when the bill was paid, null if unpaid';
COMMENT ON COLUMN bills.amount IS 'Bill amount in the system currency';
COMMENT ON COLUMN bills.description IS 'Description of what the bill is for';
COMMENT ON COLUMN bills.status IS 'Current status of the bill';
COMMENT ON COLUMN bills.created_at IS 'Timestamp when the bill was created';
COMMENT ON COLUMN bills.updated_at IS 'Timestamp when the bill was last updated';