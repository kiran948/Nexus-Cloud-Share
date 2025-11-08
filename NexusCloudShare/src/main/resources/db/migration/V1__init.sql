-- Flyway migration: create initial tables

CREATE TABLE IF NOT EXISTS profiles (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  clerk_id VARCHAR(255),
  email VARCHAR(255),
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  credits INT,
  photo_url VARCHAR(1024),
  created_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_profiles_email ON profiles(email);
CREATE INDEX IF NOT EXISTS idx_profiles_clerk_id ON profiles(clerk_id);

CREATE TABLE IF NOT EXISTS user_credits (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  clerk_id VARCHAR(255),
  credits INT,
  plan VARCHAR(50)
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_credits_clerk_id ON user_credits(clerk_id);

CREATE TABLE IF NOT EXISTS files (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  name VARCHAR(1024),
  type VARCHAR(255),
  size BIGINT,
  clerk_id VARCHAR(255),
  is_public BOOLEAN,
  file_location VARCHAR(2048),
  uploaded_at DATETIME
);
CREATE INDEX IF NOT EXISTS idx_files_clerk_id ON files(clerk_id);

CREATE TABLE IF NOT EXISTS payment_transactions (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  clerk_id VARCHAR(255),
  order_id VARCHAR(255),
  payment_id VARCHAR(255),
  plan_id VARCHAR(255),
  amount INT,
  currency VARCHAR(10),
  credits_added INT,
  status VARCHAR(50),
  transaction_date DATETIME,
  user_email VARCHAR(255),
  user_name VARCHAR(255)
);
CREATE INDEX IF NOT EXISTS idx_payment_transactions_clerk_id ON payment_transactions(clerk_id);
