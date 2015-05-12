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
  lastclose numeric,
  CONSTRAINT stockprice_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE stockprice
  OWNER TO postgres;
COMMENT ON TABLE stockprice
  IS 'STOCK PRICE';


-- Table: ind_macd

-- DROP TABLE ind_macd;

CREATE TABLE ind_macd
(
  stockid text NOT NULL,
  date text NOT NULL,
  dif numeric,
  dea numeric,
  macd numeric,
  CONSTRAINT macd_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_macd
  OWNER TO postgres;
GRANT ALL ON TABLE ind_macd TO public;
GRANT ALL ON TABLE ind_macd TO postgres;


-- Table: ind_kdj

-- DROP TABLE ind_kdj;

CREATE TABLE ind_kdj
(
  stockid text NOT NULL,
  date text NOT NULL,
  k numeric,
  d numeric,
  j numeric,
  CONSTRAINT kdj_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_kdj
  OWNER TO postgres;
GRANT ALL ON TABLE ind_kdj TO public;
GRANT ALL ON TABLE ind_kdj TO postgres;

-- Table: ind_boll

-- DROP TABLE ind_boll;

CREATE TABLE ind_boll
(
  stockid text NOT NULL,
  date text NOT NULL,
  mb numeric,
  up numeric,
  dn numeric,
  CONSTRAINT boll_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_boll
  OWNER TO postgres;
GRANT ALL ON TABLE ind_boll TO public;
GRANT ALL ON TABLE ind_boll TO postgres;

-- Table: ind_shenxian

-- DROP TABLE ind_shenxian;

CREATE TABLE ind_shenxian
(
  stockid text NOT NULL,
  date text NOT NULL,
  h1 numeric,
  h2 numeric,
  h3 numeric,
  CONSTRAINT shenxian_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_shenxian
  OWNER TO postgres;
GRANT ALL ON TABLE ind_shenxian TO public;
GRANT ALL ON TABLE ind_shenxian TO postgres;

-- Table: ind_week_shenxian

-- DROP TABLE ind_week_shenxian;

CREATE TABLE ind_week_shenxian
(
  stockid text NOT NULL,
  date text NOT NULL,
  h1 numeric,
  h2 numeric,
  h3 numeric,
  CONSTRAINT week_shenxian_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_week_shenxian
  OWNER TO postgres;
GRANT ALL ON TABLE ind_week_shenxian TO public;
GRANT ALL ON TABLE ind_week_shenxian TO postgres;

-- Table: ind_event

-- DROP TABLE ind_event;

CREATE TABLE ind_event
(
  date text NOT NULL,
  checkpoint text NOT NULL,
  stockidlist text NOT NULL,
  CONSTRAINT event_primary_key PRIMARY KEY (date, checkpoint)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_event
  OWNER TO postgres;
GRANT ALL ON TABLE ind_event TO public;
GRANT ALL ON TABLE ind_event TO postgres;

-- Table: ind_week_boll

-- DROP TABLE ind_week_boll;

CREATE TABLE ind_week_boll
(
  stockid text NOT NULL,
  date text NOT NULL,
  mb numeric,
  up numeric,
  dn numeric,
  CONSTRAINT week_boll_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_week_boll
  OWNER TO postgres;
GRANT ALL ON TABLE ind_week_boll TO public;
GRANT ALL ON TABLE ind_week_boll TO postgres;


-- Table: ind_week_kdj

-- DROP TABLE ind_week_kdj;

CREATE TABLE ind_week_kdj
(
  stockid text NOT NULL,
  date text NOT NULL,
  k numeric,
  d numeric,
  j numeric,
  CONSTRAINT week_kdj_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_week_kdj
  OWNER TO postgres;
GRANT ALL ON TABLE ind_week_kdj TO public;
GRANT ALL ON TABLE ind_week_kdj TO postgres;

-- Table: ind_week_macd

-- DROP TABLE ind_week_macd;

CREATE TABLE ind_week_macd
(
  stockid text NOT NULL,
  date text NOT NULL,
  dif numeric,
  dea numeric,
  macd numeric,
  CONSTRAINT week_macd_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_week_macd
  OWNER TO postgres;
GRANT ALL ON TABLE ind_week_macd TO public;
GRANT ALL ON TABLE ind_week_macd TO postgres;

-- Table: week_stockprice

-- DROP TABLE week_stockprice;

CREATE TABLE week_stockprice
(
  stockid text NOT NULL,
  date text NOT NULL,
  open numeric NOT NULL,
  high numeric NOT NULL,
  low numeric NOT NULL,
  close numeric NOT NULL,
  volume bigint NOT NULL,
  lastclose numeric,
  CONSTRAINT week_stockprice_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE week_stockprice
  OWNER TO postgres;
GRANT ALL ON TABLE week_stockprice TO public;
GRANT ALL ON TABLE week_stockprice TO postgres;
COMMENT ON TABLE week_stockprice
  IS 'WEEK STOCK PRICE';
  
-- Table: checkpoint_daily_selection

-- DROP TABLE checkpoint_daily_selection;

CREATE TABLE checkpoint_daily_selection
(
  date text NOT NULL,
  checkpoint text NOT NULL,
  stockidlist text NOT NULL,
  CONSTRAINT event_primary_key PRIMARY KEY (date, checkpoint)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE checkpoint_daily_selection
  OWNER TO postgres;
GRANT ALL ON TABLE checkpoint_daily_selection TO postgres;
GRANT ALL ON TABLE checkpoint_daily_selection TO public;
COMMENT ON TABLE checkpoint_daily_selection
  IS 'Store daily checkpoint analyse report, selecting stock based on checkpoint.';
  
-- Table: checkpoint_history_analyse

-- DROP TABLE checkpoint_history_analyse;

CREATE TABLE checkpoint_history_analyse
(
  checkpoint text,
  total_satisfy integer,
  close_earn_percent numeric,
  high_earn_percent numeric,
  low_earn_percent numeric,
  avg_hold_days integer,
  total_high_earn integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE checkpoint_history_analyse
  OWNER TO postgres;
GRANT ALL ON TABLE checkpoint_history_analyse TO public;
GRANT ALL ON TABLE checkpoint_history_analyse TO postgres;

-- Table: checkpoint_history_selection

-- DROP TABLE checkpoint_history_selection;

CREATE TABLE checkpoint_history_selection
(
  stockid text,
  checkpoint text,
  buydate text,
  selldate text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE checkpoint_history_selection
  OWNER TO postgres;
GRANT ALL ON TABLE checkpoint_history_selection TO public;
GRANT ALL ON TABLE checkpoint_history_selection TO postgres;
COMMENT ON TABLE checkpoint_history_selection
  IS 'Store history checkpoint high earn percent  (>50) reports';  
