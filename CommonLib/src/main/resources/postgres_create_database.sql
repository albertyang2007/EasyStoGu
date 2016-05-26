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
  stockid text NOT NULL,
  date text NOT NULL,
  checkpoint text NOT NULL,
  CONSTRAINT checkpoint_daily_selection_primary_key PRIMARY KEY (stockid, date, checkpoint)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE checkpoint_daily_selection
  OWNER TO postgres;
GRANT ALL ON TABLE checkpoint_daily_selection TO public;
GRANT ALL ON TABLE checkpoint_daily_selection TO postgres;
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

  
-- Table: event_chuquanchuxi

-- DROP TABLE event_chuquanchuxi;

CREATE TABLE event_chuquanchuxi
(
  stockid text NOT NULL,
  date text NOT NULL,
  rate numeric NOT NULL,
  alreadyupdateprice boolean,
  CONSTRAINT chuquanchuxi_primary PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE event_chuquanchuxi
  OWNER TO postgres;
GRANT ALL ON TABLE event_chuquanchuxi TO public;
GRANT ALL ON TABLE event_chuquanchuxi TO postgres;
COMMENT ON TABLE event_chuquanchuxi
  IS 'ChuQuan ChuXi event for stock';

  
-- Table: ind_xueshi2

-- DROP TABLE ind_xueshi2;

CREATE TABLE ind_xueshi2
(
  stockid text NOT NULL,
  date text NOT NULL,
  up numeric,
  dn numeric,
  CONSTRAINT xueshi2_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_xueshi2
  OWNER TO postgres;
GRANT ALL ON TABLE ind_xueshi2 TO public;
GRANT ALL ON TABLE ind_xueshi2 TO postgres;

-- Table: ind_week_xueshi2

-- DROP TABLE ind_week_xueshi2;

CREATE TABLE ind_week_xueshi2
(
  stockid text NOT NULL,
  date text NOT NULL,
  up numeric,
  dn numeric,
  CONSTRAINT week_xueshi2_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_week_xueshi2
  OWNER TO postgres;
GRANT ALL ON TABLE ind_week_xueshi2 TO public;
GRANT ALL ON TABLE ind_week_xueshi2 TO postgres;

-- Table: ind_mai1mai2

-- DROP TABLE ind_mai1mai2;

CREATE TABLE ind_mai1mai2
(
  stockid text NOT NULL,
  date text NOT NULL,
  sd numeric,
  sk numeric,
  CONSTRAINT ind_mai1mai2_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_mai1mai2
  OWNER TO postgres;
GRANT ALL ON TABLE ind_mai1mai2 TO public;
GRANT ALL ON TABLE ind_mai1mai2 TO postgres;

-- Table: ind_week_mai1mai2

-- DROP TABLE ind_week_mai1mai2;

CREATE TABLE ind_week_mai1mai2
(
  stockid text NOT NULL,
  date text NOT NULL,
  sd numeric,
  sk numeric,
  CONSTRAINT ind_week_mai1mai2_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_week_mai1mai2
  OWNER TO postgres;
GRANT ALL ON TABLE ind_week_mai1mai2 TO public;
GRANT ALL ON TABLE ind_week_mai1mai2 TO postgres;

-- Table: ind_zhulijinchu

-- DROP TABLE ind_zhulijinchu;

CREATE TABLE ind_zhulijinchu
(
  stockid text NOT NULL,
  date text NOT NULL,
  duofang numeric,
  kongfang numeric,
  CONSTRAINT ind_zhulijinchu_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_zhulijinchu
  OWNER TO postgres;
GRANT ALL ON TABLE ind_zhulijinchu TO public;
GRANT ALL ON TABLE ind_zhulijinchu TO postgres;

-- Table: ind_week_zhulijinchu

-- DROP TABLE ind_week_zhulijinchu;

CREATE TABLE ind_week_zhulijinchu
(
  stockid text NOT NULL,
  date text NOT NULL,
  duofang numeric,
  kongfang numeric,
  CONSTRAINT ind_week_zhulijinchu_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_week_zhulijinchu
  OWNER TO postgres;
GRANT ALL ON TABLE ind_week_zhulijinchu TO public;
GRANT ALL ON TABLE ind_week_zhulijinchu TO postgres;

-- Table: zijinliu

-- DROP TABLE zijinliu;

CREATE TABLE zijinliu
(
  stockid text NOT NULL,
  date text NOT NULL,
  rate integer,
  incper text,
  majornetin numeric,
  majornetper numeric,
  biggestnetin numeric,
  biggestnetper numeric,
  bignetin numeric,
  bignetper numeric,
  midnetin numeric,
  midnetper numeric,
  smallnetin numeric,
  smallnetper numeric,
  CONSTRAINT zijinliu_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zijinliu
  OWNER TO postgres;
GRANT ALL ON TABLE zijinliu TO public;
GRANT ALL ON TABLE zijinliu TO postgres;

-- Table: zijinliu_3day

-- DROP TABLE zijinliu_3day;

CREATE TABLE zijinliu_3day
(
  stockid text NOT NULL,
  date text NOT NULL,
  rate integer,
  incper text,
  majornetin numeric,
  majornetper numeric,
  biggestnetin numeric,
  biggestnetper numeric,
  bignetin numeric,
  bignetper numeric,
  midnetin numeric,
  midnetper numeric,
  smallnetin numeric,
  smallnetper numeric,
  CONSTRAINT zijinliu_3day_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zijinliu_3day
  OWNER TO postgres;
GRANT ALL ON TABLE zijinliu_3day TO public;
GRANT ALL ON TABLE zijinliu_3day TO postgres;


-- Table: zijinliu_5day

-- DROP TABLE zijinliu_5day;

CREATE TABLE zijinliu_5day
(
  stockid text NOT NULL,
  date text NOT NULL,
  rate integer,
  incper text,
  majornetin numeric,
  majornetper numeric,
  biggestnetin numeric,
  biggestnetper numeric,
  bignetin numeric,
  bignetper numeric,
  midnetin numeric,
  midnetper numeric,
  smallnetin numeric,
  smallnetper numeric,
  CONSTRAINT zijinliu_5day_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zijinliu_5day
  OWNER TO postgres;
GRANT ALL ON TABLE zijinliu_5day TO public;
GRANT ALL ON TABLE zijinliu_5day TO postgres;


-- Table: ind_yimengbs

-- DROP TABLE ind_yimengbs;

CREATE TABLE ind_yimengbs
(
  stockid text NOT NULL,
  date text NOT NULL,
  x2 numeric,
  x3 numeric,
  CONSTRAINT yimengbs_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_yimengbs
  OWNER TO postgres;
GRANT ALL ON TABLE ind_yimengbs TO public;
GRANT ALL ON TABLE ind_yimengbs TO postgres;

-- Table: ind_week_yimengbs

-- DROP TABLE ind_week_yimengbs;

CREATE TABLE ind_week_yimengbs
(
  stockid text NOT NULL,
  date text NOT NULL,
  x2 numeric,
  x3 numeric,
  CONSTRAINT week_yimengbs_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_week_yimengbs
  OWNER TO postgres;
GRANT ALL ON TABLE ind_week_yimengbs TO public;
GRANT ALL ON TABLE ind_week_yimengbs TO postgres;

-- Table: zhulijingliuru

-- DROP TABLE zhulijingliuru;

CREATE TABLE zhulijingliuru
(
  stockid text NOT NULL,
  date text NOT NULL,
  rate integer,
  price numeric,
  majornetper numeric,
  incper text,
  CONSTRAINT zhulijingliuru_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zhulijingliuru
  OWNER TO postgres;
GRANT ALL ON TABLE zhulijingliuru TO public;
GRANT ALL ON TABLE zhulijingliuru TO postgres;

-- Table: ind_ddx

-- DROP TABLE ind_ddx;

CREATE TABLE ind_ddx
(
  stockid text NOT NULL,
  date text NOT NULL,
  ddx numeric,
  ddy numeric,
  ddz numeric,
  CONSTRAINT ind_ddx_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_ddx
  OWNER TO postgres;
GRANT ALL ON TABLE ind_ddx TO public;
GRANT ALL ON TABLE ind_ddx TO postgres;

-- Table: ind_qsdd

-- DROP TABLE ind_qsdd;

CREATE TABLE ind_qsdd
(
  stockid text NOT NULL,
  date text NOT NULL,
  lonterm numeric,
  shoterm numeric,
  midterm numeric,
  CONSTRAINT ind_qsdd_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_qsdd
  OWNER TO postgres;
GRANT ALL ON TABLE ind_qsdd TO public;
GRANT ALL ON TABLE ind_qsdd TO postgres;


-- Table: ind_week_qsdd

-- DROP TABLE ind_week_qsdd;

CREATE TABLE ind_week_qsdd
(
  stockid text NOT NULL,
  date text NOT NULL,
  lonterm numeric,
  midterm numeric,
  shoterm numeric,
  CONSTRAINT ind_week_sdd_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_week_qsdd
  OWNER TO postgres;
GRANT ALL ON TABLE ind_week_qsdd TO public;
GRANT ALL ON TABLE ind_week_qsdd TO postgres;

-- Table: checkpoint_daily_statistics

-- DROP TABLE checkpoint_daily_statistics;

CREATE TABLE checkpoint_daily_statistics
(
  date text NOT NULL,
  checkpoint text NOT NULL,
  count integer,
  CONSTRAINT checkpoint_daily_statistics_primary_key PRIMARY KEY (date, checkpoint)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE checkpoint_daily_statistics
  OWNER TO postgres;
GRANT ALL ON TABLE checkpoint_daily_statistics TO public;
GRANT ALL ON TABLE checkpoint_daily_statistics TO postgres;
COMMENT ON TABLE checkpoint_daily_statistics
  IS 'Store daily checkpoint statistics report, selecting checkpoint count based on date.';


-- Table: hou_fuquan_stockprice

-- DROP TABLE hou_fuquan_stockprice;

CREATE TABLE hou_fuquan_stockprice
(
  stockid text NOT NULL,
  date text NOT NULL,
  open numeric NOT NULL,
  high numeric NOT NULL,
  low numeric NOT NULL,
  close numeric NOT NULL,
  volume bigint NOT NULL,
  lastclose numeric,
  CONSTRAINT hou_fuquan_stockprice_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE hou_fuquan_stockprice
  OWNER TO postgres;
GRANT ALL ON TABLE hou_fuquan_stockprice TO public;
GRANT ALL ON TABLE hou_fuquan_stockprice TO postgres;
COMMENT ON TABLE hou_fuquan_stockprice
  IS 'Hou Fu Quan STOCK PRICE';
  

-- Table: company_info

-- DROP TABLE company_info;

CREATE TABLE company_info
(
  stockid text NOT NULL,
  name text NOT NULL,
  totalguben numeric,
  liutongagu numeric,
  updatetime text NULL,
  CONSTRAINT company_info_primary_key PRIMARY KEY (stockid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE company_info
  OWNER TO postgres;
GRANT ALL ON TABLE company_info TO public;
GRANT ALL ON TABLE company_info TO postgres;
COMMENT ON TABLE company_info
  IS 'STOCK Company Base Info';

  
-- Table: schedule_action

-- DROP TABLE schedule_action;

CREATE TABLE schedule_action
(
  stockid text NOT NULL,
  runDate text NOT NULL,
  createDate text NOT NULL,
  actionDo text NOT NULL,
  params text NULL,
  CONSTRAINT schedule_action_primary PRIMARY KEY (stockid, runDate, actionDo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE schedule_action
  OWNER TO postgres;
GRANT ALL ON TABLE schedule_action TO public;
GRANT ALL ON TABLE schedule_action TO postgres;
COMMENT ON TABLE schedule_action
  IS 'Schedule action that will be run later';  
  

-- Table: qian_fuquan_stockprice

-- DROP TABLE qian_fuquan_stockprice;

CREATE TABLE qian_fuquan_stockprice
(
  stockid text NOT NULL,
  date text NOT NULL,
  open numeric NOT NULL,
  high numeric NOT NULL,
  low numeric NOT NULL,
  close numeric NOT NULL,
  volume bigint NOT NULL,
  lastclose numeric,
  CONSTRAINT qian_fuquan_stockprice_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE qian_fuquan_stockprice
  OWNER TO postgres;
GRANT ALL ON TABLE qian_fuquan_stockprice TO public;
GRANT ALL ON TABLE qian_fuquan_stockprice TO postgres;
COMMENT ON TABLE qian_fuquan_stockprice
  IS 'Qian Fu Quan STOCK PRICE';
  
-- Table: ind_wr

-- DROP TABLE ind_wr;

CREATE TABLE ind_wr
(
  stockid text NOT NULL,
  date text NOT NULL,
  lonterm numeric,
  shoterm numeric,
  midterm numeric,
  CONSTRAINT ind_wr_primary_key PRIMARY KEY (stockid, date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ind_wr
  OWNER TO postgres;
GRANT ALL ON TABLE ind_wr TO public;
GRANT ALL ON TABLE ind_wr TO postgres;
