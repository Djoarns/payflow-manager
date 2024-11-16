CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT true,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL REFERENCES users(id),
                            roles VARCHAR(20) NOT NULL,
                            PRIMARY KEY (user_id, roles)
);

-- Add indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);

-- Add comments
COMMENT ON TABLE users IS 'Stores user account information';
COMMENT ON COLUMN users.id IS 'Unique identifier for the user';
COMMENT ON COLUMN users.username IS 'Unique username for login';
COMMENT ON COLUMN users.password IS 'Hashed password';
COMMENT ON COLUMN users.enabled IS 'Whether the user account is active';
COMMENT ON COLUMN users.created_at IS 'Timestamp when the user was created';
COMMENT ON COLUMN users.updated_at IS 'Timestamp when the user was last updated';

COMMENT ON TABLE user_roles IS 'Stores user roles for authorization';
COMMENT ON COLUMN user_roles.user_id IS 'Reference to the user';
COMMENT ON COLUMN user_roles.roles IS 'Role name (USER, ADMIN, etc)';

-- Add updated_at trigger for users table
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();