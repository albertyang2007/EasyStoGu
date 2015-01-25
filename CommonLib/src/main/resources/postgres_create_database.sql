-- Database: easystogu

-- DROP DATABASE easystogu;

CREATE DATABASE easystogu
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Chinese (Simplified)_People''s Republic of China.936'
       LC_CTYPE = 'Chinese (Simplified)_People''s Republic of China.936'
       CONNECTION LIMIT = -1;

ALTER DEFAULT PRIVILEGES 
    GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON TABLES
    TO public;

ALTER DEFAULT PRIVILEGES 
    GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON TABLES
    TO postgres;

COMMENT ON DATABASE easystogu
  IS 'easystogu for stock';
  

-- Table: stockprice

-- DROP TABLE stockprice;

CREATE TABLE stockprice
(
  stockid text NOT NULL,
  date text NOT NULL,
  open numeric NOT NULL,
  high numeric NOT NULL,
  low numeric NOT NULL,
  close numeric NOT NULL,
  volume bigint NOT NULL,
  CONSTRAINT "stockIdDate" PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE stockprice
  OWNER TO postgres;
COMMENT ON TABLE stockprice
  IS 'STOCK PRICE';
