CREATE TABLE IF NOT EXISTS client
  (
     id                UUID DEFAULT Gen_random_uuid() PRIMARY KEY,
     name              TEXT,
     client_identifier TEXT NOT NULL,
     api_key           TEXT NOT NULL,
     created           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

ALTER TABLE client ADD CONSTRAINT unique_client_identifier UNIQUE (client_identifier);
ALTER TABLE client ALTER COLUMN name SET not null;

CREATE TABLE shortened_url
  (
     id                UUID DEFAULT Gen_random_uuid() PRIMARY KEY,
     client_identifier TEXT NOT NULL,
     customer_id       TEXT,
     shortcode     TEXT UNIQUE NOT NULL,
     redirect_url      TEXT NOT NULL,
     is_single_access  BOOLEAN,
     expires_at        TIMESTAMP,
     click_count       INT DEFAULT 0,
     created           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

