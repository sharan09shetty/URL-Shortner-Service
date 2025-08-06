CREATE TABLE IF NOT EXISTS client
  (
     id                UUID DEFAULT Gen_random_uuid() PRIMARY KEY,
     name              TEXT,
     client_identifier TEXT NOT NULL,
     api_key           TEXT NOT NULL,
     created           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

